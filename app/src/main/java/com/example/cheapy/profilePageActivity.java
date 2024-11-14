package com.example.cheapy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.API.UserAPI;
import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.databinding.ActivityProfilePageBinding;
import com.example.cheapy.entities.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class profilePageActivity extends AppCompatActivity {
    private ActivityProfilePageBinding binding;
    String activeUserName;
    String userToken;
    String selectedCity;
    String savedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }
        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationMyProfile) {
                return true;
            } else if (itemId == R.id.navigationHome) {
                Intent homeIntent = new Intent(profilePageActivity.this, HomePageActivity.class);
                homeIntent.putExtra("activeUserName", activeUserName);
                homeIntent.putExtra("token", userToken);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(profilePageActivity.this, CartActivity.class);
                cartIntent.putExtra("activeUserName", activeUserName);
                cartIntent.putExtra("token", userToken);
                startActivity(cartIntent);
                return true;
            } else {
                return false;
            }
        });

        UserAPI userAPI = new UserAPI();
        userAPI.getUserDetails(activeUserName, userToken, callback -> {
            if(callback == 200) {
                User u = userAPI.getUser();
                if (u!=null) {
                    binding.userDisplayName.setText(u.getDisplayName());
                    binding.userImage.setImageBitmap(decodeImage(u.getProfilePic()));
                    binding.userMail.setText(u.getMail());
                    binding.userPhone.setText(u.getPhone());
                    setUpAddressSpinner(u);
                }
                else {
                    //error
                }
            }
            else {
                //error
            }
        });
    }

    private void setUpAddressSpinner(User user) {
        Spinner addressSpinner = binding.addressSpinner;
        List<String> addressOptions = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        savedCity = sharedPreferences.getString("selected_city", "");

        if (savedCity.equals(user.getHomeAddress())) {
            if (user.getHomeAddress() != null && !user.getHomeAddress().isEmpty()) {
                addressOptions.add("בית: " + user.getHomeAddress());
            }
            if (user.getWorkAddress() != null && !user.getWorkAddress().isEmpty()) {
                addressOptions.add("עבודה: " + user.getWorkAddress());
            }
        }
        else {
            if (user.getWorkAddress() != null && !user.getWorkAddress().isEmpty()) {
                addressOptions.add("עבודה: " + user.getWorkAddress());
            }
            if (user.getHomeAddress() != null && !user.getHomeAddress().isEmpty()) {
                addressOptions.add("בית: " + user.getHomeAddress());
            }
        }
        // Set up ArrayAdapter with dynamic data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(adapter);

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.equals("בחר כתובת")) {
                    selectedCity = null;
                } else {
                    if (selectedOption.startsWith("בית: ")) {
                        selectedCity = selectedOption.replace("בית: ", "");
                    } else if (selectedOption.startsWith("עבודה: ")) {
                        selectedCity = selectedOption.replace("עבודה: ", "");
                    }
                }
                SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_city", selectedCity);
                editor.apply();
                Log.d("Selected City", selectedCity != null ? selectedCity : "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private Bitmap decodeImage(String imageString) {
        try {
            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } catch (Exception e) {
            Toast.makeText(Cheapy.context,
                    "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();;
        }
        return null;
    }
}

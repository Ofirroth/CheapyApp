package com.example.cheapy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.cheapy.API.UserAPI;
import com.example.cheapy.Cart.CartActivity;
import com.example.cheapy.Home_page.HomePageActivity;
import com.example.cheapy.databinding.ActivityProfilePageBinding;
import com.example.cheapy.entities.User;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class profilePageActivity extends AppCompatActivity {
    private ActivityProfilePageBinding binding;
    String activeUserName;
    String userToken;
    String selectedCity;
    String savedCity;

    String currentLocation;

    double userLatitude;
    double userLongitude;

    Location userWork;

    Location userHome;
    User user;

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;


    private static final int LOCATION_REQUEST_CODE = 100;

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
                homeIntent.putExtra("latitude", userLatitude);
                homeIntent.putExtra("longitude", userLongitude);
                homeIntent.putExtra("selectedCity", selectedCity);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.navigationCheckout) {
                Intent cartIntent = new Intent(profilePageActivity.this, CartActivity.class);
                cartIntent.putExtra("activeUserName", activeUserName);
                cartIntent.putExtra("token", userToken);
                cartIntent.putExtra("latitude", userLatitude);
                cartIntent.putExtra("longitude", userLongitude);
                cartIntent.putExtra("selectedCity", selectedCity);
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
                user = u;
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

        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());
    }
    public void onSwitchClick(View view) {
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch switchLocation = (Switch) view;
        boolean isChecked = switchLocation.isChecked();

        if (isChecked) {
            requestLocationPermission();
        } else {
            stopLocationUpdates();
            restoreStaticSpinnerData();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            checkLocationServicesEnabled();
        }
    }

    private void checkLocationServicesEnabled() {
        LocationRequest locationRequest = LocationRequest.create();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(builder.build())
                .addOnSuccessListener(locationSettingsResponse -> {
                    fetchUserLocation();
                })
                .addOnFailureListener(e -> {
                    if (e instanceof ResolvableApiException) {
                        try {
                            ((ResolvableApiException) e).startResolutionForResult(this, LOCATION_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException sendEx) {
                            Log.e("LocationServices", "Error starting resolution for location services.");
                        }
                    } else {
                        Toast.makeText(this, "Please enable location services.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void restoreStaticSpinnerData() {
        UserAPI userAPI = new UserAPI();
        userAPI.getUserDetails(activeUserName, userToken, callback -> {
            if(callback == 200) {
                User u = userAPI.getUser();
                if (u!=null) {
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

    private String fetchCurrentAddress(double latitude, double longitude) {
        String currentAddress = "Unknown Location";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                currentAddress = addresses.get(0).getAddressLine(0);
            }
        } catch (Exception e) {
            Log.e("Geocoder Error", "Error fetching address: " + e.getMessage());
            // Fallback to coordinates if fetching address fails
            currentAddress = "Lat: " + latitude + ", Lon: " + longitude;
        }
        currentLocation = currentAddress;
        return currentAddress;
    }


    private void setUpAddressSpinner(User user) {
        Spinner addressSpinner = binding.addressSpinner;
        List<String> addressOptions = new ArrayList<>();

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
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
                sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("selected_city", selectedCity);
                editor.putString("location_name", currentLocation);
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



    private void fetchUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("PermissionCheck", "Permissions not granted. Requesting permissions...");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    requestNewLocationData();
//                    if (location != null) {
//                         userLatitude = location.getLatitude();
//                         userLongitude = location.getLongitude();
//                        Log.d("LocationSuccess", "Latitude: " + userLatitude + ", Longitude: " + userLongitude);
//                        handleLocation(userLatitude, userLongitude);
//                    } else {
//                        Log.e("LocationError", "Location is null. Requesting new location...");
//                        requestNewLocationData();
//                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LocationError", "Failed to fetch location: " + e.getMessage());
                    Toast.makeText(this, "Failed to fetch location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private void handleLocation(double latitude, double longitude) {
        Log.d("User Location", "Latitude: " + latitude + ", Longitude: " + longitude);
        updateAddressSpinnerWithLocation(latitude, longitude, user);
    }

    private void updateAddressSpinnerWithLocation(double latitude, double longitude, User user) {
        user.setUserLatitude(latitude);
        user.setUserLongitude(longitude);
        editor.putString("user_latitude", String.valueOf(userLatitude));
        editor.putString("user_longitude", String.valueOf(userLongitude));
        editor.apply();
        Spinner addressSpinner = binding.addressSpinner;
        List<String> addressOptions = new ArrayList<>();

        // Add current location
        String currentAddress = fetchCurrentAddress(latitude, longitude);
        addressOptions.add(currentAddress);

        // Add predefined "Home" and "Work" addresses
        if (user.getHomeAddress() != null && !user.getHomeAddress().isEmpty()) {
            addressOptions.add("Home: " + user.getHomeAddress());
        }
        if (user.getWorkAddress() != null && !user.getWorkAddress().isEmpty()) {
            addressOptions.add("Work: " + user.getWorkAddress());
        }

        // Set data to Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addressSpinner.setAdapter(adapter);

        addressSpinner.setSelection(0);

        addressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                if (selectedOption.startsWith("Current Location: ")) {
                    selectedCity = "Current Location";
                    userLatitude = latitude;
                    userLongitude = longitude;
                } else if (selectedOption.startsWith("Home: ")) {
                    selectedCity = user.getHomeAddress();
                } else if (selectedOption.startsWith("Work: ")) {
                    selectedCity = user.getWorkAddress();
                }

                Log.d("SpinnerSelection", "Selected: " + selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void requestNewLocationData() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            // Iterate through all the received locations
            for (Location location : locationResult.getLocations()) {
                userLatitude = location.getLatitude();
                userLongitude = location.getLongitude();
                Log.d("LocationCallback", "New location: " + userLongitude + ", " + userLatitude);
                handleLocation(userLatitude, userLongitude);
                stopLocationUpdates();
                break;
            }
        }
    };



    private List<String> fetchNearbyAddresses(double latitude, double longitude) {
        List<String> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> results = geocoder.getFromLocation(latitude, longitude, 5);
            for (Address address : results) {
                addresses.add(address.getAddressLine(0));
            }
        } catch (Exception e) {
            Log.e("Geocoder Error", e.getMessage());
        }
        return addresses;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, fetch user location
                fetchUserLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required to suggest nearby stores.", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void stopLocationUpdates() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.removeLocationUpdates(locationCallback);
        Log.d("LocationCallback", "Location updates stopped.");
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Optionally, resume location updates if the switch is on
        if (binding.switchLocation.isChecked()) {
            requestLocationPermission();
        }
    }

}

package com.example.cheapy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheapy.API.UserAPI;
import com.example.cheapy.Cart.CartManager;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.StoreDao;
import com.example.cheapy.adapters.StoreAdapter;
import com.example.cheapy.databinding.ActivityCheckoutPageBinding;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.User;
import com.example.cheapy.viewModels.CheckOutViewModel;

import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends AppCompatActivity {
    private Boolean isFilteredByLocation = false;
    private ActivityCheckoutPageBinding binding;
    private StoreAdapter storeAdapter;
    private Button btnProceedToCheckout;

    private ImageButton btnFilterByLocation;
    private double totalPrice2;
    private CheckOutViewModel viewModel;
    private List<Store> listStores;

    private List<Store> originalList;
    private Store selectedStore;
    private AppDB db;
    private StoreDao storeDao;
    String activeUserName;
    String userToken;
    String selectedCity;
    User user;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        this.db = DatabaseManager.getDatabase();
        this.storeDao = db.storeDao();
        this.viewModel = new CheckOutViewModel(this.userToken);
        this.listStores = new ArrayList<>();
        this.originalList = new ArrayList<>();

        UserAPI userAPI = new UserAPI();
        userAPI.getUserDetails(activeUserName, userToken, callback -> {
            if(callback == 200) {
                user = userAPI.getUser();
            }
            else {
                //error
            }
        });

        GridView lvItems = binding.gridViewStores;
        storeAdapter = new StoreAdapter(CheckOutActivity.this, this.listStores, userToken);

        // Set the selection listener
        storeAdapter.setOnStoreSelectedListener(new StoreAdapter.OnStoreSelectedListener() {
            @Override
            public void onStoreSelected(Store store) {
                selectedStore = store;
            }
        });

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        selectedCity = sharedPreferences.getString("selected_city", "");
        this.viewModel.getStores().observe(this, stores -> {
            if (stores != null && !stores.isEmpty()) {
                for (Store store : stores) {
                    Log.d("StoreValidation", "Store: " + store.getName() +
                            ", Location: " + (store.getNameLocation() != null ? store.getNameLocation() : "null"));
                }
                storeAdapter.setStores(stores);
                lvItems.setAdapter(storeAdapter);
                listStores.clear();
                listStores.addAll(stores);
                calculateTotalForSelectedStore();
                originalList.clear();
                originalList.addAll(stores);
            } else {
                Log.d("CheckOutActivity", "Store list is empty or null.");
            }
        });

        this.btnProceedToCheckout = binding.btnProceedToCheckout;

        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());

        calculateTotalForSelectedStore();

        this.btnFilterByLocation = binding.btnFilterByLocation;

        btnFilterByLocation.setOnClickListener(view -> {
            if (isFilteredByLocation) {
                storeAdapter.setStores(originalList); // Restore the full list
                originalList.sort((s1, s2) -> Double.compare(s1.getTotalPrice(), s2.getTotalPrice()));
                storeAdapter.notifyDataSetChanged();
                isFilteredByLocation = false;
                listStores.sort((s1, s2) -> Double.compare(s1.getTotalPrice(), s2.getTotalPrice()));
                storeAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Reset to price-based sorting.", Toast.LENGTH_SHORT).show();
            } else {
                // Apply location filtering
                double userLatitude = Double.parseDouble(sharedPreferences.getString("user_latitude", "0.0"));
                double userLongitude = Double.parseDouble(sharedPreferences.getString("user_longitude", "0.0"));

                if (userLatitude == 0.0 && userLongitude == 0.0) {
                    Toast.makeText(this, "No location found. Enable location services.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Store> filteredStores = filterStoresByLocation(userLatitude, userLongitude);

                if (!filteredStores.isEmpty()) {
                    // Sort by distance
                    filteredStores.sort((s1, s2) -> {
                        double distance1 = calculateDistance(userLatitude, userLongitude, s1.getLatitude(), s1.getLongitude());
                        double distance2 = calculateDistance(userLatitude, userLongitude, s2.getLatitude(), s2.getLongitude());
                        return Double.compare(distance1, distance2);
                    });

                    storeAdapter.setStores(filteredStores);
                    storeAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Stores filtered by location.", Toast.LENGTH_SHORT).show();
                    isFilteredByLocation = true; // Toggle on
                } else {
                    Toast.makeText(this, "No nearby stores found.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnProceedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStore != null) {
                    Intent intent = new Intent(CheckOutActivity.this, NewCartActivity.class);
                    intent.putExtra("store_id", selectedStore.getId());
                    intent.putExtra("store_name", selectedStore.getName());
                    intent.putExtra("token", userToken);
                    intent.putExtra("activeUserName", activeUserName);
                    intent.putExtra("store_id", selectedStore.getId());
                    intent.putExtra("total_price", selectedStore.getTotalPrice());
                    intent.putExtra("store_location", selectedStore.getNameLocation());
                    startActivity(intent);
                } else {
                    Toast.makeText(CheckOutActivity.this, "Please select a store to continue.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private List<Store> filterStoresByLocation(double userLatitude, double userLongitude) {
        double MAX_DISTANCE_KM = 10.0;
        List<Store> filteredStores = new ArrayList<>();

        for (Store store : listStores) {
            Log.d("StoreLocation", "Store: " + store.getName() +
                    ", Latitude: " + store.getLatitude() +
                    ", Longitude: " + store.getLongitude());
            double storeLatitude = store.getLatitude();
            double storeLongitude = store.getLongitude();

            double distance = calculateDistance(userLatitude, userLongitude, storeLatitude, storeLongitude);
            Log.d("Distance:", String.valueOf(distance));
            if (distance <= MAX_DISTANCE_KM) {
                filteredStores.add(store);
            }
        }

        return filteredStores;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

        // Convert latitude and longitude from degrees to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);

        Log.d("DistanceDebug", "lat1: " + lat1 + ", lon1: " + lon1 +
                ", lat2: " + lat2 + ", lon2: " + lon2);
        Log.d("DistanceDebug", "dLat: " + dLat + ", dLon: " + dLon);

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rLat1) * Math.cos(rLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        Log.d("DistanceDebug", "Intermediate a: " + a);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Log.d("DistanceDebug", "Intermediate c: " + c);

        double distance = EARTH_RADIUS_KM * c; // Distance in kilometers
        Log.d("DistanceDebug", "Calculated distance: " + distance);

        return distance;
    }



    private void calculateTotalForSelectedStore() {
        List<Item> selectedStoreItems = new ArrayList<>(CartManager.getInstance().getCartProducts());
        viewModel.fetchTotalPriceByStore(userToken, listStores, selectedStoreItems);

        for (Store store : listStores) {
            viewModel.getTotalPriceLiveDataForStore(store).observe(this, totalPrice -> {
                if (totalPrice != null) {
                    store.setTotalPrice(totalPrice);
                    totalPrice2 = totalPrice;
                    // Sort stores by total price each time a new price is set
                    listStores.sort((s1, s2) -> Double.compare(s1.getTotalPrice(), s2.getTotalPrice()));
                    storeAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        calculateTotalForSelectedStore();

    }
}
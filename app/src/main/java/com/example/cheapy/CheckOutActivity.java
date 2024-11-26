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

    // Toggle for filtering by location
    private Boolean isFilteredByLocation = false;

    // UI components and bindings
    private ActivityCheckoutPageBinding binding;
    private StoreAdapter storeAdapter;
    private Button btnProceedToCheckout;
    private ImageButton btnFilterByLocation;

    // Variables for handling store data and user details
    private double totalPrice2;
    private CheckOutViewModel viewModel;
    private List<Store> listStores; // Current store list
    private List<Store> originalList; // Backup for resetting filters
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

        // Retrieve intent extras for user information
        Intent intent = getIntent();
        if (intent != null) {
            activeUserName = getIntent().getStringExtra("activeUserName");
            userToken = getIntent().getStringExtra("token");
        }

        // Initialize database and view model
        this.db = DatabaseManager.getDatabase();
        this.storeDao = db.storeDao();
        this.viewModel = new CheckOutViewModel(this.userToken);
        this.listStores = new ArrayList<>();
        this.originalList = new ArrayList<>();

        // Fetch user details
        UserAPI userAPI = new UserAPI();
        userAPI.getUserDetails(activeUserName, userToken, callback -> {
            if (callback == 200) {
                user = userAPI.getUser();
            } else {
                Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the GridView for displaying stores
        GridView lvItems = binding.gridViewStores;
        storeAdapter = new StoreAdapter(CheckOutActivity.this, this.listStores, userToken);

        // Listener for store selection
        storeAdapter.setOnStoreSelectedListener(store -> selectedStore = store);

        // Load stores based on user's selected city
        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        selectedCity = sharedPreferences.getString("selected_city", "");
        this.viewModel.getStores().observe(this, stores -> {
            if (stores != null && !stores.isEmpty()) {
                // Update the store list and adapter
                storeAdapter.setStores(stores);
                lvItems.setAdapter(storeAdapter);
                listStores.clear();
                listStores.addAll(stores);
                calculateTotalForSelectedStore();
                originalList.clear();
                originalList.addAll(stores); // Backup for resetting
            } else {
                Log.d("CheckOutActivity", "Store list is empty or null.");
            }
        });

        // Set up the Proceed to Checkout button
        this.btnProceedToCheckout = binding.btnProceedToCheckout;
        this.btnProceedToCheckout.setOnClickListener(v -> {
            if (selectedStore != null) {
                // Navigate to the checkout page with selected store details
                Intent intentToCart = new Intent(CheckOutActivity.this, NewCartActivity.class);
                intentToCart.putExtra("store_id", selectedStore.getId());
                intentToCart.putExtra("store_name", selectedStore.getName());
                intentToCart.putExtra("token", userToken);
                intentToCart.putExtra("activeUserName", activeUserName);
                intentToCart.putExtra("total_price", selectedStore.getTotalPrice());
                intentToCart.putExtra("store_location", selectedStore.getNameLocation());
                startActivity(intentToCart);
            } else {
                Toast.makeText(CheckOutActivity.this, "Please select a store to continue.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up filter by location button
        this.btnFilterByLocation = binding.btnFilterByLocation;
        this.btnFilterByLocation.setOnClickListener(view -> {
            if (isFilteredByLocation) {
                // Reset to original list sorted by price
                storeAdapter.setStores(originalList);
                originalList.sort((s1, s2) -> Double.compare(s1.getTotalPrice(), s2.getTotalPrice()));
                storeAdapter.notifyDataSetChanged();
                isFilteredByLocation = false;
            } else {
                // Apply location-based filtering
                double userLatitude = Double.parseDouble(sharedPreferences.getString("user_latitude", "0.0"));
                double userLongitude = Double.parseDouble(sharedPreferences.getString("user_longitude", "0.0"));

                if (userLatitude == 0.0 && userLongitude == 0.0) {
                    Toast.makeText(this, "No location found. Enable location services.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Filter stores within a certain distance
                List<Store> filteredStores = filterStoresByLocation(userLatitude, userLongitude);

                if (!filteredStores.isEmpty()) {
                    // Sort by proximity
                    filteredStores.sort((s1, s2) -> {
                        double distance1 = calculateDistance(userLatitude, userLongitude, s1.getLatitude(), s1.getLongitude());
                        double distance2 = calculateDistance(userLatitude, userLongitude, s2.getLatitude(), s2.getLongitude());
                        return Double.compare(distance1, distance2);
                    });

                    storeAdapter.setStores(filteredStores);
                    storeAdapter.notifyDataSetChanged();
                    isFilteredByLocation = true; // Enable location filter
                } else {
                    Toast.makeText(this, "No nearby stores found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton returnHomeButton = binding.btnReturnHome;
        returnHomeButton.setOnClickListener(v -> finish());
    }

    /**
     * Filters stores within a certain distance from the user's location.
     *
     * @param userLatitude  User's latitude
     * @param userLongitude User's longitude
     * @return Filtered list of stores
     */
    private List<Store> filterStoresByLocation(double userLatitude, double userLongitude) {
        double MAX_DISTANCE_KM = 10.0; // Maximum distance in kilometers
        List<Store> filteredStores = new ArrayList<>();

        for (Store store : listStores) {
            double distance = calculateDistance(userLatitude, userLongitude, store.getLatitude(), store.getLongitude());
            if (distance <= MAX_DISTANCE_KM) {
                filteredStores.add(store);
            }
        }
        return filteredStores;
    }

    /**
     * Calculates the distance between two geographic points using the Haversine formula.
     *
     * @param lat1 Latitude of the first point
     * @param lon1 Longitude of the first point
     * @param lat2 Latitude of the second point
     * @param lon2 Longitude of the second point
     * @return Distance in kilometers
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_KM = 6371.0; // Earth's radius in kilometers

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Updates total price for each store and sorts the list by price.
     */
    private void calculateTotalForSelectedStore() {
        List<Item> selectedStoreItems = new ArrayList<>(CartManager.getInstance().getCartProducts());
        viewModel.fetchTotalPriceByStore(userToken, listStores, selectedStoreItems);

        for (Store store : listStores) {
            viewModel.getTotalPriceLiveDataForStore(store).observe(this, totalPrice -> {
                if (totalPrice != null) {
                    store.setTotalPrice(totalPrice);
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

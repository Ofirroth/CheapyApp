package com.example.cheapy.viewModels;
import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.API.UserAPI;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.User;
import com.example.cheapy.repositories.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    private final CartRepository cartRepository;
    private final LiveData<Cart> cartLiveData;

    public CartViewModel(String token) {
        cartRepository = new CartRepository(token);
        cartLiveData = new MutableLiveData<>();
    }

    // Method to create and save a new cart
    public void createCart(String token, String activeUserName, String storeId, List<Item> items, double totalPrice, String dateCreated) {
        UserAPI userAPI = new UserAPI();
        MutableLiveData<String> userIdLiveData = new MutableLiveData<>();
        Log.d("boo1","5");
        Log.d("boo1",activeUserName);
        // Fetch user ID and observe changes
        userAPI.getUserId(token, activeUserName, userIdLiveData);
        Log.d("boo1","6");
        userIdLiveData.observeForever(userId -> {
            if (userId != null) {
                Log.d("boo1",userId);
                Cart cart = new Cart(userId, storeId, totalPrice, items, dateCreated);
                cartRepository.saveCart(cart, token);
            } else {
                Log.e("CartViewModel", "Failed to fetch user ID");
            }
        });
        Log.d("boo1","7");
    }

    // Get the LiveData object that observes the cart
    public LiveData<Cart> getCartLiveData() {
        return cartLiveData;
    }
}

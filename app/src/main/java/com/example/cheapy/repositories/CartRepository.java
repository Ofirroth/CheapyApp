package com.example.cheapy.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.CartAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CartDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.shoppingListHistoryItem;

import java.util.List;

public class CartRepository {

    private CartDao cartDao;
    private CartAPI cartAPI;
    private AppDB db;
    private String token;

    public CartRepository(String token) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.cartDao = db.cartDao();
        this.cartAPI = new CartAPI();
    }

    // Get all items in the cart from the database or API
    public MutableLiveData<List<Item>> getCartItems(String cartId) {
        MutableLiveData<List<Item>> cartUserList = new MutableLiveData<>();
        cartAPI.getCartItems(cartUserList, token, cartId);
        return cartUserList;
    }

    // Save the cart to the server
    public void saveCart(Cart cart, String token) {
        cartAPI.createCart(token, cart);
    }

    public MutableLiveData<List<shoppingListHistoryItem>> getUserCart(String userName) {
        MutableLiveData<List<shoppingListHistoryItem>> cartUserList = new MutableLiveData<>();
        cartAPI.getUserCarts(cartUserList, token, userName);
        return cartUserList;
    }
    public MutableLiveData<List<Item>> getSpecificCart(String cartId) {
        Log.d("msgrepo","4");
        MutableLiveData<List<Item>> cartList = new MutableLiveData<>();
        Log.d("msgrepo","5");
        cartAPI.getCartItems(cartList, token, cartId);
        Log.d("msgrepo","6");
        return cartList;
    }
    // Reload the cart from the API and update the local database
    private void reload() {
        //cartAPI.getCartItems(new CartItemListData(), token);
        // Optionally, you could clear the local cart before reloading
        //cartDao.deleteAll();
    }

    // MutableLiveData that holds the cart items and fetches them from the server when active
    class CartItemListData extends MutableLiveData<List<Item>> {
        public CartItemListData() {
            super();
            //setValue(cartDao.getCartItems()); // Initialize with local data
        }

        @Override
        protected void onActive() {
            super.onActive();
//            new Thread(() -> {
//                cartAPI.getCartItems(this, token); // Fetch cart items from the API
//            }).start();
        }
    }
}

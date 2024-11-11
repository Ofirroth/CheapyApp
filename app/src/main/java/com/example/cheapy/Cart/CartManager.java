package com.example.cheapy.Cart;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Item> cartItems;
    private MutableLiveData<List<Item>> cartLiveData = new MutableLiveData<>();

    private CartManager() {
        cartItems = new ArrayList<>();
        cartLiveData.setValue(cartItems);
    }


    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public LiveData<List<Item>> getCartLiveData() {
        return cartLiveData;
    }


    public void addProduct(Item item) {
        boolean productExists = false;
        for (Item i : cartItems) {
            Log.d("CartManager: " , String.valueOf(i.getId()));
            Log.d("CartManager: " , String.valueOf(item.getId()));
            if ((i.getId().equals(item.getId()))){
                i.setQuantity(i.getQuantity() + 1);
                Log.d("CartManager: " , String.valueOf(i.getQuantity()));
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            item.setQuantity(1);
            cartItems.add(item);
        }
        Log.d("CartManager", "Cart updated: " + cartItems.toString());
        cartLiveData.setValue(cartItems);
    }

    public void decreaseProductQuantity(Item item) {
        for (Item i : cartItems) {
            if ((i.getId().equals(item.getId()))) {
                int newQuantity = i.getQuantity() - 1;
                if (newQuantity > 0) {
                    i.setQuantity(newQuantity);
                } else {
                    i.setQuantity(0);
                    cartItems.remove(i);
                }
                break;
            }
        }
        cartLiveData.setValue(cartItems);
    }


    public List<Item> getCartProducts() {
        return cartItems;
    }

    public Item getProduct (String id) {
        for (Item item : cartItems){
            if (item.getId().equals(id)){
                return item;
            }
        }

        return null;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Item item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public int getTotalItemCount() {
        int total = 0;
        for (Item item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }
}

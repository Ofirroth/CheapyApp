package com.example.cheapy.Cart;

import com.example.cheapy.entities.Item;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Item> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProduct(Item item) {
        boolean productExists = false;
        for (Item p : cartItems) {
            if (p.getName().equals(item.getName())) {
                p.setQuantity(p.getQuantity() + 1);
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            item.setQuantity(1);
            cartItems.add(item);
        }
    }

    public void removeProduct(Item item) {
        cartItems.remove(item);
    }

    public List<Item> getCartProducts() {
        return cartItems;
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

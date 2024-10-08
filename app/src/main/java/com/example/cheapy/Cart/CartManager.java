package com.example.cheapy.Cart;

import com.example.cheapy.entities.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<Product> cartProducts;

    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProduct(Product product) {
        boolean productExists = false;
        for (Product p : cartProducts) {
            if (p.getName().equals(product.getName())) {
                p.setQuantity(p.getQuantity() + 1);
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            product.setQuantity(1);
            cartProducts.add(product);
        }
    }

    public void removeProduct(Product product) {
        cartProducts.remove(product);
    }

    public List<Product> getCartProducts() {
        return cartProducts;
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Product product : cartProducts) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    public int getTotalItemCount() {
        int total = 0;
        for (Product product : cartProducts) {
            total += product.getQuantity();
        }
        return total;
    }
}

package com.example.cheapy.API;

import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.shoppingListHistoryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CartServiceAPI {

    @POST("cart/")
    Call<Cart> createCart(@Header("Authorization") String token, @Body Cart cart);

    @GET("cart/{username}")
    Call<List<shoppingListHistoryItem>> getCarts(@Header("Authorization") String token, @Path("username") String userName);
    //@GET("cart/getSpec/{cartId}")
    //Call<Cart> getSpecificCart(@Header("Authorization") String token, @Path("cartId") String cartId);

    @GET("cart/getSpec/{cartId}")
    Call<List<Item>> getItemsCart(@Header("Authorization") String token, @Path("cartId") String cartId);
}

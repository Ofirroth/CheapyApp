package com.example.cheapy.API;

import com.example.cheapy.entities.Cart;

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

    @GET("cart/{id}")
    Call<Cart> getCart(@Header("Authorization") String token, @Path("id") String cartId);
}

package com.example.cheapy.API;

import com.example.cheapy.entities.Price;
import com.example.cheapy.entities.StoreTotalRequest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PriceServiceApi {
    @GET("Price/getItemPriceByStore/{itemId}/{storeName}")
    Call<Double> getItemPriceByStore(
            @Header("Authorization") String token,
            @Path("itemId") String itemId,
            @Path("storeName") String storeName
    );
    @POST("Price/getTotalPriceByStore")
    Call<Double> getTotalPriceByStore(
            @Header("Authorization") String token,
            @Body StoreTotalRequest request
    );
}

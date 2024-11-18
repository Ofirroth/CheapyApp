package com.example.cheapy.API;

import com.example.cheapy.entities.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.StoreTotalRequest;

public interface StoreServiceAPI {
    @GET("Store/")
    Call<List<Store>> getAllStores(@Header("Authorization")String token);
    @POST("Store/getTotalPriceByStore")
    Call<Double> getTotalPriceByStore(@Header("Authorization") String token, @Body StoreTotalRequest request);

    @GET("Store/getName/{id}")
    Call<Store> getStore(@Header("Authorization")String token,@Path("id") String name);

    @GET("Store/{city}")
    Call<List<Store>> getStoresByCity(@Header("Authorization")String token,@Path("city") String city);
}

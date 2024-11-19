package com.example.cheapy.API;

import com.example.cheapy.entities.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface recommendationServiceApi {
    @GET("recommended/{username}")
    Call<List<Item>> getRecommended(@Header("Authorization")String token,@Path("username") String username);
}

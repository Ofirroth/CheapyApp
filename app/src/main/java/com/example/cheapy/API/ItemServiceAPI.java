package com.example.cheapy.API;

import com.example.cheapy.entities.Item;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemServiceAPI {
    @GET("Chats/")
    Call<List<Item>> getItems(@Header("Authorization")String token);

    @GET("Chats/{name}")
    Call<Item> getItem(@Header("Authorization")String token,@Path("name") String name);

    @GET("Chats/{category}")
    Call<List<Item>> getItemByCategory(@Header("Authorization")String token,@Path("category") String category);

}

package com.example.cheapy.API;

import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.SubCategory;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CategoryServiceAPI {

    @GET("Category/")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    @GET("Category/{id}")
    Call<Category> getCategory(@Header("Authorization") String token, @Path("id") int id);

    @GET("Category/{category}")
    Call<List<SubCategory>> getSubCategories(@Header("Authorization") String token, @Path("category") int category);

}

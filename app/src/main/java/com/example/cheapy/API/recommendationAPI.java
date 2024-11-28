package com.example.cheapy.API;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// API class for handling recommendations using Retrofit
public class recommendationAPI {
    Retrofit retrofit;
    recommendationServiceApi recommendationServiceapi;

    // Constructor to initialize Retrofit and API interface
    public recommendationAPI() {
        /////////////
        // Logging interceptor for debugging API calls
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // OkHttpClient with interceptor
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        /////////////////
        // Base URL for the API
        String apiAddress = Cheapy.urlServer.getValue();
        // Initializing Retrofit instance
        retrofit = new Retrofit.Builder()
                .baseUrl(apiAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        // Creating API service interface
        recommendationServiceapi = retrofit.create(recommendationServiceApi.class);
    }

    // Fetch recommended items for a user
    public void getRecommendedItems(MutableLiveData<List<Item>> itemsListData, String token, String username) {
        // API call to get recommended items
        Call<List<Item>> call = recommendationServiceapi.getRecommended(token, username);
        // Enqueueing the call asynchronously
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    // If the response is successful, update the LiveData
                    itemsListData.setValue(response.body());
                } else {
                    // Show error message if the response is not successful
                    Toast.makeText(Cheapy.context, "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                // Handle call failure and show error message
                String err = t.getMessage();
                if (err != null) {
                    Toast.makeText(Cheapy.context, "Error:" + err, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cheapy.context, "Error:", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

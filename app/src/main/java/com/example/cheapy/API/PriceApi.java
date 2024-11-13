package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Price;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.StoreTotalRequest;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PriceApi {
    Retrofit retrofit;
    PriceServiceApi priceServiceApi;

    private MutableLiveData<List<Price>> responseAnswer;

    public PriceApi() {
        /////////////
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        /////////////////
        String apiAddress = Cheapy.urlServer.getValue();

        retrofit = new Retrofit.Builder()
                .baseUrl(apiAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        priceServiceApi = retrofit.create(PriceServiceApi.class);
        responseAnswer = new MutableLiveData<>();
    }

    public double getItemPriceByStore(String token, String itemId, String storeName) {
        Call<Double> call = priceServiceApi.getItemPriceByStore(token, itemId, storeName);

        try {
            Response<Double> response = call.execute(); // Synchronous call
            if (response.isSuccessful() && response.body() != null) {
                return response.body(); // Return the price as a double
            } else {
                // Handle the error here (e.g., logging or notifying the user)
                Toast.makeText(Cheapy.context, "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                return 0.0; // Default value if the response isn't successful
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Cheapy.context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return 0.0; // Default value if there's an exception
        }
    }

    public double getTotalPriceByStore(String token, String storeName, List<Item> items) {
        StoreTotalRequest request = new StoreTotalRequest(storeName, items);
        Call<Double> call = priceServiceApi.getTotalPriceByStore(token, request);

        try {
            Response<Double> response = call.execute(); // Synchronous call
            if (response.isSuccessful() && response.body() != null) {
                return response.body(); // Return the total price as a double
            } else {
                return 0.0; // Return 0.0 if there's an error or no response
            }
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0; // Return 0.0 if there's an exception
        }
    }
}

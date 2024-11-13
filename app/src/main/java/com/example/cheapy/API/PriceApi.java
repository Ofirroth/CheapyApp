package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Price;
import com.example.cheapy.entities.StoreTotalRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PriceApi {
    private final PriceServiceApi priceServiceApi;

    public PriceApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        String apiAddress = Cheapy.urlServer.getValue();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiAddress + "/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        priceServiceApi = retrofit.create(PriceServiceApi.class);
    }

    public void getItemPriceByStore(String token, String itemId, String storeName, MutableLiveData<Double> priceLiveData) {
        Call<Double> call = priceServiceApi.getItemPriceByStore(token, itemId, storeName);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    priceLiveData.setValue(response.body()); // Update LiveData with the price
                } else {
                    Toast.makeText(Cheapy.context, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    priceLiveData.setValue(0.0); // Default value in case of error
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Toast.makeText(Cheapy.context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                priceLiveData.setValue(0.0); // Default value in case of failure
            }
        });
    }

    public void getTotalPriceByStore(String token, String storeName, List<Item> items, MutableLiveData<Double> totalPriceLiveData) {
        StoreTotalRequest request = new StoreTotalRequest(storeName, items);
        Call<Double> call = priceServiceApi.getTotalPriceByStore(token, request);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalPriceLiveData.postValue(response.body());  // Update LiveData with response
                } else {
                    totalPriceLiveData.postValue(0.0);  // Default if the response is not successful
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                totalPriceLiveData.postValue(0.0);  // Set a default value on failure
            }
        });
    }

}

package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;
import com.example.cheapy.entities.StoreTotalRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoreAPI {
    Retrofit retrofit;
    StoreServiceAPI storeServiceAPI;

    private MutableLiveData<List<Store>> responseAnswer;

    public StoreAPI() {
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

        storeServiceAPI = retrofit.create(StoreServiceAPI.class);
        responseAnswer = new MutableLiveData<>();
    }

    public void getAllStores(MutableLiveData<List<Store>> storesListData, String token) {
        // Make API call for stores
        Call<List<Store>> call = storeServiceAPI.getAllStores(token);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    // Log the details for the first store
                    if (response.body() != null && !response.body().isEmpty()) {
                        storesListData.setValue(response.body());
                    }
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                String err = t.getMessage();
                if (err != null) {
                    Toast.makeText(Cheapy.context,
                            "Error:" + err, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error:", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getTotalPriceByStore(String token, String storeName, List<Item> items, MutableLiveData<Double> totalPriceLiveData) {
        StoreTotalRequest request = new StoreTotalRequest(storeName, items);
        Call<Double> call = storeServiceAPI.getTotalPriceByStore(token, request);

        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalPriceLiveData.setValue(response.body());
                } else {
                    totalPriceLiveData.setValue(0.0); // default if no response
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                totalPriceLiveData.setValue(0.0); // default on failure
            }
        });
    }
    public void getStoresByAddress(MutableLiveData<List<Store>> storesListData, String token, String address) {
        // API call to fetch stores by address
        Call<List<Store>> call = storeServiceAPI.getStoresByAdress(token, address);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    storesListData.setValue(response.body());
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                String err = t.getMessage();
                if (err != null) {
                    Toast.makeText(Cheapy.context,
                            "Error:" + err, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error:", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

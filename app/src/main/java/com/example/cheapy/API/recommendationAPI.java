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

public class recommendationAPI {
    Retrofit retrofit;
    recommendationServiceApi recommendationServiceapi;

    private MutableLiveData responeAnswer;

    public recommendationAPI() {
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

        recommendationServiceapi = retrofit.create(recommendationServiceApi.class);
        responeAnswer = new MutableLiveData<>();
    }

    public void getRecommendedItems(MutableLiveData<List<Item>> itemsListData, String token, String username) {
        Log.d("boo","8");
        Call<List<Item>> call = recommendationServiceapi.getRecommended(token, username);
        Log.d("boo","9");
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    Log.d("boo","10");
                    itemsListData.setValue(response.body());
                } else {
                    Log.d("boo","11");
                    Toast.makeText(Cheapy.context, "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                String err = t.getMessage();
                Log.d("boo","12");
                Log.d("boo",err);
                if (err != null) {
                    Toast.makeText(Cheapy.context, "Error:" + err, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cheapy.context, "Error:", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

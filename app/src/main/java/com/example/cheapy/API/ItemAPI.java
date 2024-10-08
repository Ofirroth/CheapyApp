package com.example.cheapy.API;
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
import retrofit2.converter.gson.GsonConverterFactory;
public class ItemAPI {
    Retrofit retrofit;
    ItemServiceAPI itemServiceAPI;

    private MutableLiveData responeAnswer;

    public ItemAPI() {
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

        itemServiceAPI = retrofit.create(ItemServiceAPI.class);
        responeAnswer = new MutableLiveData<>();
    }

    public void getItemsByCategory(MutableLiveData<List<Item>> itemsListData, String token, String category) {
        Call<List<Item>> call = itemServiceAPI.getItemByCategory(token, category);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    itemsListData.setValue(response.body());
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error:" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
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

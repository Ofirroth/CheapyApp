package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Category;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryAPI {
    private final CategoryServiceAPI categoryServiceAPI;

    public CategoryAPI() {
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

        categoryServiceAPI = retrofit.create(CategoryServiceAPI.class);
    }

    public void getCategories(MutableLiveData<List<Category>> categoriesListData, String token) {
        Call<List<Category>> call = categoryServiceAPI.getCategories(token);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoriesListData.setValue(response.body());
                } else {
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                String err = t.getMessage();
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;
import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.SubCategory;

import java.util.ArrayList;
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
        Log.d("sadasr","1");
        Call<List<Category>> call = categoryServiceAPI.getCategories(token);
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoriesListData.setValue(response.body());
                } else {
                    Log.d("st", response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                String err = t.getMessage();
                Log.d("st2", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchSubcategoriesFromCategory(String token, int categoryId, MutableLiveData<List<SubCategory>> result) {
        Call<List<SubCategory>> call = categoryServiceAPI.getSubCategories(token, categoryId);
        call.enqueue(new Callback<List<SubCategory>>() {
            @Override
            public void onResponse(Call<List<SubCategory>> call, Response<List<SubCategory>> response) {
                // Check if the response is successful and not empty
                if (response.isSuccessful() && response.body() != null) {
                    // Update the LiveData with the subcategory names
                    result.setValue(response.body());
                } else {
                    // Handle case where response is unsuccessful or body is null
                    Log.e("CategoryAPI", "Error fetching subcategories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SubCategory>> call, Throwable t) {
                // Handle failure to connect to the server
                Log.e("CategoryAPI", "Error fetching subcategories: " + t.getMessage());
            }
        });
    }
}

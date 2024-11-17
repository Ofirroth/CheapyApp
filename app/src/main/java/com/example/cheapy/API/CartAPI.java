package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Cart;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAPI {
    private final CartServiceAPI cartServiceAPI;

    public CartAPI() {
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

        cartServiceAPI = retrofit.create(CartServiceAPI.class);
    }

    public void createCart(String token, Cart cart) {
        Call<Cart> call = cartServiceAPI.createCart(token, cart);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    return;
                } else {
                    Log.d("CartAPI", response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                String err = t.getMessage();
                Log.d("CartAPI", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchCart(String token, String cartId, MutableLiveData<Cart> cartData) {
        Call<Cart> call = cartServiceAPI.getCart(token, cartId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    cartData.setValue(response.body());
                } else {
                    Log.d("CartAPI", response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                String err = t.getMessage();
                Log.d("CartAPI", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

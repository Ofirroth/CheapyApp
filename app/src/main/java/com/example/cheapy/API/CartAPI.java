package com.example.cheapy.API;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.Cheapy;
import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.shoppingListHistoryItem;

import java.util.List;

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
        Log.d("msg3", cart.getItems().get(0).getName());
        Call<Cart> call = cartServiceAPI.createCart(token, cart);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful()) {
                    Log.d("msg3", "1");
                } else {
                    Log.d("msg3", response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                String err = t.getMessage();
                Log.d("msg3", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getUserCarts(MutableLiveData<List<shoppingListHistoryItem>> cartData, String token, String userName) {
        Call<List<shoppingListHistoryItem>> call = cartServiceAPI.getCarts(token, userName);
        call.enqueue(new Callback<List<shoppingListHistoryItem>>() {
            @Override
            public void onResponse(Call<List<shoppingListHistoryItem>> call, Response<List<shoppingListHistoryItem>> response) {
                if (response.isSuccessful()) {
                    cartData.setValue(response.body());
                } else {
                    Log.d("msg3", response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<shoppingListHistoryItem>> call, Throwable t) {
                String err = t.getMessage();
                Log.d("msg", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCartItems(MutableLiveData<List<Item>> cartData, String token, String cartId) {
        Call<List<Item>> call = cartServiceAPI.getItemsCart(token, cartId);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                if (response.isSuccessful()) {
                    Log.d("msga","7");
                    Log.d("msga",response.body().get(0).getName());
                    cartData.postValue(response.body());
                } else {
                    Log.d("msgapi",response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                String err = t.getMessage();
                Log.d("msgapi", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*public void getSpecificCart(MutableLiveData<Cart> cartData, String token, String cartId) {
        Log.d("msgapi","7");
        Call<Cart> call = cartServiceAPI.getSpecificCart(token, cartId);
        Log.d("msgapi","8");
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                Log.d("msgapi","1000");
                if (response.isSuccessful()) {
                    Log.d("msgapi","9");
                    Cart c = response.body();
                    Log.d("boo",c.toString());
                    cartData.setValue(c);
                } else {
                    Log.d("msgapi","10");
                    Log.d("msgapi",response.message());
                    Toast.makeText(Cheapy.context,
                            "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                String err = t.getMessage();
                Log.d("msgapi","11");
                Log.d("msgapi", err);
                Toast.makeText(Cheapy.context,
                        "Error: " + (err != null ? err : ""), Toast.LENGTH_SHORT).show();
            }
        });
    }*/


}

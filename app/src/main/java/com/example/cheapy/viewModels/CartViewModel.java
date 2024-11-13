package com.example.cheapy.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.cheapy.entities.Item;
import com.example.cheapy.repositories.StoreRepository;

import java.util.List;

public class CartViewModel extends ViewModel {

    private String userToken;

    private StoreRepository storeRepository;
    private MutableLiveData<Double> totalPriceLiveData = new MutableLiveData<>();

    public CartViewModel(String token) {
        this.userToken = token;
        storeRepository = new StoreRepository(userToken);
    }

    public LiveData<Double> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public void fetchTotalPriceByStore(String token, String storeName, List<Item> items) {
        storeRepository.getTotalPriceByStore(token, storeName, items, totalPriceLiveData);
    }
}

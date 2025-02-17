package com.example.cheapy.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.CategoryDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;
import com.example.cheapy.repositories.StoreRepository;

import java.util.List;

public class CheckOutViewModel extends ViewModel {

    private StoreRepository storeRepository;
    private LiveData<List<Store>> storesLiveData;
    private MutableLiveData<List<Store>> storesCityLiveData;

    private MutableLiveData<Double> totalPriceLiveData = new MutableLiveData<>();
    private MutableLiveData<Double> itemPriceData = new MutableLiveData<>();


    public CheckOutViewModel(String token) {
        storeRepository = new StoreRepository(token);
        storesLiveData = storeRepository.getAllStores();
    }

    public LiveData<List<Store>> getStores() {
        reload();
        return storesLiveData;
    }
    public MutableLiveData<List<Store>> getStoresCity(String city) {
        this.storesCityLiveData = storeRepository.getCityStore(city);
        return storesCityLiveData;
    }
    public void reload() {
        this.storesLiveData = storeRepository.getAllStores();
    }

    public LiveData<Double> getTotalPriceLiveData() {
        return totalPriceLiveData;
    }

    public LiveData<Double> getTotalPriceLiveDataForStore(Store store) {
        return storeRepository.getTotalPriceLiveDataForStore(store);
    }

    public void fetchTotalPriceByStore(String token,List<Store> stores, List<Item> items) {
           storeRepository.getTotalPriceByStore(token, stores, items);
    }

    public LiveData<Double> getItemPriceLiveDataForStore(Item item) {
        return storeRepository.getItemPriceLiveDataForStore(item);
    }

    public void fetchItemPriceByStore(String token,String storeId, String itemId) {
        storeRepository.getItemPriceByStore(token, storeId, itemId);
    }

}

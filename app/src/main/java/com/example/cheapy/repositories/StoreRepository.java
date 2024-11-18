package com.example.cheapy.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.CategoryAPI;
import com.example.cheapy.API.ItemAPI;
import com.example.cheapy.API.PriceApi;
import com.example.cheapy.API.StoreAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.StoreDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreRepository {

    private StoreDao storeDao;

    private StoreListData storeListData;
    private StoreListData cityStoresListData;
    private AppDB db;
    private String token;

    private StoreAPI storeAPI;

    private PriceApi priceApi;

    MutableLiveData<Double> priceLiveData;
    MutableLiveData<Double> totalPriceLiveData;

    private Map<Store, MutableLiveData<Double>> totalPriceLiveDataMap;
    private Map<String, MutableLiveData<Double>> ItemsStorePriceLiveDataMap;
    MutableLiveData<Double> storeItemPriceLiveData = new MutableLiveData<>();



    public StoreRepository(String token) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.storeDao = db.storeDao();
        this.storeListData = new StoreListData();
        this.storeAPI = new StoreAPI();
        this.priceApi = new PriceApi();
        this.priceLiveData = new MutableLiveData<>();
        this.totalPriceLiveData = new MutableLiveData<>();
        this.totalPriceLiveDataMap = new HashMap<>();
        this.ItemsStorePriceLiveDataMap = new HashMap<>();
    }

    public LiveData<List<Store>> getAllStores() {
        reload();
        return storeListData;
    }

    public MutableLiveData<List<Store>> getCityStore(String city) {
        MutableLiveData<List<Store>> storesCity = new MutableLiveData<>();
        storeAPI.getStoresByCity(storesCity, token, city);
        return storesCity;
    }

    public void reload() {
        StoreAPI storeAPI = new StoreAPI();
        storeAPI.getAllStores(storeListData, token);
        storeDao.delete();
        List<Store> storeList = storeListData.getValue();
        storeListData.postValue(storeList);
    }

    public void getTotalPriceByStore(String token, List<Store> stores, List<Item> items) {
        for (Store store : stores) {
            MutableLiveData<Double> storeTotalPriceLiveData = new MutableLiveData<>();
            totalPriceLiveDataMap.put(store, storeTotalPriceLiveData);
            String storeName = store.getName();
            priceApi.getTotalPriceByStore(token, storeName, items, storeTotalPriceLiveData);
        }
    }

    public MutableLiveData<Double> getTotalPriceLiveDataForStore(Store store) {
        return totalPriceLiveDataMap.getOrDefault(store, new MutableLiveData<>(0.0));
    }

    public void getItemPriceByStore(String token, String storeId, String itemId) {
        MutableLiveData<Double> storeItemPriceLiveData = new MutableLiveData<>();
        ItemsStorePriceLiveDataMap.put(itemId, storeItemPriceLiveData);
        priceApi.getItemPriceByStore(token, itemId, storeId, storeItemPriceLiveData);
    }

    public MutableLiveData<Double> getItemPriceLiveDataForStore(Item item) {
        return ItemsStorePriceLiveDataMap.getOrDefault(item.getId(), new MutableLiveData<>(0.0));
    }


    class StoreListData extends MutableLiveData<List<Store>> {
        public StoreListData() {
            super();
            setValue(storeDao.getAllStores());

        }

        @Override
        protected void onActive() {
            super.onActive();
            new Thread(() -> {
                storeAPI.getAllStores(this,token);
            }).start();
        }
    }
}

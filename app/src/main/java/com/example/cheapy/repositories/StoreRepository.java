package com.example.cheapy.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.CategoryAPI;
import com.example.cheapy.API.ItemAPI;
import com.example.cheapy.API.StoreAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.StoreDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Category;
import com.example.cheapy.entities.Item;
import com.example.cheapy.entities.Store;

import java.util.List;

public class StoreRepository {

    private StoreDao storeDao;

    private StoreListData storeListData;
    private AppDB db;
    private String token;

    private StoreAPI storeAPI;


    public StoreRepository(String token) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.storeDao = db.storeDao();
        this.storeListData = new StoreListData();
        this.storeAPI = new StoreAPI();
    }

    public LiveData<List<Store>> getAllStores() {
        reload();
        return storeListData;
    }

    public void reload() {
        StoreAPI storeAPI = new StoreAPI();
        storeAPI.getAllStores(storeListData, token);
        storeDao.delete();
        List<Store> storeList = storeListData.getValue();
        storeListData.postValue(storeList);
    }

    public void getTotalPriceByStore(String token, String storeName, List<Item> items, MutableLiveData<Double> totalPriceLiveData) {
        storeAPI.getTotalPriceByStore(token, storeName, items, totalPriceLiveData);
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

//    public void insert(Store store) {
//        db.databaseWriteExecutor.execute(() -> storeDao.insert(store));
//    }
//
//    public void update(Store store) {
//        db.databaseWriteExecutor.execute(() -> storeDao.update(store));
//    }
//
//    public void delete(Store store) {
//        db.databaseWriteExecutor.execute(() -> storeDao.delete(store));
//    }
}

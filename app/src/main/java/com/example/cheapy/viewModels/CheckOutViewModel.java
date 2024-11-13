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


    public CheckOutViewModel(String token) {
        storeRepository = new StoreRepository(token);
        storesLiveData = storeRepository.getAllStores();
        Log.d("storesLiveData:", storesLiveData.toString());
    }

    public LiveData<List<Store>> getStores() {
        reload();
        return storesLiveData;
    }
    public void reload() {
        this.storesLiveData = storeRepository.getAllStores();
    }
}

package com.example.cheapy.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cheapy.API.ItemAPI;
import com.example.cheapy.API.recommendationAPI;
import com.example.cheapy.Dao.AppDB;
import com.example.cheapy.Dao.ItemDao;
import com.example.cheapy.DatabaseManager;
import com.example.cheapy.entities.Item;

import java.util.List;

public class ItemRepository {

    private ItemDao itemDao;
    private ItemListData itemListData;
    private ItemAPI itemAPI;
    private String token;
    private AppDB db;

    public ItemRepository(String token) {
        this.token = token;
        this.db = DatabaseManager.getDatabase();
        this.itemDao = db.itemDao();
        this.itemListData= new ItemListData();
        this.itemAPI = new ItemAPI();
    }

    /*public LiveData<List<Item>> getItems() {
        reload();
        return itemListData;
    }*/
    public LiveData<List<Item>> getRecoItems(String userId) {
        reload(userId);
        return itemListData;
    }
    public MutableLiveData<List<Item>> getItemsByCategory(int categoryId) {
        MutableLiveData<List<Item>> categoryItemList = new MutableLiveData<>();
        itemAPI.getItemsByCategory(categoryItemList, token, categoryId);
        return categoryItemList;
    }

    public void reload(String username) {
        //ItemAPI itemAPI = new ItemAPI();
        //itemAPI.getItems(itemListData, token);
        recommendationAPI reco = new recommendationAPI();
        reco.getRecommendedItems(itemListData,token,username);
        Log.d("boo", username);
        itemDao.delete();
        List<Item> itemsList = itemListData.getValue();
        itemListData.postValue(itemsList);
    }


    class ItemListData extends MutableLiveData<List<Item>> {
        public ItemListData() {
            super();
        }
    }

}

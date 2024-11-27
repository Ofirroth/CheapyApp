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

// Repository class to handle data operations for items
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
    // Fetch recommended items for a user, observing the data via LiveData
    public LiveData<List<Item>> getRecoItems(String userId) {
        reload(userId);
        return itemListData;
    }

    // Fetch items by category, updating a MutableLiveData instance
    public MutableLiveData<List<Item>> getItemsByCategory(int categoryId) {
        MutableLiveData<List<Item>> categoryItemList = new MutableLiveData<>();
        itemAPI.getItemsByCategory(categoryItemList, token, categoryId);
        return categoryItemList;
    }

    // Reload recommended items for the specified user
    public void reload(String username) {
        recommendationAPI reco = new recommendationAPI();
        reco.getRecommendedItems(itemListData,token,username);
        itemDao.delete(); // Clear existing items in the local database
        List<Item> itemsList = itemListData.getValue(); // Get current items from LiveData
        itemListData.postValue(itemsList); // Update LiveData with new items
    }

    // Custom LiveData class for handling item lists
    class ItemListData extends MutableLiveData<List<Item>> {
        public ItemListData() {
            super();
        }
    }

}

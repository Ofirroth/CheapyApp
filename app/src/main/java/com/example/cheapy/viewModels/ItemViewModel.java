package com.example.cheapy.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.entities.Item;
import com.example.cheapy.repositories.ItemRepository;

import java.util.List;

// ViewModel for managing UI-related data for items
public class ItemViewModel extends ViewModel {
    private ItemRepository itemsRepository;
    private LiveData<List<Item>> items;
    private LiveData<List<Item>> categoryItems;
    public ItemViewModel(String token) {
            this.itemsRepository = new ItemRepository(token);
        }

    // Fetch recommended items for a specific username
    public LiveData<List<Item>> getRecoItems(String username) {
        reload(username);  // Reload data for the given username
        return this.items; // Return the LiveData containing items
    }
    // Fetch items based on category ID
    public LiveData<List<Item>> getItemsByCategory(int categoryId) {
        // Load items by category from repository
        categoryItems = itemsRepository.getItemsByCategory(categoryId);
        return categoryItems;
    }
    // Reload recommended items for the specified username
    public void reload(String username) {
        // Refresh LiveData with new data from repository
        this.items = itemsRepository.getRecoItems(username);
    }
}

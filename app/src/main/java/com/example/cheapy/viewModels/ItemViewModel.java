package com.example.cheapy.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.entities.Item;
import com.example.cheapy.repositories.ItemRepository;

import java.util.List;

public class ItemViewModel extends ViewModel {
    private ItemRepository itemsRepository;
    private LiveData<List<Item>> items;
    private LiveData<List<Item>> categoryItems;



    public ItemViewModel(String token) {
            this.itemsRepository = new ItemRepository(token);
            this.items = itemsRepository.getItems();
        }

        public LiveData<List<Item>> getItems() {
            reload();
            return this.items;
        }

    public LiveData<List<Item>> getItemsByCategory(int categoryId) {
        categoryItems = itemsRepository.getItemsByCategory(categoryId);
        return categoryItems;
    }


        public void reload() {
            this.items = itemsRepository.getItems();
        }
}

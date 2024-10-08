package com.example.cheapy.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cheapy.entities.Item;
import com.example.cheapy.repositories.ItemRepository;

import java.util.List;

public class ItemViewModel extends ViewModel {
    private ItemRepository itemsRepository;
    private LiveData<List<Item>> items;


    public ItemViewModel(String token) {
            this.itemsRepository = new ItemRepository(token);
            this.items = itemsRepository.getItemsByCategory();
        }

        public LiveData<List<Item>> getItems() {
            reload();
            return this.items;
        }

        public void reload() {
            this.items = itemsRepository.getItemsByCategory();
        }
}

//package com.example.cheapy.repositories;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//
//import com.example.cheapy.API.ItemAPI;
//import com.example.cheapy.API.PriceApi;
//import com.example.cheapy.Dao.AppDB;
//import com.example.cheapy.Dao.ItemDao;
//import com.example.cheapy.Dao.PriceDao;
//import com.example.cheapy.Dao.StoreDao;
//import com.example.cheapy.DatabaseManager;
//import com.example.cheapy.entities.Item;
//import com.example.cheapy.entities.Store;
//
//import java.util.List;
//
//public class PriceRepository {
//
//        private PriceDao priceDao;
//        private PriceApi priceApi;
//        private String token;
//        private AppDB db;
//
//        public PriceRepository(String token) {
//            this.token = token;
//            this.db = DatabaseManager.getDatabase();
//            this.priceDao = db.priceDao();
//            this.priceApi = new PriceApi();
//        }
//
//    public void getTotalPriceByStore(String token, List<Item> items) {
//        List <Store> stores = storeDao.getAllStores();
//        assert stores != null;
//        for (Store store: stores) {
//            String storeName = store.getName();
//            double totalPrice = priceApi.getTotalPriceByStore(token, storeName, items);
//            store.setTotalPrice(totalPrice);
//        }
//    }
//
//
//
//    }

package com.example.cheapy.Dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.cheapy.entities.Price;

import java.util.List;

@Dao
public interface PriceDao {

    @Query("SELECT price FROM Price WHERE itemId = :itemId AND storeId = :storeId")
    Double getItemPriceByStore(String itemId, String storeId);

    @Query("SELECT SUM(price) FROM Price WHERE itemId IN (:itemIds) AND storeId = :storeId")
    Double getTotalPriceByStore(List<String> itemIds, String storeId);
}

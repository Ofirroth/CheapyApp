package com.example.cheapy.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheapy.entities.Cart;
import com.example.cheapy.entities.Item;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(Cart... carts);

    @Update
    void update(Cart... carts);

    @Delete
    void delete(Cart... carts);

    @Query("DELETE FROM cart")
    void deleteAll();

//    @Query("SELECT * FROM cart")
//    LiveData<List<Item>> getCartItems();
//
//    @Query("SELECT COUNT(*) FROM cart")
//    int getCartItemCount();
//
//    @Query("SELECT SUM(price * quantity) FROM cart")
//    double getTotalPrice();
}

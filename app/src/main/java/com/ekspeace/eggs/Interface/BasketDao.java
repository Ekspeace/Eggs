package com.ekspeace.eggs.Interface;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ekspeace.eggs.Model.Basket;

import java.util.List;

@Dao
public interface BasketDao {
    @Insert
    void Insert(Basket basket);

    @Update
    void Update(Basket basket);

    @Delete
    void Delete(Basket basket);

    @Query("SELECT * FROM basket_table ORDER BY Id DESC")
    LiveData<List<Basket>> getAllItems();
}

package com.ekspeace.eggs.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ekspeace.eggs.Interface.BasketDao;
import com.ekspeace.eggs.Model.Basket;

@Database(entities = {Basket.class}, version = 1, exportSchema = false)
public abstract class BasketDatabase extends RoomDatabase {
    private static BasketDatabase instance;

    public abstract BasketDao basketDao();

    public static synchronized BasketDatabase getInstance(Context context)
    {
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BasketDatabase.class, "basket_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

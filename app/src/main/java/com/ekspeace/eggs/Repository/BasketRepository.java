package com.ekspeace.eggs.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.ekspeace.eggs.Database.BasketDatabase;
import com.ekspeace.eggs.Interface.BasketDao;
import com.ekspeace.eggs.Model.Basket;

import java.util.List;

public class BasketRepository {
    private BasketDao basketDao;
    private LiveData<List<Basket>> allItems;

    public BasketRepository(Application application){
        BasketDatabase basketDatabase = BasketDatabase.getInstance(application);
        basketDao = basketDatabase.basketDao();
        allItems = basketDao.getAllItems();
    }

    public void insert(Basket basket) {
        new InsertBasketAsyncTask(basketDao).execute(basket);
    }

    public void update(Basket basket) {
        new UpdateBasketAsyncTask(basketDao).execute(basket);
    }

    public void delete(Basket basket) {
        new DeleteBasketAsyncTask(basketDao).execute(basket);
    }

    public LiveData<List<Basket>> getAllItems(){
        return allItems;
    }

    private static class InsertBasketAsyncTask extends AsyncTask<Basket, Void, Void> {
        private BasketDao basketDao;

        private InsertBasketAsyncTask(BasketDao basketDao) {
            this.basketDao = basketDao;
        }

        @Override
        protected Void doInBackground(Basket... baskets) {
            basketDao.Insert(baskets[0]);
            return null;
        }
    }

    private static class UpdateBasketAsyncTask extends AsyncTask<Basket, Void, Void> {
        private BasketDao basketDao;

        private UpdateBasketAsyncTask(BasketDao basketDao) {
            this.basketDao = basketDao;
        }

        @Override
        protected Void doInBackground(Basket... baskets) {
            basketDao.Update(baskets[0]);
            return null;
        }
    }
    private static class DeleteBasketAsyncTask extends AsyncTask<Basket, Void, Void> {
        private BasketDao basketDao;

        private DeleteBasketAsyncTask(BasketDao basketDao) {
            this.basketDao = basketDao;
        }

        @Override
        protected Void doInBackground(Basket... baskets) {
            basketDao.Delete(baskets[0]);
            return null;
        }
    }
}

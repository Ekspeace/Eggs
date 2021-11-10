package com.ekspeace.eggs.ViewModel;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ekspeace.eggs.Model.Basket;
import com.ekspeace.eggs.Repository.BasketRepository;

import java.util.List;

public class BasketViewModel extends AndroidViewModel {
    private BasketRepository repository;
    private LiveData<List<Basket>> allItems;
    public BasketViewModel(Application application) {
        super(application);
        repository = new BasketRepository(application);
        allItems = repository.getAllItems();
    }
    public void insert(Basket basket) {
        repository.insert(basket);
    }
    public void delete(Basket basket) {
        repository.delete(basket);
    }
    public void update(Basket basket) {
        repository.update(basket);
    }
    public LiveData<List<Basket>> getAllItems() {
        return allItems;
    }
}

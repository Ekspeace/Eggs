package com.ekspeace.eggs.Interface;



import com.ekspeace.eggs.Model.Egg;

import java.util.List;

public interface IEggLoadListener {
    void onEggLoadSuccess(List<Egg> eggs);
    void onEggLoadFailed(String message);
}

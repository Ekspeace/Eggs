package com.ekspeace.eggs.Interface;

import com.ekspeace.eggs.Model.Recipe;

import java.util.List;


public interface IRecipeLoadListener {
    void onRecipeLoadSuccess(List<Recipe> recipes);
    void onRecipeLoadFailed(String message);
}

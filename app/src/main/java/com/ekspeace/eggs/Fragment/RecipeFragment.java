package com.ekspeace.eggs.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Activity.RecipeDetails;
import com.ekspeace.eggs.Adapter.RecyclerViewRecipeAdapter;
import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Interface.IRecipeLoadListener;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.Recipe;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeFragment extends Fragment implements IRecipeLoadListener {
    private static RecipeFragment instance;
    private Recipe recipe;
    private RecyclerView recyclerView;
    private IRecipeLoadListener iRecipeLoadListener;
    private View layout;
    public static RecipeFragment getInstance() {
        if (instance == null) {
            instance = new RecipeFragment();
        }
        return instance;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        InitView(view);
        LoadRecipes();
        return view;
    }
    private void LoadRecipes(){
        EventBus.getDefault().postSticky(new LoadingEvent(true));
        if (Common.isOnline(getContext())) {
            CollectionReference reference = FirebaseFirestore.getInstance()
                    .collection("Recipes");

            List<Recipe> recipes = new ArrayList<>();

            reference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                        recipe = queryDocumentSnapshot.toObject(Recipe.class);
                                        recipes.add(recipe);
                                    }
                                    iRecipeLoadListener.onRecipeLoadSuccess(recipes);
                                }
                                EventBus.getDefault().postSticky(new LoadingEvent(false));
                            }
                        }
                    }).addOnFailureListener(e -> iRecipeLoadListener.onRecipeLoadFailed(e.getMessage()));

        } else {
            PopUp.Network(getContext(), "Connection", "Please check your internet connectivity and try again");
            EventBus.getDefault().postSticky(new LoadingEvent(false));
        }
    }
    private void InitView(View v){
        recyclerView = v.findViewById(R.id.recipe_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setClipToPadding(false);
        iRecipeLoadListener = this;
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, v.findViewById(R.id.custom_toast_container));
    }

    @Override
    public void onRecipeLoadSuccess(List<Recipe> recipes) {
        RecyclerViewRecipeAdapter adapter = new RecyclerViewRecipeAdapter(getActivity(), recipes);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position) -> {
            Recipe recipe = recipes.get(position);
            String name = recipe.getName();
            String image = recipe.getImage();
            String cook = recipe.getCook();
            String prep = recipe.getPreparation();
            String serves = recipe.getServes();
            ArrayList<String> instructions = recipe.getInstructions();
            ArrayList<String> ingredients = recipe.getIngredients();
            String sInstructions = "<ul>", sIngredients = "<ul>";
            for (int i = 0; i < instructions.size(); i++){
                sInstructions += "<li>" + instructions.get(i) + "</li>";;
            }
            for (int i = 0; i < ingredients.size(); i++){
                sIngredients += "<li>" + ingredients.get(i) + "</li>";;
            }
            sInstructions += "</ul>";
            sIngredients += "</ul>";
            Intent intent = new Intent(getActivity(), RecipeDetails.class);
            intent.putExtra("NAME",name);
            intent.putExtra("IMAGE",image);
            intent.putExtra("COOK",cook);
            intent.putExtra("PREP",prep);
            intent.putExtra("SERVES",serves);
            intent.putExtra("INSTRUCTIONS",sInstructions);
            intent.putExtra("INGREDIENTS",sIngredients);
            startActivity(intent);
        });
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }

    @Override
    public void onRecipeLoadFailed(String message) {
        PopUp.Toast(getActivity(), layout,message, Toast.LENGTH_SHORT);
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }
}
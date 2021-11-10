package com.ekspeace.eggs.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.eggs.Activity.AddToBasket;
import com.ekspeace.eggs.Activity.Main;
import com.ekspeace.eggs.Adapter.RecyclerViewEggAdapter;
import com.ekspeace.eggs.Adapter.ViewPagerRecipeAdapter;
import com.ekspeace.eggs.Constants.Common;
import com.ekspeace.eggs.Constants.PopUp;
import com.ekspeace.eggs.Interface.IEggLoadListener;
import com.ekspeace.eggs.Interface.IRecipeLoadListener;
import com.ekspeace.eggs.Model.Egg;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.Recipe;
import com.ekspeace.eggs.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, IRecipeLoadListener, IEggLoadListener {
    private static HomeFragment instance;
    private ViewPager viewPagerRecipe;
    private View layout;
    private RecyclerView recyclerView;
    private IRecipeLoadListener iRecipeLoadListener;
    private IEggLoadListener iEggLoadListener;
    private Recipe recipe;
    private Egg egg;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
        viewPagerRecipe = view.findViewById(R.id.viewPagerRecipe);
        recyclerView = view.findViewById(R.id.home_recyclerView);


        DrawerLayout drawerLayout = view.findViewById(R.id.drawer);
        Toolbar toolbar = view.findViewById(R.id.toolbar_dashboard);
        NavigationView navigationView = view.findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.user_profile_name);
        TextView tvEmail = headerView.findViewById(R.id.user_profile_email);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toolbar.setNavigationIcon(R.drawable.menu);
        navigationView.setNavigationItemSelectedListener(this);

        iRecipeLoadListener = this;
        iEggLoadListener = this;
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_container));

        tvName.setText(Common.currentUser.getName());
        tvEmail.setText(Common.currentUser.getEmail());

        LoadRecipes();
        LoadEggs();
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
                            }
                        }
                    }).addOnFailureListener(e -> iRecipeLoadListener.onRecipeLoadFailed(e.getMessage()));

        } else {
            PopUp.Network(getContext(), "Connection", "Please check your internet connectivity and try again");
            EventBus.getDefault().postSticky(new LoadingEvent(false));
        }
    }
    private void LoadEggs(){
        if (Common.isOnline(getContext())) {
        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("Eggs");

        List<Egg> eggs = new ArrayList<>();

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!Objects.requireNonNull(task.getResult()).isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    egg = queryDocumentSnapshot.toObject(Egg.class);
                                    eggs.add(egg);
                                }
                                iEggLoadListener.onEggLoadSuccess(eggs);
                            }
                            EventBus.getDefault().postSticky(new LoadingEvent(false));
                        }
                    }
                }).addOnFailureListener(e -> iEggLoadListener.onEggLoadFailed(e.getMessage()));

    } else {
        PopUp.Network(getContext(), "Connection", "Please check your internet connectivity and try again");
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }
}

    @Override
    public void onRecipeLoadSuccess(List<Recipe> recipes) {
        ViewPagerRecipeAdapter adapter = new ViewPagerRecipeAdapter(recipes, getActivity());
        viewPagerRecipe.setAdapter(adapter);
        viewPagerRecipe.setPadding(16, 0, -16, 0);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRecipeLoadFailed(String message) {
        PopUp.Toast(getActivity(), layout,message, Toast.LENGTH_SHORT);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about){
            PopUp.About(getContext());
        }
        if(item.getItemId() == R.id.contact){
            PopUp.Contact(getContext());
        }
        if(item.getItemId() == R.id.rate){
            rateMe();
        }
        if(item.getItemId() == R.id.private_policy){
            PopUp.Policy(getContext());
        }
        if(item.getItemId() == R.id.sign_out){
            PopUp.SignOut(getContext());
        }
        return false;
    }
    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getActivity().getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    @Override
    public void onEggLoadSuccess(List<Egg> eggs) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerViewEggAdapter adapter = new RecyclerViewEggAdapter(getActivity(), eggs);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view, position) -> {
            String price = eggs.get(position).getPrice();
            String name = eggs.get(position).getName();
            String image = eggs.get(position).getImage();
            Intent intent = new Intent(getContext(), AddToBasket.class);
            intent.putExtra("PRICE", price);
            intent.putExtra("NAME", name);
            intent.putExtra("IMAGE", image);
            startActivity(intent);
        });
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }

    @Override
    public void onEggLoadFailed(String message) {
        PopUp.Toast(getActivity(), layout,message, Toast.LENGTH_SHORT);
        EventBus.getDefault().postSticky(new LoadingEvent(false));
    }
}
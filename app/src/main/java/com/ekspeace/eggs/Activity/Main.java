package com.ekspeace.eggs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ekspeace.eggs.Fragment.HistoryFragment;
import com.ekspeace.eggs.Fragment.HomeFragment;
import com.ekspeace.eggs.Fragment.ProfileFragment;
import com.ekspeace.eggs.Fragment.RecipeFragment;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Main extends AppCompatActivity {
    private static Main instance;
    private LinearLayout loading;
   private BottomNavigationView bottomNavigationView;
   private FloatingActionButton floatingActionButton;


    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        events();
    }
    private void initView(){
        bottomNavigationView = findViewById(R.id.main_bottom_nav);
        floatingActionButton = findViewById(R.id.main_floatingActionButton);
        loading = findViewById(R.id.home_progressbar);

    }
    private void events() {
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_layout, new HomeFragment()).commit();
        floatingActionButton.setOnClickListener(view -> startActivity(new Intent(this, CheckOut.class)));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.recipe:
                selectedFragment = new RecipeFragment();
                break;
            case R.id.history:
                selectedFragment = new HistoryFragment();
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_layout, selectedFragment)
                .commit();
        return true;
    };
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void LoadingBar(LoadingEvent event) {
        if (event.isLoading())
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.GONE);
    }
}
package com.ekspeace.eggs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ekspeace.eggs.R;
import com.ekspeace.eggs.Adapter.ViewPagerStartupAdapter;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class StartUp extends AppCompatActivity {
    private ViewPager2 viewPager;
    private WormDotsIndicator dotsIndicator;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        initView();
    }
    private void initView(){
        viewPager = findViewById(R.id.startup_view_pager);
        dotsIndicator = findViewById(R.id.startup_dots_indicator);
        button = findViewById(R.id.startup_get_started_btn);

        ViewPagerStartupAdapter adapter = new ViewPagerStartupAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        dotsIndicator.setViewPager2(viewPager);
        viewPager.setOffscreenPageLimit(2);

        button.setOnClickListener(view -> startActivity(new Intent(this, Login.class)));
    }
}
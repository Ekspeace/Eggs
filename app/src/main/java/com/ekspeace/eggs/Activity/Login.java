package com.ekspeace.eggs.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekspeace.eggs.Adapter.ViewPagerLoginAdapter;
import com.ekspeace.eggs.Model.EventBus.LoadingEvent;
import com.ekspeace.eggs.Model.EventBus.RegisterSuccessEvent;
import com.ekspeace.eggs.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Login extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TextView loginText, registerText;
    private LinearLayout loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        events();
    }
    private void initView(){
        viewPager = findViewById(R.id.login_fragment_viewpager);
        loginText = findViewById(R.id.login_text);
        registerText = findViewById(R.id.register_text);
        loading = findViewById(R.id.progressbar_fragment);

        ViewPagerLoginAdapter adapter = new ViewPagerLoginAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }
    private void events(){
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                optionText(position);
                super.onPageSelected(position);
            }
        });

        registerText.setOnClickListener(view -> {
            optionText(viewPager.getCurrentItem());
            viewPager.setCurrentItem(1);
        });
        loginText.setOnClickListener(view -> {
            optionText(viewPager.getCurrentItem());
            viewPager.setCurrentItem(0);
        });
    }
    private void optionText(int position){
        if(position == 0){
            loginText.setTextColor(getResources().getColor(R.color.white));
            loginText.setBackground(getResources().getDrawable(R.drawable.egg_back, getTheme()));
            registerText.setTextColor(getResources().getColor(R.color.brown_700));
            registerText.setBackground(getResources().getDrawable(R.color.brown_500, getTheme()));
        }else {
            loginText.setTextColor(getResources().getColor(R.color.brown_700));
            loginText.setBackground(getResources().getDrawable(R.color.brown_500, getTheme()));
            registerText.setTextColor(getResources().getColor(R.color.white));
            registerText.setBackground(getResources().getDrawable(R.drawable.egg_back, getTheme()));
        }
    }
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
    @Subscribe(sticky = false, threadMode = ThreadMode.MAIN)
    public void RegisterSuccess(RegisterSuccessEvent event) {
        if (event.isSuccess())
            viewPager.setCurrentItem(0);
    }
}
package com.ekspeace.eggs.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ekspeace.eggs.Activity.Login;
import com.ekspeace.eggs.Fragment.LoginFragment;
import com.ekspeace.eggs.Fragment.RegisterFragment;
import com.ekspeace.eggs.Fragment.StartUpFirstFragment;
import com.ekspeace.eggs.Fragment.StartUpSecondFragment;

public class ViewPagerLoginAdapter extends FragmentStateAdapter {

    public ViewPagerLoginAdapter(FragmentManager fa, Lifecycle l) {
        super(fa, l);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return RegisterFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

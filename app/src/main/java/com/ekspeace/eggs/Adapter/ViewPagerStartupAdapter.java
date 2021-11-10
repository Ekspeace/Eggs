package com.ekspeace.eggs.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ekspeace.eggs.Fragment.StartUpFirstFragment;
import com.ekspeace.eggs.Fragment.StartUpSecondFragment;

public class ViewPagerStartupAdapter extends FragmentStateAdapter {

    public ViewPagerStartupAdapter(FragmentManager fa, Lifecycle l) {
        super(fa, l);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return StartUpFirstFragment.getInstance();
            case 1:
                return StartUpSecondFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


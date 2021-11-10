package com.ekspeace.eggs.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ekspeace.eggs.Fragment.HistoryFragment;
import com.ekspeace.eggs.Fragment.HomeFragment;
import com.ekspeace.eggs.Fragment.ProfileFragment;
import com.ekspeace.eggs.Fragment.RecipeFragment;
import com.ekspeace.eggs.Fragment.StartUpFirstFragment;
import com.ekspeace.eggs.Fragment.StartUpSecondFragment;

public class ViewPagerMainAdapter extends FragmentStateAdapter {

    public ViewPagerMainAdapter(FragmentManager fa, Lifecycle l) {
        super(fa, l);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return HomeFragment.getInstance();
            case 1:
                return RecipeFragment.getInstance();
            case 2:
                return HistoryFragment.getInstance();
            case 3:
                return ProfileFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

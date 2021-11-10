package com.ekspeace.eggs.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekspeace.eggs.R;

public class StartUpSecondFragment extends Fragment {
    private static StartUpSecondFragment instance;
    public static StartUpSecondFragment getInstance() {
        if (instance == null) {
            instance = new StartUpSecondFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_up_second, container, false);
    }
}
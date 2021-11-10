package com.ekspeace.eggs.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ekspeace.eggs.R;


public class StartUpFirstFragment extends Fragment {
    private static StartUpFirstFragment instance;
    public static StartUpFirstFragment getInstance() {
        if (instance == null) {
            instance = new StartUpFirstFragment();
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
        return inflater.inflate(R.layout.fragment_start_up_first, container, false);
    }
}
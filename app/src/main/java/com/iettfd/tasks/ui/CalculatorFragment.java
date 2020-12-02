package com.iettfd.tasks.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iettfd.tasks.R;

import butterknife.ButterKnife;

public class CalculatorFragment extends Fragment {

    public CalculatorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, root);

        return root;
    }
}
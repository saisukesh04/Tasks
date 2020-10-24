package com.iettfd.tasks.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.iettfd.tasks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Task1Fragment extends Fragment {

    @BindView(R.id.redButton) Button redButton;
    @BindView(R.id.blueButton) Button blueButton;
    @BindView(R.id.yellowButton) Button yellowButton;
    @BindView(R.id.greenButton) Button greenButton;
    @BindView(R.id.pinkButton) Button pinkButton;

    private boolean red, blue, yellow, green, pink;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this,root);

        red = false;
        blue = false;
        yellow = false;
        green = false;
        pink = false;

        redButton.setOnClickListener(v -> {
            if(!red) {
                redButton.setBackgroundColor(getResources().getColor(R.color.red));
                red = true;
            }else{
                redButton.setBackgroundColor(getResources().getColor(R.color.grey));
                red = false;
            }
        });

        blueButton.setOnClickListener(v -> {
            if(!blue) {
                blueButton.setBackgroundColor(getResources().getColor(R.color.blue));
                blue = true;
            }else{
                blueButton.setBackgroundColor(getResources().getColor(R.color.grey));
                blue = false;
            }
        });

        yellowButton.setOnClickListener(v -> {
            if(!yellow) {
                yellowButton.setBackgroundColor(getResources().getColor(R.color.yellow));
                yellow = true;
            }else{
                yellowButton.setBackgroundColor(getResources().getColor(R.color.grey));
                yellow = false;
            }
        });

        greenButton.setOnClickListener(v -> {
            if(!green) {
                greenButton.setBackgroundColor(getResources().getColor(R.color.green));
                green = true;
            }else{
                greenButton.setBackgroundColor(getResources().getColor(R.color.grey));
                green = false;
            }
        });

        pinkButton.setOnClickListener(v -> {
            if(!pink) {
                pinkButton.setBackgroundColor(getResources().getColor(R.color.pink));
                pink = true;
            }else{
                pinkButton.setBackgroundColor(getResources().getColor(R.color.grey));
                pink = false;
            }
        });
        return root;
    }
}
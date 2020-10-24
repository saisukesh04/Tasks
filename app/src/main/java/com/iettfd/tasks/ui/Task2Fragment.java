package com.iettfd.tasks.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

public class Task2Fragment extends Fragment {

    @BindView(R.id.vibrateButton) Button vibrateButton;
    @BindView(R.id.helloText) TextView helloText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        ButterKnife.bind(this,root);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(4000);
        helloText.startAnimation(fadeIn);
//        fadeIn.setRepeatCount(Animation.INFINITE);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                helloText.animate().rotation(360f).setDuration(4000);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                helloText.animate().rotation(-360f).setDuration(4000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                helloText.animate().rotation(360f).setDuration(4000);
            }
        });

        vibrateButton.setOnClickListener(v -> {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(500);
            }
        });

        return root;
    }
}
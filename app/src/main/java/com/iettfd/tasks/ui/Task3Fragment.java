package com.iettfd.tasks.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.iettfd.tasks.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iettfd.tasks.util.WindowUtil.showToolbar;

public class Task3Fragment extends Fragment {

    @BindView(R.id.task_image_view) ImageView task_image_view;
    @BindView(R.id.prev_btn) Button prev_btn;
    @BindView(R.id.next_btn) Button next_btn;

    int position;
    int[] images;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showToolbar(getActivity());
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        ButterKnife.bind(this,root);
        images = new int[]{R.drawable.img_one, R.drawable.img_two, R.drawable.img_three, R.drawable.img_four};
        position = 0;

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0)
                    position = 3;
                else
                    position--;
                fadeOut(task_image_view);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 3)
                    position = 0;
                else
                    position++;
                fadeOut(task_image_view);
            }
        });

        return root;
    }

    private void fadeOut(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                Glide.with(getContext()).load(images[position]).into(task_image_view);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }
}
package com.iettfd.tasks.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.iettfd.tasks.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iettfd.tasks.util.WindowUtil.showToolbar;

public class Task4Fragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.loading_anim) AVLoadingIndicatorView loading_anim;
    @BindView(R.id.date_btn) Button date_btn;
    @BindView(R.id.toast_btn) Button toast_btn;
    @BindView(R.id.alert_btn) Button alert_btn;
    @BindView(R.id.snackbar_btn) Button snackbar_btn;
    @BindView(R.id.progress_btn) Button progress_btn;

    public Task4Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showToolbar(getActivity());
        View root = inflater.inflate(R.layout.fragment_task4, container, false);

        ButterKnife.bind(this,root);

        toast_btn.setOnClickListener(v -> {
            Toast.makeText(getContext(),"Hope you liked the application",Toast.LENGTH_LONG).show();
        });

        snackbar_btn.setOnClickListener(v -> {
            Snackbar.make(v,"Hope you loved the application",Snackbar.LENGTH_LONG).show();
        });

        progress_btn.setOnClickListener(v -> {
            startAnim();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopAnim();
                }
            },5000);
        });

        date_btn.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext(),this,
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        alert_btn.setOnClickListener(v -> {
            Dialog updateDialog = new Dialog(getContext());
            updateDialog.setContentView(R.layout.custom_alert);
            updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            updateDialog.setCancelable(true);
            updateDialog.show();
        });

        return root;
    }

    void startAnim(){
        loading_anim.setVisibility(View.VISIBLE);
        loading_anim.show();
    }

    void stopAnim(){
        loading_anim.hide();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month+1) + "-" + year;
        date_btn.setText(date);
    }
}
package com.example.accountbook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.accountbook.R;

public class SelectYearMonthDialog extends  Dialog implements View.OnClickListener{
    DatePicker datePicker;
    TextView cancelTv,ensureTv;
    OnEnsureListener onEnsureListener;
    // 回调接口
    public interface OnEnsureListener {
        void onEnsure(int year, int month);
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectYearMonthDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_dialog);
        initView();
        hideDay();
    }

    private void initView() {
        datePicker = findViewById(R.id.date_dialog_dp);
        cancelTv = findViewById(R.id.date_dialog_tv_cancel);
        ensureTv = findViewById(R.id.date_date_dialog_tv_ensure);

        cancelTv.setOnClickListener(this);
        ensureTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_dialog_tv_cancel:
                cancel();
                break;
            case R.id.date_date_dialog_tv_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(year,month);
                }
                cancel();
                break;
        }
    }

    private void hideDay() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        ViewGroup headView = (ViewGroup) rootView.getChildAt(0);  // 获取年月日的下拉框
        // 有的机型可能没有竖线，这里需要进行判断，然后找到显示日的view，把它隐藏
        if (headView.getChildCount()==3) {
            headView.getChildAt(2).setVisibility(View.GONE);
        }else if (headView.getChildCount()==5) {
            headView.getChildAt(3).setVisibility(View.GONE);
            headView.getChildAt(4).setVisibility(View.GONE);
        }
    }

}

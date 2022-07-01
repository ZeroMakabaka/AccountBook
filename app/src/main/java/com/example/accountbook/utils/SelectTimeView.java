package com.example.accountbook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.example.accountbook.R;

/*
*   选择时间对话框
* */
public class SelectTimeView extends Dialog implements View.OnClickListener {
    EditText hourEt,minEt;
    DatePicker datePicker;
    TimePicker timePicker;
    Button ensureBtn,cancelBtn;
    OnEnsureListener onEnsureListener;
    // 接口
    public interface OnEnsureListener{
        public void onEnsure(String time, int year, int month,int day);
    };
    public void setOnEnsureListener(OnEnsureListener onEnsureListener){
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_desc_calendar);
        initView();
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.desc_time_btn_ensure:
                int year = datePicker.getYear();
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();

                // 修改月日的显示格式---> xx月xx日
                String monthStr = String.valueOf(month);
                String dayStr = String.valueOf(day);
                if (month<10) {
                    monthStr = "0" + monthStr;
                }
                if (day<10) {
                    dayStr = "0" + dayStr;
                }

                /***这里应该要做一个判断，如果用户点击了时间，后面有没有修改*****/
                // 获取输入的小时和分钟
                String hourStr = hourEt.getText().toString();
                String minStr = minEt.getText().toString();
                int hour = 0;
                if (!TextUtils.isEmpty(hourStr)) {
                    hour = Integer.parseInt(hourStr);
                    hour = hour % 24;
                }
                int min = 0;
                if (!TextUtils.isEmpty(minStr)) {
                    min = Integer.parseInt(minStr);
                    min = min % 60;
                }
                hourStr = String.valueOf(hour);
                minStr = String.valueOf(min);

                if (hour < 10){
                    hourStr = "0"+hour;
                }
                if (min<10){
                    minStr = "0"+min;
                }
                String timeFormat = year + "年"+monthStr + "月" + dayStr + "日 "+ hourStr +":" + minStr;
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(timeFormat,year,month,day);
                }
                cancel();
                break;
            case R.id.desc_time_btn_cancel:
                cancel();
                break;
        }
    }

    public void initView(){
        hourEt = findViewById(R.id.desc_time_et_hour);
        minEt = findViewById(R.id.desc_time_et_min);
        datePicker = findViewById(R.id.record_desc_time_dp);
        ensureBtn = findViewById(R.id.desc_time_btn_ensure);
        cancelBtn = findViewById(R.id.desc_time_btn_cancel);

        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    /* 隐藏头布局 */
    private void hideDatePickerHeader(){
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null){
            return;
        }
        View headerView = rootView.getChildAt(0);
        if (headerView == null){
            return;
        }

        // 5.0+
        int headerId = getContext().getResources().getIdentifier("day_picker_selector_layout", "id", "android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
            ViewGroup.LayoutParams rootParams = rootView.getLayoutParams();
            rootParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            rootView.setLayoutParams(rootParams);

            ViewGroup animator = (ViewGroup) rootView.getChildAt(1);
            ViewGroup.LayoutParams animatorParams = animator.getLayoutParams();
            animatorParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            animator.setLayoutParams(animatorParams);

            View child = animator.getChildAt(0);
            ViewGroup.LayoutParams childParams = child.getLayoutParams();
            childParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            child.setLayoutParams(childParams);
            return;
        }

        // 6.0+
        headerId = getContext().getResources().getIdentifier("date_picker_header","id","android");
        if (headerId == headerView.getId()) {
            headerView.setVisibility(View.GONE);
        }
    }


}

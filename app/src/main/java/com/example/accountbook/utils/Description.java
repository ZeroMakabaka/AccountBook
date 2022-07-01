package com.example.accountbook.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.accountbook.R;

public class Description extends Dialog implements View.OnClickListener {
    EditText desc; // 备注
    Button cancelBtn,ensureBtn;
    OnEnsureListener onEnsureListener;


    public Description(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_desc);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_desc_cancel_btn:
                cancel();
                break;
            case R.id.record_desc_ensure_btn:
                // 接口回调
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure();
                }
                break;

        }
    }

    public void initView(){
        desc = findViewById(R.id.record_desc_et);
        cancelBtn = findViewById(R.id.record_desc_cancel_btn);
        ensureBtn = findViewById(R.id.record_desc_ensure_btn);
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    public interface OnEnsureListener{
        public void onEnsure();
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    /*获取输入框数据*/
    public String getDesc(){
        return desc.getText().toString().trim();
    }

    /* 设置宽度与屏幕宽度一致 */
    public void setViewHeight(){
        // 获取当前页面宽度
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        Display display = window.getWindowManager().getDefaultDisplay();
        params.width = display.getWidth();
        params.gravity = Gravity.BOTTOM;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setAttributes(params);
        handler.sendEmptyMessageAtTime(1,100);
    }

    /* 自动弹出软键盘 */
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}

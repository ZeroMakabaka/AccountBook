package com.example.accountbook.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.accountbook.MainActivity;
import com.example.accountbook.R;

public class MainBudgetDialog extends Dialog implements View.OnClickListener {
    ImageView cancelIv;
    Button ensureBtn;
    EditText budgetEt;
    OnEnsureListener onEnsureListener;

    public interface OnEnsureListener {
        void onEnsure(Double budget);
    };

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public MainBudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dialog_budget);
        initBudgetDialog();

    }

    // 初始化控件
    public  void initBudgetDialog(){
        cancelIv = findViewById(R.id.main_dialog_budget_iv_error);
        ensureBtn =findViewById(R.id.main_dialog_budget_btn_ensure);
        budgetEt = findViewById(R.id.main_dialog_budget_et);
        // 设置监听
        cancelIv.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_dialog_budget_btn_ensure:
                // 获取输入数值
                String budgetStr = budgetEt.getText().toString();
                if (TextUtils.isEmpty(budgetStr)) {
                    Toast.makeText(getContext(),"输入不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }
                Double budget = Double.parseDouble(budgetStr);
                if (budget<=0){
                    Toast.makeText(getContext(),"预算金额必需大于0",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (onEnsureListener!=null) {
                    onEnsureListener.onEnsure(budget);
                }
                cancel();
                break;
            case R.id.main_dialog_budget_iv_error:
                cancel();
                break;
        }
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

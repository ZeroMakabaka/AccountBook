package com.example.accountbook.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.example.accountbook.R;

public class KeyBoardUtils {
    private KeyboardView keyboardView;
    private EditText editText; // 键盘输入的数字文本
    private final Keyboard myKeyboard; // 自定义键盘

    // 显示键盘
    public void showKeyboard(){
        if (keyboardView.getVisibility()==View.INVISIBLE || keyboardView.getVisibility()==View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    // 隐藏键盘
    public void hideKayBoard(){
        if (keyboardView.getVisibility()==View.VISIBLE || keyboardView.getVisibility()==View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }

    // 当键盘确定之后，需要定义接口回调数据
    public interface OnEnterListener{
        public void onEnter();
    }
    OnEnterListener onEnterListener;

    public void setOnEnterListener(OnEnterListener onEnterListener) {
        this.onEnterListener = onEnterListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        // 阻止弹出系统键盘
        this.editText.setInputType(InputType.TYPE_NULL);

        // 启动自定义键盘
        myKeyboard = new Keyboard(this.editText.getContext(), R.xml.key);
        this.keyboardView.setKeyboard(myKeyboard);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);// 可预览
        // 设置键盘按钮点击监听
        this.keyboardView.setOnKeyboardActionListener(keyListener);
    }

    // 键盘事件监听
    KeyboardView.OnKeyboardActionListener keyListener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        // 按钮点击事件
        @Override
        public void onKey(int i, int[] ints) {
            Editable editable = editText.getText();
            // 获取光标位置
            int start = editText.getSelectionStart();
            switch (i) {
                // 删除
                case Keyboard.KEYCODE_DELETE:
                    if (editable!=null && editable.length()>0) {
                        if (start>0) {
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                // 清零
                case Keyboard.KEYCODE_CANCEL:
                    editable.clear();
                    break;
                // 确认
                case Keyboard.KEYCODE_DONE:
                    onEnterListener.onEnter();
                    break;
                // 其余数字键
                default:
                    editable.insert(start,Character.toString((char) i));
                    break;
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };
}

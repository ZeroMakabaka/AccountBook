package com.example.accountbook;

import android.app.Application;

import com.example.accountbook.db.DBManager;

/*
*   全局应用
* */
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.initDB(getApplicationContext());

    }


}

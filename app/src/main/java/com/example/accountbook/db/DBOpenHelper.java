package com.example.accountbook.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.accountbook.R;

public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(@Nullable Context context) {
        super(context, "account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table typetb(tId integer primary key autoincrement," +
                "imgId integer,checkedImgId integer,typename varchar(20),kind integer)";
        sqLiteDatabase.execSQL(sql);
        insertType(sqLiteDatabase);

        sql = "create table accounttb(id integer primary key autoincrement,typename varchar(20)," +
                "checkedImgId integer,description varchar(100), money double,time varchar(60),year integer," +
                "month integer,day integer,kind integer)";
        sqLiteDatabase.execSQL(sql);
    }

    // 类型表
    private void insertType(SQLiteDatabase sqLiteDatabase) {
        String sql = "insert into typetb (imgId,checkedImgId,typename,kind)" +
                "values(?,?,?,?)";
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_qita,R.mipmap.ic_qita_fs,"其他", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,"餐饮", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,"交通", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,"购物", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,"服饰", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,"日用品", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_yule,R.mipmap.ic_yule_fs,"娱乐", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,"零食", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_yanjiucha,R.mipmap.ic_yanjiucha_fs,"烟酒茶", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,"学习", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,"医疗", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,"住宅", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,"水电煤", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,"通讯", 0});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,"人情往来", 0});

        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_qt,R.mipmap.in_qt_fs,"其他", 1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,"薪资", 1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,"奖金", 1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_jieru,R.mipmap.in_jieru_fs,"借入", 1});
        /*sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,"收债", 1});*/
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,"利息收入",1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_touzi,R.mipmap.in_touzi_fs,"投资回报", 1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,"二手交易", 1});
        sqLiteDatabase.execSQL(sql,new Object[]{R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,"意外所得", 1});



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

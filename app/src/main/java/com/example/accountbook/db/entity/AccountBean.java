package com.example.accountbook.db.entity;

import java.io.Serializable;

/**  记录一条数据内容***/
public class AccountBean implements Serializable {
    public static String ACCOUNT_BEAN_CODE = "accountBean";
    private int aId;
    private String typename;
    private int checkedImgId;
    private String desc;
    private double money;
    private String time;
    private int year;
    private int month;
    private int day; //
    private int kind; // 记录类型

    public AccountBean() {
    }

    public AccountBean(int aId, String typename, int checkedImgId, String desc, double money, String time, int year, int month, int day, int kind) {
        this.aId = aId;
        this.typename = typename;
        this.checkedImgId = checkedImgId;
        this.desc = desc;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }

    public int getaId() {
        return aId;
    }

    public void setaId(int aId) {
        this.aId = aId;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getCheckedImgId() {
        return checkedImgId;
    }

    public void setCheckedImgId(int checkedImgId) {
        this.checkedImgId = checkedImgId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "AccountBean{" +
                "aId=" + aId +
                ", typename='" + typename + '\'' +
                ", checkedImgId=" + checkedImgId +
                ", desc='" + desc + '\'' +
                ", money=" + money +
                ", time='" + time + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", kind=" + kind +
                '}';
    }
}

package com.example.accountbook.utils;

import java.math.BigDecimal;

public class DoubleUtils {
    // 除法保留位数
    public static double div(double v1,double v2){
        double v3= v1/v2;
        BigDecimal b1 = new BigDecimal(v3);
        double value = b1.setScale(4, 4).doubleValue();
        return value;
    }

    // 转换百分比
    public static String ratioToPercent(double val) {
        double v = val*100;
        BigDecimal b1 = new BigDecimal(v);
        double v1 = b1.setScale(2,4).doubleValue();
        String per = v1+"%";
        return per;
    }
}

package com.example.leesnriud.myucrop;

/**
 * Created by lee.snriud on 2018/4/24.
 */

class CommonUtils {

    //获取时间戳
    public static String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;
        String timeStamp = String.valueOf(time);

        return timeStamp;
    }
}

package com.lijie.smartweather.utils;

public class DoubleClickExit {
    public static long mLastClick = 0L;
    private static final int THRESHOLD = 2000;
    //判断是否是第二次点击返回
    public static boolean check(){
            long now = System.currentTimeMillis();
            boolean  click= now - mLastClick <THRESHOLD;
        mLastClick = now;
        return click;
    }
}

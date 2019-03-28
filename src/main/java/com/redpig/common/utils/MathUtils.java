package com.redpig.common.utils;

/**
 * Created by hetao on 2018/6/25.
 */
public class MathUtils {
    public static int randomNum(int min, int max) {
        return (int) (min + Math.random() * (max - min + 1));
    }
}

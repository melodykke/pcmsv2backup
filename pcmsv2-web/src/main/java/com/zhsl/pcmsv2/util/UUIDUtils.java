package com.zhsl.pcmsv2.util;

import java.util.UUID;

public class UUIDUtils {

    public static String getUUID(){
        return UUID.randomUUID().toString().toUpperCase();
    }
    public static String getUUIDString(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static void main(String[] args) {
        System.out.println("格式前的UUID ： " + UUID.randomUUID().toString());
        System.out.println("格式化后的UUID ：" + getUUID());
    }
}

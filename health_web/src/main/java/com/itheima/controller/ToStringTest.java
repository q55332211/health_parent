package com.itheima.controller;



import java.util.ArrayList;
import java.util.List;

public class ToStringTest {
    static int i = 1;

    public static void main(String args[]) {
        method();
    }

    /**
     * 九九乘法表
     */
    private static void method9x9() {
        for (i = 1; i <= 9; i++) {

            for (int j = 1; j <= i; j++) {
                System.out.print(j + "x" + i + "=" + j * i);
            }
            System.out.println("");

        }
    }

    /**
     * 获得所有排序
     */
    public static void method() {


    }
}

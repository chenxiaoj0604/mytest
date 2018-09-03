package com.mytest.tests;

/**
 * Created by duanxun on 2018-08-29.
 *
 */
public class ExceptionsDealType {

    public static void main(String[] args) {
        dealExceptions();
    }

    private static void dealExceptions(){
        try {
            exception1();
        }catch (Exception e){
            System.out.println("方法捕获异常:" + e.getMessage());
        }
    }

    //异常被捕获所以不会抛出
    private static void exception1() throws Exception{
        try {
            int a = 5/0;
        }catch (Exception e){
            System.out.println("出现异常");
        }
    }
}

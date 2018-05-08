package com.mytest.mains;

/**
 * Created by duanxun on 2018/5/7.
 */
public class Polymorphism extends TT{
    public Polymorphism(String s){
        super(s);
        System.out.println("how do you do?");
    }
    public Polymorphism(){
        this("I am Tom");
    }
}
class TT{
    public TT(){
        System.out.println("what a pleasure!");
    }
    public TT(String s){
        this();
        System.out.println("I am " + s);
    }
}

class main{
    public static void main(String[] args) {
        TT t = new Polymorphism("Sam");
    }
}
package com.mytest.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by duanxun on 2018-08-23.
 * java程序执行cmd命令
 */
public class CMDTest {
    public static void main(String[] args) {
        //打开计算器
        //cmd("cmd /c calc");

        //获取磁盘大小
        cmd("cmd /c fsutil volume diskfree d: >>D:\\size.txt");

    }

    public static void cmd(String str){
        /*获取cmd命令*/
        try {
            Process pro = Runtime.getRuntime().exec(str); //添加要进行的命令
            BufferedReader br = new BufferedReader(new InputStreamReader(pro
                    .getInputStream())); //虽然cmd命令可以直接输出，但是通过IO流技术可以保证对数据进行一个缓冲。
            String msg = null;
            while ((msg = br.readLine()) != null) {
                System.out.println(msg);
            }
        } catch (IOException exception) {
        }
        /*cmd /c dir 是执行完dir命令后关闭命令窗口
        cmd /k dir 是执行完dir命令后不关闭命令窗口
        cmd /c start dir  会打开一个新窗口后执行dir命令，原窗口会关闭
        cmd /k start dir  会打开一个新窗口后执行dir命令，原窗口不会关闭
        cmd /?  查看帮助信息*/
    }

}


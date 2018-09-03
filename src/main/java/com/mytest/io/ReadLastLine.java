package com.mytest.io;

import java.io.*;

/**
 * Created by duanxun on 2018-08-28.
 * 读最后一行
 */
public class ReadLastLine {
    public static void main(String[] args) throws FileNotFoundException {
        read("log1.txt","utf-8");
    }

    public static void read(String filename, String charset) {
        RandomAccessFile rf = null;
        try {
            rf = new RandomAccessFile(filename, "r");
            long len = rf.length();
            long start = rf.getFilePointer();
            long nextend = start + len - 1;
            String line;
            rf.seek(nextend);
            int c = -1;
            while (nextend > start) {
                c = rf.read();
                if (c == '\n' || c == '\r') {
                    line = rf.readLine();
                    if (line != null && !line.equals("")) {
                        System.out.println("bbb" + new String(line
                                .getBytes("ISO-8859-1"), charset));
                        break;
                    } else {
                        System.out.println(line);
                    }
                }
                nextend--;
                rf.seek(nextend);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null)
                    rf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

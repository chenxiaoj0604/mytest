package com.mytest.ftp;

import com.mytest.msg.WebService1;
import com.mytest.msg.WebService1Soap;
import lombok.extern.slf4j.Slf4j;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import static com.mytest.cmd.CMDTest.cmd;

/**
 * Created by duanxun on 2018-08-23.
 * （公司用) ftp 连接下载文件(监测磁盘空间)
 */
@Slf4j
public class FtpTest1 {
    public static void main(String[] args) {
        String ip1 = "192.168.3.15";
        String ip2 = "192.168.3.16";
        diskMonitor(ip1);
        diskMonitor(ip2);
    }

    //连接到ftp
    public static FtpClient connectFTP(String url, int port, String username, String password) {
        //创建ftp
        FtpClient ftp = null;
        try {
            //创建地址
            SocketAddress addr = new InetSocketAddress(url, port);
            //连接
            ftp = FtpClient.create();
            ftp.connect(addr);
            //登陆
            ftp.login(username, password.toCharArray());
            ftp.setBinaryType();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }

//    //从FTP服务器下载文件
//    public static void download(String localFile, String ftpFile, FtpClient ftp) {
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            // 获取ftp上的文件
//            is = ftp.getFileStream(ftpFile);
//            File file = new File(localFile);
//            byte[] bytes = new byte[1024];
//            int i;
//            fos = new FileOutputStream(file);
//            while((i = is.read(bytes)) != -1){
//                fos.write(bytes, 0, i);
//            }
//            System.out.println("download success!!");
//
//        } catch (FtpProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if(fos!=null) {
//                    fos.close();
//                }
//                if(is!=null){
//                    is.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static String readStr(String ftpFile, FtpClient ftp) throws IOException, FtpProtocolException {
        cmd("cmd /c quote site exec disk_space.bat");
        // 获取ftp上的文件
        InputStream is = ftp.getFileStream(ftpFile);
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        String str = result.toString(StandardCharsets.UTF_8.name());
        str = str.replaceAll("\r\n",",");
        str = str.replaceAll("\\s*","");
        return str;
    }

    //从字符串中获取总字节数和可用字节数
    public static Volume getVolume(String str){
        Volume volume = new Volume();
        String[] strs = str.split(",");
        String availableByte = strs[0].substring(strs[0].indexOf(":") + 1);
        String totalByte = strs[1].substring(strs[1].indexOf(":") + 1);
        volume.setAvailableByte(Double.valueOf(availableByte));
        volume.setTotalByte(Double.valueOf(totalByte));
        return volume;
    }

    //硬盘占用空间是否大于等于85%
    public static double getRatio(Volume volume){
        double ratio = (1 - volume.getAvailableByte()/volume.getTotalByte()) * 100;
        //System.out.println("(1 - " + volume.getAvailableByte() + "/" + volume.getTotalByte()  + ") * 100 = " + ratio);
        return ratio;
    }

    //磁盘监控
    public static void diskMonitor(String ip){
        FtpClient ftpClient1 = connectFTP(ip,21,"ftpuser","123!@#asd");
        try {
            String str1 = readStr(ip + "_S.txt",ftpClient1);
            //System.out.println("测试" + str1);
            Volume volume1 = getVolume(str1);
            double ratio1 = getRatio(volume1);
            if (ratio1 >= 85){
                String msg = ip + "的服务器S盘空间已不足" + (int)(100 - ratio1) + "%,总空间:字节总数 " + (int)(volume1.getTotalByte()/(1024.0 * 1024.0 * 1024.0)) +
                        "GB,可用空间:可用字节总数 " + (int)(volume1.getAvailableByte()/(1024.0 * 1024.0 * 1024.0)) + "GB,请及时清理空间。";
                WebService1 webService1 = new WebService1();
                WebService1Soap service1Soap= webService1.getWebService1Soap();
                String result = service1Soap.txtServerMsg("硬盘占用率监控",msg,null);
            }
            System.out.println(ip + "的服务器S盘空间已不足" + (int)(100 - ratio1) + "%,总空间:字节总数 " + (int)(volume1.getTotalByte()/(1024.0 * 1024.0 * 1024.0)) +
                    "GB,可用空间:可用字节总数 " + (int)(volume1.getAvailableByte()/(1024.0 * 1024.0 * 1024.0)) + "GB,请及时清理空间。");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        }
    }

}

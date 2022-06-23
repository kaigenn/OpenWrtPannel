package com.example.openwrtpannel.Util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    public static void main(String[] args) throws IOException {

//        Scanner scanner = new Scanner(System.in);
//        //ip地址
//        System.out.print("host:");
//        String host = scanner.next();
//        //端口号
//        System.out.print("port:");
//        int port = scanner.nextInt();
//        //用户名
//        System.out.print("name:");
//        String userName = scanner.next();
//        //密码
//        System.out.print("password:");
//        String password = scanner.next();

        //ip地址
        String host = "192.168.100.100";
        //端口号
        int port = 22;
        //用户名
        String userName = "root";
        //密码
        String password = "prkqzeds1234";


        //密钥文件
        String privateKeyFile = " ";

        Connection sshConnection = getSSHConnection(host, port, userName, password, privateKeyFile);

        assert sshConnection != null;
        getCpuName(sshConnection);
        getCpuFreq(sshConnection);
        getCpuTemp(sshConnection);


    }

    /**
     * 建立与服务器的连接
     *
     * @param host           服务器IP
     * @param port           端口   int port = 22;（默认的，直接用即可）
     * @param userName       登录服务器的用户名（work）
     * @param password       登录服务器的密码（为空就行）
     * @param privateKeyFile 与服务器公钥对应的私钥文件  String pubkeypath = "src/main/resources/sshkey/id_rsa";
     * @return 返回登录的连接， 在使用的最后一定记得关闭connect资源
     * @throws IOException
     */
    public static Connection getSSHConnection(String host, int port, String userName, String password, String privateKeyFile) throws IOException {
        Connection connection = new Connection(host, port);
        connection.connect();
        boolean b = connection.authenticateWithPassword(userName,password);

        if (b) {
            System.out.println("success");
            return connection;
        } else {
            System.out.println("登录连接失败，请检查用户名、密码、私钥文件");
            return null;
        }
    }


    /**
     * 获取指定log文件的指定关键字的日志信息
     *
     * @param connection SSH的连接
     * @param logFile    需要读取的log文件 全路径 /opt/dubbo_server/risk-server-provider-xjd/logs/server-info.log
     * @param key        关键字     例如  节点3150调用策略引擎开始
     * @param timeKey    時間关键字   例如 09-21 14:42:1
     * @return 返回需要的日志信息行 如果有多天日志信息满足条件，只返回最后一天日志信息
     * @throws IOException
     */
    public static String getLogInfo(Connection connection, String logFile, String key, String timeKey) throws IOException {

        String cmd = "tail -1000 " + logFile + " | grep '" + key + "'" + " | grep '" + timeKey + "'";

        System.out.println("====cmd===" + cmd);

//                "tail -1000 /opt/dubbo_server/risk-server-provider-xjd/logs/server-info.log | grep '节点3150调用策略引擎开始，' | grep '09-21 14:42:1'";
        Session session = connection.openSession();

        session.execCommand(cmd);//执行shell命令
        //处理获取的shell命令的输出信息
        InputStream stdout = session.getStdout();
        InputStreamReader inputStreamReader = new InputStreamReader(stdout);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = bufferedReader.readLine();
        String lastLineLog = null;
        System.out.println("==========以下是获取日志的全部信息============");

        while (s != null) {
            if (s != null) {
                lastLineLog = s;
                System.out.println(s);
            }
            s = bufferedReader.readLine();
        }

        System.out.println("==========以上是获取日志的全部信息============");
        System.out.println("========以下是日志的最后一行数据=======");
        System.out.println(lastLineLog);

        //最后关闭session资源
        session.close();
        return lastLineLog;
    }

    public static List getCpuFreq(Connection connection) throws IOException{
        String getCpuFreqCmd = "cat /proc/cpuinfo | grep \"cpu MHz\"";
        Session session = connection.openSession();
        session.execCommand(getCpuFreqCmd);

        List list = new ArrayList();

        InputStream stdout = session.getStdout();
        InputStreamReader inputStreamReader = new InputStreamReader(stdout);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = bufferedReader.readLine();
        int i = 0;
        while (s != null){
            String arrys[] = s.split(":");
            System.out.printf("CPU "+i+" Frequency:");
            System.out.println(arrys[1] + " MHz");
            list.add(arrys[1]);
            s = bufferedReader.readLine();
            i++;
        }
        session.close();
        return list;
    }

    public static List getCpuTemp(Connection connection) throws IOException {
        String getCpuTempCmd = "sensors | grep \"Package id 0\"";
        Session session = connection.openSession();
        session.execCommand(getCpuTempCmd);

        List list = new ArrayList();

        InputStream stdout = session.getStdout();
        InputStreamReader inputStreamReader = new InputStreamReader(stdout);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = bufferedReader.readLine();
        while (s != null){
            String arrys[] = s.split(": | +");
            System.out.println(s);
            System.out.println(arrys[4] + "℃");
            list.add(arrys[4]);
            s = bufferedReader.readLine();
        }
        session.close();
        return list;
    }

    public static List getCpuName(Connection connection) throws IOException{

        String getCpuNameCmd = "cat /proc/cpuinfo | grep \"model name\"";
        Session session = connection.openSession();
        session.execCommand(getCpuNameCmd);

        List list = new ArrayList();

        InputStream stdout = session.getStdout();
        InputStreamReader inputStreamReader = new InputStreamReader(stdout);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String s = bufferedReader.readLine();
        int i = 0;
        while (s != null){
            String arrys[] = s.split(":");
            System.out.printf("CPU "+i+" Name:");
            System.out.println(arrys[1]);
            list.add(arrys[1]);
            s = bufferedReader.readLine();
            i++;
        }
        session.close();
        return list;
    }

}

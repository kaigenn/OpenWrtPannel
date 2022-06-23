package com.example.openwrtpannel.Dao;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SSHDao {

    private String userName = "root";

    private String password = "prkqzeds1234";

    private String host = "192.168.100.1";

    private Integer port = 22;

    private Connection connection = new Connection(host,port);

    private Session session = null;

    public void getSSHSession() {
        try {
            connection.connect();
            connection.authenticateWithPassword(userName,password);
            session = connection.openSession();
        }catch (IOException e){
            System.out.println("====================连接有误，请检查参数====================");
        }
    }

    public List executeCmd(String cmd) {
        getSSHSession();
        try {
            session.execCommand(cmd);
        }catch (IOException e){
            System.out.println("====================连接有误，请检查参数====================");
        }
        List list = new ArrayList();
        InputStream stdout = session.getStdout();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stdout);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String s = bufferedReader.readLine();
            while (s != null){
                s = bufferedReader.readLine();
                list.add(s);
            }
        }catch (IOException e){
            System.out.println("====================连接有误，请检查参数====================");
        }
        return list;
    }
}

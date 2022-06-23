package com.example.openwrtpannel.Util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.sun.corba.se.pept.transport.ConnectionCache;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SSHUtil {

    public List getCpuFreq(Connection connection) throws IOException {
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

    public List getCpuTemp(Connection connection) throws IOException {
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
        return list;
    }

    public List getCpuName(Connection connection) throws IOException{
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

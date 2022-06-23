package com.example.openwrtpannel.Dao.impl;

import com.example.openwrtpannel.Dao.CPUDao;
import com.example.openwrtpannel.Dao.SSHDao;

import java.util.ArrayList;
import java.util.List;

public class CPUDaoImpl extends SSHDao implements CPUDao {
    @Override
    public List getCPUFreq() {
        String cmd = "cat /proc/cpuinfo | grep \"cpu MHz\"";

        List list = executeCmd(cmd);

        return list;
    }

    public static void main(String[] args) {
        CPUDao cpuDao = new CPUDaoImpl();
        List list = cpuDao.getCPUFreq();
        System.out.println(list);
    }
}

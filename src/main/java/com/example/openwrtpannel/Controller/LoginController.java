package com.example.openwrtpannel.Controller;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.example.openwrtpannel.Bean.SSH;
import com.example.openwrtpannel.Util.SSHConnectionUtil;
import com.example.openwrtpannel.Util.SSHUtil;
import com.example.openwrtpannel.Util.TestUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    SSHConnectionUtil sshConnectionUtil = new SSHConnectionUtil();

    SSHUtil sshUtil = new SSHUtil();


    @RequestMapping("/login")
    @ResponseBody
    public Connection login(String host, int port, String userName, String password) throws IOException {
        return TestUtil.getSSHConnection(host,port,userName,password,null);
    }
    @RequestMapping("/getCpuInfo")
    @ResponseBody
    public Map<Object, List> getCpuInfo(SSH ssh) throws IOException {

        Connection connection = sshConnectionUtil.getSSHConnection(ssh);

        Map<Object,List> map = new HashMap();
        map.put("CoreFrequency", sshUtil.getCpuFreq(connection));
        map.put("CpuName", sshUtil.getCpuName(connection));
        map.put("CpuTemp", sshUtil.getCpuTemp(connection));

        return map;
    }
}

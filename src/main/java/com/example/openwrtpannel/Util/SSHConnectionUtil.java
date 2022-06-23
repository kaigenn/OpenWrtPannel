package com.example.openwrtpannel.Util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.example.openwrtpannel.Bean.SSH;

import java.io.IOException;

public class SSHConnectionUtil {

    public Connection getSSHConnection(SSH ssh) throws IOException {
        Connection connection = new Connection(ssh.getHost(), ssh.getPort());
        connection.connect();
        boolean b = connection.authenticateWithPassword(ssh.getUserName(), ssh.getPassword());

        if (b) {
            return connection;
        } else {
            return null;
        }
    }

}

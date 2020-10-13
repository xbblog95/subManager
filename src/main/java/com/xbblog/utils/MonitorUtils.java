package com.xbblog.utils;


import org.apache.commons.net.telnet.TelnetClient;

public class MonitorUtils {

    private final static int TIMEOUT = 10 * 1000;

    public static long testTCPActive(String host, int port){
        TelnetClient telnet = new TelnetClient();
        try
        {
            long time = System.currentTimeMillis();
            telnet.setConnectTimeout(TIMEOUT);
            telnet.connect(host, port);
            telnet.sendAYT(TIMEOUT);
            telnet.disconnect();
            return System.currentTimeMillis() - time;
        }
        catch (Exception e)
        {
            return 0L;
        }
    }
}
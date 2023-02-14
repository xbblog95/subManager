package com.xbblog.config;

import com.xbblog.business.handler.MonitorNodeHandler;
import com.xbblog.business.handler.impl.StairSpeedTestMonitorNodeHandlerImpl;
import com.xbblog.business.handler.impl.TelnetMonitorNodeHandlerImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class NormalConfiguration {


    public static String webGroup;

    @Value("${node.monitor.type}")
    public String monitorType = "telnet";



    @Value("${web.group}")
    public void setWebGroup(String webGroup) {
        NormalConfiguration.webGroup = webGroup;
    }

    @Bean
    public MonitorNodeHandler getMonitorHandler()
    {
        if("telnet".equals(monitorType))
        {
            return new TelnetMonitorNodeHandlerImpl();
        }
        else if("stairSpeedTest".equals(monitorType))
        {
            return new StairSpeedTestMonitorNodeHandlerImpl();
        }
        else
        {
            return new TelnetMonitorNodeHandlerImpl();
        }
    }

}

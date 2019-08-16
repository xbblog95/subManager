package com.xbblog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NormalConfiguration {


    public static String webGroup;

    @Value("${web.group}")
    public void setWebGroup(String webGroup) {
        NormalConfiguration.webGroup = webGroup;
    }
}

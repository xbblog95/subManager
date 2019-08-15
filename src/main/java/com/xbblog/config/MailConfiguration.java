package com.xbblog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@Component
//@Configuration
//@PropertySource({"file:D:\\subManager\\mail.properties", "file:D:\\subManager\\web.properties"})
public class MailConfiguration {

    private String fromUserName;

    private String address;


    private String from;


    public String getAddress() {
        return address;
    }

    @Value("#{monitor.email.address}")
    public void setAddress(String address) {
        this.address = address;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    @Value("#{monitor.email.fromUserName}")
    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFrom() {
        return from;
    }

    @Value("#{monitor.email.fromAddress}")
    public void setFrom(String from) {
        this.from = from;
    }
}

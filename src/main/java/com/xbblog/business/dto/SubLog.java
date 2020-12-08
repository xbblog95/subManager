package com.xbblog.business.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SubLog  {

    private String ip;

    private int userId;

    private String country;

    private String region;

    private String province;

    private String city;

    private String type;

    private Date time;

    public SubLog(String ip, String type, int userId) {
        this.ip = ip;
        this.type = type;
        this.time = new Date();
        this.userId = userId;
    }
}

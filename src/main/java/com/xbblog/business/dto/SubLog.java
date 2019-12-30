package com.xbblog.business.dto;

import java.util.Date;

public class SubLog  {

    private String ip;

    private int userId;

    private String ipdesc;

    private String type;

    private Date time;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIpdesc() {
        return ipdesc;
    }

    public void setIpdesc(String ipdesc) {
        this.ipdesc = ipdesc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public SubLog(String ip, String type, int userId) {
        this.ip = ip;
        this.type = type;
        this.time = new Date();
        this.userId = userId;
    }
}

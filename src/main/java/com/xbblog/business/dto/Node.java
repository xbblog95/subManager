package com.xbblog.business.dto;

public class Node {

    private int id;
    private String source;
    private int flag;
    private int subscribeId;


    public int getSubscribeId() {
        return subscribeId;
    }

    public int getFlag() {
        return flag;
    }


    public int getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setSubscribeId(int subscribeId) {
        this.subscribeId = subscribeId;
    }

    public Node(String source, int subscribeId) {
        this.source = source;
        this.flag = 1;
        this.subscribeId = subscribeId;
    }

    public Node(int id, String source, int flag, int subscribeId) {
        this.id = id;
        this.source = source;
        this.flag = flag;
        this.subscribeId = subscribeId;
    }
}
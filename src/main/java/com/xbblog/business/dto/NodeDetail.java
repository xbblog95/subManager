package com.xbblog.business.dto;

public class NodeDetail {

    //id
    private int id;

    //节点id
    private int nodeId;

    //ip地址
    private String ip;

    //端口
    private int port;

    //备注
    private String remarks;

    // 加密方式
    private String security;


    //节点类型
    private String type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NodeDetail( String ip, int port, String remarks, String security, String type) {
        this.ip = ip;
        this.port = port;
        this.remarks = remarks;
        this.security = security;
        this.type = type;
    }

    public NodeDetail() {
    }
}

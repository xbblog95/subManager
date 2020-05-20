package com.xbblog.business.dto;

import lombok.Data;

@Data
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

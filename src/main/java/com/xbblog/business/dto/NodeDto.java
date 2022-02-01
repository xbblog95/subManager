package com.xbblog.business.dto;

import lombok.Data;

@Data
public class NodeDto extends NodeDetail {

    //v2ray uuid
    private String uuid;

    //v2ray 额外id
    private int alterId;

    //v2ray 网络传输
    private String network;

    //v2ray 伪装类型
    private String camouflageType;

    //v2ray 伪装域名
    private String camouflageHost;

    //v2ray 伪装路径
    private String camouflagePath;

    //v2ray 底层传输协议
    private String camouflageTls;


    //ss/ssr密码
    private String password;

    //ssr混淆参数
    private String obfsparam;

    //ssr协议参数
    private String protoparam;

    //ssr协议
    private String protocol;

    //ssr混淆
    private String obfs;

    //来源
    private String source;

    //有效性
    private int flag;

    private String group;
    //订阅id
    private int subscribeId;

    private int udp;

    public NodeDto(String ip, int port, String remarks, String uuid, int alterId,
                           String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls, String password, int udp) {
        super(ip, port, remarks, "auto", "v2ray", udp);
        this.uuid = uuid;
        this.alterId = alterId;
        this.network = network == null ? "tcp" : network;
        this.camouflageType = camouflageType == null ? "" : camouflageType;
        this.camouflageHost = camouflageHost == null ? "" : camouflageHost;
        this.camouflagePath = camouflagePath == null ? "" : camouflagePath;
        this.camouflageTls = camouflageTls == null ? "" : camouflageTls;
        this.password = password == null ? "" : password;
    }



    public NodeDto(String ip, int port, String remarks, String security, String type, int udp) {
        super(ip, port, remarks, security, type, udp);
    }

    public NodeDto() {
        super();
    }
}

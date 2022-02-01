package com.xbblog.business.dto;


import lombok.Data;

@Data
public class SnellNode extends NodeDetail{

    private String version;

    private String host;

    private String psk;


    public SnellNode(String ip, int port, String remarks, String security, String version, String host, String psk, int udp) {
        super(ip, port, remarks, security, "snell", udp);
        this.version = version;
        this.host = host;
        this.psk = psk;
    }
}

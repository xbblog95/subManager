package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClashNodeConfigDto {

    private String name;

    private String type;

    private String server;

    private Integer port;

    private String uuid;

    private Integer alterId;

    private String cipher;

    private Boolean udp;

    private String password;

    private String sni;

    private String network;

    @YamlProperty("ws-path")
    private String wsPath;

    @YamlProperty("ws-headers")
    private ClashNodeWsHeadersConfigDto wsHeaders;

    @YamlProperty("ws-opts")
    private ClashNodoWsConfigDto wsOpt;

    private String plugin;

    @YamlProperty("plugin-opts")
    private ClashNodePluginConfigDto pluginOpts;

    private Boolean tls;

    @YamlProperty("skip-cert-verify")
    private Boolean skipCertVerify;

    private String servername;

    @YamlProperty("h2-opts")
    private ClashNodeH2ConfigDto h2Opts;

    @YamlProperty("http-opts")
    private ClashNodeHttpConfigDto httpOpts;

    @YamlProperty("grpc-opts")
    private ClashNodeGrpcConfigDto grpcOpts;

    private String username;

    private String psk;

    private String version;

    @YamlProperty("obfs-opts")
    private ClashNodeObfsConfigDto obfsOpts;

    private List<String> alpn;

    private String obfs;

    private String protocol;

    @YamlProperty("obfs-param")
    private String  obfsParam;

    @YamlProperty("protocol-param")
    private String protocolParam;


}

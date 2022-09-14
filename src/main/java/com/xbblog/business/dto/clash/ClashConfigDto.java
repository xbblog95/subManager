package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ClashConfigDto {

    private int port;

    @YamlProperty("socks-port")
    private int socksPort;

    @YamlProperty("redir-port")
    private int redirectPort;

    @YamlProperty("tproxy-port")
    private int tProxyPort;

    @YamlProperty("mixed-port")
    private int mixedPort;

    private List<String> authentication;



    @YamlProperty("allow-lan")
    private Boolean allowLan;

    @YamlProperty("bind-address")
    private Boolean bindAddress;

    private String mode;

    @YamlProperty("log-level")
    private String logLevel;

    private Boolean ipv6;

    @YamlProperty("external-controller")
    private String externalController;

    @YamlProperty("external-ui")
    private String externalUi;

    private String secret;

    @YamlProperty("interface-name")
    private String interfaceName;

    @YamlProperty("routing-mark")
    private String routingMark;

    private Map<String, String> hosts;

    private ClashProfileConfigDto profile;

    private ClashDnsConfigDto dns;

    private List<ClashNodeConfigDto> proxies;

    @YamlProperty("proxy-groups")
    private List<ClashProxyGroupsConfigDto> proxyGroups;

    private List<String> rules;

    @YamlProperty("rule-providers")
    private Map<String, ClashProxyProvidersConfigDto> proxyProviders;

    @YamlProperty("clash-for-android")
    private ClashConfigForAndroidDto clashForAndroid;


    public static ClashConfigDto newInstance() {
        ClashConfigDto clashConfigDto = new ClashConfigDto();
        clashConfigDto.setPort(7890);
        clashConfigDto.setSocksPort(7891);
        clashConfigDto.setRedirectPort(7892);
        clashConfigDto.setAllowLan(true);
        clashConfigDto.setMode("Rule");
        clashConfigDto.setLogLevel("info");
        clashConfigDto.setExternalController("127.0.0.1:9090");
        clashConfigDto.setSecret("");
        return clashConfigDto;
    }
}

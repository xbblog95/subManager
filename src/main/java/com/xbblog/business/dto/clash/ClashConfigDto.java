package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import com.xbblog.base.utils.YamlProPertyUtils;
import lombok.Data;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileNotFoundException;
import java.io.FileReader;
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

    @YamlProperty("clash-for-android")
    private ClashConfigForAndroidDto clashForAndroid;

    public static void main(String[] args) throws FileNotFoundException {
        Constructor constructor = new Constructor(ClashConfigDto.class);
        constructor.setPropertyUtils(new YamlProPertyUtils());
        Yaml yaml = new Yaml(constructor);
        ClashConfigDto clashConfigDto = yaml.loadAs(new FileReader("C:\\Users\\f5505\\.config\\clash\\profiles\\1643475726491.yml"), ClashConfigDto.class);
        String dump = yaml.dump(clashConfigDto);
        System.out.println(dump);
    }

}

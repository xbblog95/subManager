package com.xbblog.business.dto.clash;
import com.xbblog.business.dto.clash.ClashDnsCallbackFilterConfigDto;
import java.util.ArrayList;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClashDnsConfigDto {

    private Boolean enable;

    private String listen;

    @YamlProperty("default-nameserver")
    private List<String> defaultNameserver;

    @YamlProperty("enhanced-mode")
    private String enhancedMode;

    @YamlProperty("fake-ip-range")
    private String fakeIpRange;

    @YamlProperty("fake-ip-filter")
    private List<String> fakeIpFilter;

    private List<String> nameserver;

    private Boolean ipv6;

    @YamlProperty("use-hosts")
    private Boolean useHosts;

    private List<String> fallback;



    @YamlProperty("fallback-filter")
    private ClashDnsCallbackFilterConfigDto fallbackFilter;

    @YamlProperty("nameserver-policy")
    private List<String> nameserverPolicy;

    public static ClashDnsConfigDto newInstance() {
        ClashDnsConfigDto clashDnsConfigDto = new ClashDnsConfigDto();
        clashDnsConfigDto.setEnable(true);
        clashDnsConfigDto.setListen("0.0.0.0:53");
        List<String> defaultNameServers = new ArrayList<>();
        defaultNameServers.add("114.114.114.114");
        defaultNameServers.add("223.5.5.5");
        clashDnsConfigDto.setDefaultNameserver(defaultNameServers);
        clashDnsConfigDto.setEnhancedMode("redir-host");
        List<String> fakeIpFilters = new ArrayList<>();
        fakeIpFilters.add("198.18.0.1/16");
        clashDnsConfigDto.setFakeIpFilter(fakeIpFilters);
        List<String> nameServers = new ArrayList<>();
        nameServers.add("8.8.8.8");
        nameServers.add("8.8.4.4");
        nameServers.add("tls://dns.rubyfish.cn:853");
        nameServers.add("https://1.1.1.1/dns-query");
        clashDnsConfigDto.setNameserver(nameServers);
        clashDnsConfigDto.setIpv6(false);
        clashDnsConfigDto.setUseHosts(false);
        List<String> fallbacks = new ArrayList<>();
        fallbacks.add("tcp://1.1.1.1");
        clashDnsConfigDto.setFallback(fallbacks);
        return clashDnsConfigDto;
    }
}

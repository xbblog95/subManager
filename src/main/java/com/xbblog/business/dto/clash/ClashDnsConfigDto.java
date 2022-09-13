package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.ArrayList;
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
        clashDnsConfigDto.setIpv6(false);
        clashDnsConfigDto.setListen("0.0.0.0:53");
        clashDnsConfigDto.setEnhancedMode("redir-host");
        List<String> nameservers = new ArrayList<>();
        nameservers.add("tls://8.8.8.8:853");
        nameservers.add("tls://8.8.4.4:853");
        nameservers.add("tls://1.1.1.1:853");
        clashDnsConfigDto.setNameserver(nameservers);
        List<String> fallbacks = new ArrayList<>();
        fallbacks.add("tls://1.0.0.1:853");
        fallbacks.add("tls://1.1.1.1:853");
        fallbacks.add("tls://dns.google:853");
        clashDnsConfigDto.setFallback(fallbacks);
        return clashDnsConfigDto;
    }
}

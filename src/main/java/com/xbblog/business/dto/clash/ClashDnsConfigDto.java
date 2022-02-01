package com.xbblog.business.dto.clash;

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


}

package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClashProxyGroupsConfigDto {

    private String name;

    private String type;

    private List<String> proxies;

    private Integer tolerance;

    private Boolean lazy;

    private String url;

    private Integer interval;

    private String strategy;

    @YamlProperty("disable-udp")
    private Boolean disableUdp;

    @YamlProperty("interface-name")
    private String interfaceName;

    private List<String> use;

}

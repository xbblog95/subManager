package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;


@Data
public class ClashProxyProvidersConfigDto {


    private String type;

    private String url;

    private Integer interval;

    private String strategy;

    private String path;

    @YamlProperty("health-check")
    private ClashProxyProvidersHealthCheckConfigDto healthCheck;
}

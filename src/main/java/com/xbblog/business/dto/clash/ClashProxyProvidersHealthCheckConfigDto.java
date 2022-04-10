package com.xbblog.business.dto.clash;

import lombok.Data;


@Data
public class ClashProxyProvidersHealthCheckConfigDto {


    private Boolean enable;

    private Boolean lazy;

    private Integer interval;

    private String url;

}

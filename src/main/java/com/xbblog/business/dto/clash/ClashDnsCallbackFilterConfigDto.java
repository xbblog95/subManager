package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;


@Data
public class ClashDnsCallbackFilterConfigDto {

    @YamlProperty("geoip")
    private Boolean geoIp;

    @YamlProperty("geoip-code")
    private String geoIpCode;

    @YamlProperty("ipcidr")
    private List<String> ipCidr;

    private List<String> domain;

}

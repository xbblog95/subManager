package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;


@Data
public class ClashDnsCallbackFilterConfigDto {

    @YamlProperty("geoip")
    private Boolean geoIp;

    @YamlProperty("ipcidr")
    private List<String> ipCidr;

    private List<String> domain;

}

package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

@Data
public class ClashProfileConfigDto {

    @YamlProperty("store-selected")
    private Boolean  storeSelected;

    @YamlProperty("store-fake-ip")
    private Boolean storeFakeIp;

    private Boolean tracing;

    public static ClashProfileConfigDto newInstance() {
        ClashProfileConfigDto clashProfileConfigDto = new ClashProfileConfigDto();
        clashProfileConfigDto.setStoreSelected(false);
        clashProfileConfigDto.setStoreFakeIp(true);
        return clashProfileConfigDto;
    }
}

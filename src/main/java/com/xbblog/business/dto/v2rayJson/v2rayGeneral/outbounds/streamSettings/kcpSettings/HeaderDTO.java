package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.kcpSettings;

import lombok.Data;

@Data
public class HeaderDTO {


    private String type;

    public HeaderDTO(String type)
    {
        this.type = type;
    }
}

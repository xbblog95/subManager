package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings;

import lombok.Data;

@Data
public class HeaderDTO {

    private String type;


    public static HeaderDTO getInstance()
    {
        HeaderDTO headerDTO = new HeaderDTO();
        headerDTO.setType("none");
        return headerDTO;
    }
}

package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.kcpSettings.HeaderDTO;
import lombok.Data;

@Data
public class KcpSettingsDTO {

    private int mtu;
    private int tti;
    private int uplinkCapacity;
    private int downlinkCapacity;
    private Boolean congestion;
    private Integer readBufferSize;
    private int writeBufferSize;
    private HeaderDTO header;

    public static KcpSettingsDTO getInstance(String type)
    {
        KcpSettingsDTO kcpSettingsDTO = new KcpSettingsDTO();
        HeaderDTO headerDTO = new HeaderDTO(type);
        kcpSettingsDTO.setHeader(headerDTO);
        return kcpSettingsDTO;
    }

}

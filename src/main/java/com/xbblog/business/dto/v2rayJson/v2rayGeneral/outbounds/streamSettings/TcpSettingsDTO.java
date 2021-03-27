package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.HeaderDTO;
import lombok.Data;

@Data
public class TcpSettingsDTO {

    private HeaderDTO header;

    public static TcpSettingsDTO getInstance(HeaderDTO headerDTO)
    {
        TcpSettingsDTO tcpSettingsDTO = new TcpSettingsDTO();
        tcpSettingsDTO.setHeader(headerDTO);
        return tcpSettingsDTO;
    }
}

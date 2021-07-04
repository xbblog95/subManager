package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.wsSettings.HeadersDTO;
import lombok.Data;

@Data
public class WsSettingsDTO {


    private String path;
    private HeadersDTO headers;


    public static WsSettingsDTO getInstance(String path, String host)
    {
        WsSettingsDTO wsSettingsDTO = new WsSettingsDTO();
        HeadersDTO headerDTO = new HeadersDTO(host);
        wsSettingsDTO.setPath(path);
        wsSettingsDTO.setHeaders(headerDTO);
        return wsSettingsDTO;
    }
}

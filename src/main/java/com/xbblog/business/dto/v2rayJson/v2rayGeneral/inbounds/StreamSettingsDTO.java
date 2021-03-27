package com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds;


import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.streamSettings.WsSettingsDTO;
import lombok.Data;

@Data
public class StreamSettingsDTO {

    private String network;
    private WsSettingsDTO wsSettings;
}

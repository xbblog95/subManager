package com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.wsSettings.ClientsDTO;
import lombok.Data;

import java.util.List;


@Data
public class WsSettingsDTO extends SettingsDTO {


    private Boolean udp;
    private List<ClientsDTO> clients;
    private Boolean allowTransparent;
}

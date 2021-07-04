package com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.socksSettings.AccountsDTO;
import lombok.Data;

import java.util.List;

@Data
public class SocksSettingsDTO extends SettingsDTO{



    private String auth;
    private List<AccountsDTO> accounts;
    private Boolean udp;
    private String ip;
    private Integer userLevel;


    public static SocksSettingsDTO getInstance()
    {
        SocksSettingsDTO socksSettingsDTO = new SocksSettingsDTO();
        socksSettingsDTO.setAuth("noauth");
        socksSettingsDTO.setUdp(true);
        socksSettingsDTO.setIp("127.0.0.1");
        return socksSettingsDTO;
    }

}

package com.xbblog.business.dto.v2rayJson.v2rayGeneral;


import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.HttpSettingsDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.SettingsDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.SocksSettingsDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.inbounds.StreamSettingsDTO;
import lombok.Data;

@Data
public class InboundsDTO {


    private int port;
    private String protocol;
    private SettingsDTO settings;
    private StreamSettingsDTO streamSettings;
    private String tag;
    private String listen;



    public static InboundsDTO getHTTPInstance()
    {
        InboundsDTO inboundsDTO = new InboundsDTO();
        inboundsDTO.setPort(10809);
        inboundsDTO.setProtocol("http");
        inboundsDTO.setTag("http");
        inboundsDTO.setListen("127.0.0.1");
        HttpSettingsDTO httpSettingsDTO = new HttpSettingsDTO();
        inboundsDTO.setSettings(httpSettingsDTO);
        return inboundsDTO;
    }

    public static InboundsDTO getSocksInstance()
    {
        InboundsDTO inboundsDTO = new InboundsDTO();
        inboundsDTO.setPort(10808);
        inboundsDTO.setProtocol("socks");
        inboundsDTO.setTag("socks");
        inboundsDTO.setListen("127.0.0.1");
        SocksSettingsDTO socksSettingsDTO = new SocksSettingsDTO();
        inboundsDTO.setSettings(socksSettingsDTO);
        return inboundsDTO;
    }
}

package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.quicSettings.HeaderDTO;
import lombok.Data;

@Data
public class QuicSettingsDTO {



    private String security;
    private String key;
    private HeaderDTO header;

    public static QuicSettingsDTO getInstance(HeaderDTO header)
    {
        QuicSettingsDTO quicSettingsDTO = new QuicSettingsDTO();
        quicSettingsDTO.setHeader(header);
        quicSettingsDTO.setSecurity("none");
        return quicSettingsDTO;
    }

}

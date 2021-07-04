package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.quicSettings;

import lombok.Data;

@Data
public class HeaderDTO {

    private String type;

    public final static String NONE = "none";

    public final static String SRTP = "srtp";

    public final static String UTP = "utp";

    public final static String WECHATVIDEO = "wechat-video";

    public final static String DTLS = "dtls";

    public final static String WIREGUARD = "wireguard";

    public HeaderDTO(String type)
    {
        this.type = type;
    }
}

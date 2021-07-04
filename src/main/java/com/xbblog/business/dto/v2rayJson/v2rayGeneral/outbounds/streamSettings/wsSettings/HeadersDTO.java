package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.wsSettings;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class HeadersDTO {

    @JSONField(name="Host")
    private String host;

    public final static String NONE = "none";

    public final static String SRTP = "srtp";

    public final static String UTP = "utp";

    public final static String WECHATVIDEO = "wechat-video";

    public final static String DTLS = "dtls";

    public final static String WIREGUARD = "wireguard";

    public HeadersDTO(String host)
    {
        this.host = host;
    }
}

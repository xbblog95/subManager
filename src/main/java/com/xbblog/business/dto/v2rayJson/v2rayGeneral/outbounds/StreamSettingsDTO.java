package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.*;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.HeaderDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.HttpHeaderDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StreamSettingsDTO {

    private String network;
    private String security;
    private TlsSettingsDTO tlsSettings;
    private TcpSettingsDTO tcpSettings;
    private KcpSettingsDTO kcpSettings;
    private WsSettingsDTO wsSettings;
    private HttpSettingsDTO httpSettings;
    private DsSettingsDTO dsSettings;
    private QuicSettingsDTO quicSettings;
    private SockoptDTO sockopt;


    public static StreamSettingsDTO getInstance(String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls)
    {
        StreamSettingsDTO streamSettingsDTO = new StreamSettingsDTO();
        if("tls".equals(camouflageTls))
        {
            streamSettingsDTO.setSecurity("tls");
        }
        switch (network.toLowerCase())
        {
            case "tcp":
            {
                streamSettingsDTO.setNetwork("tcp");
                HeaderDTO header;
                if("none".equals(camouflageType))
                {
                    header = HeaderDTO.getInstance();
                }
                else
                {
                    header = HttpHeaderDTO.getInstance(camouflageType, camouflageHost, camouflagePath);
                }
                streamSettingsDTO.setTcpSettings(TcpSettingsDTO.getInstance(header));
                break;
            }
            case "ws":
            {
                streamSettingsDTO.setNetwork("ws");
                streamSettingsDTO.setWsSettings(WsSettingsDTO.getInstance(camouflagePath, camouflageHost));
                break;
            }
            case "kcp":
            case "mkcp":
            {
                streamSettingsDTO.setNetwork("kcp");
                streamSettingsDTO.setKcpSettings(KcpSettingsDTO.getInstance(camouflageType));
                break;
            }
            case "h2":
            {
                streamSettingsDTO.setNetwork("http");
                List<String> hosts = new ArrayList<>();
                hosts.add(camouflageHost);
                streamSettingsDTO.setHttpSettings(HttpSettingsDTO.getInstance(hosts, camouflagePath));
                break;
            }
            case "quic":
            {
                streamSettingsDTO.setNetwork("http");
                com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.quicSettings.HeaderDTO quicHeaderDto = new com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.quicSettings.HeaderDTO(camouflageType);
                streamSettingsDTO.setQuicSettings(QuicSettingsDTO.getInstance(quicHeaderDto));
                break;
            }
        }
        return streamSettingsDTO;
    }



}

package com.xbblog.business.dto.v2rayJson;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class V2rayGeneral {


    private LogDTO log;
    private List<InboundsDTO> inbounds;
    private List<OutboundsDTO> outbounds;
    private RoutingDTO routing;
    private ApiDTO api;

    public static V2rayGeneral getInstance(String ip, int port, String uuid, int alterId, String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls)
    {
        V2rayGeneral v2rayGeneral = new V2rayGeneral();
        v2rayGeneral.setApi(ApiDTO.getInstance());
        List<String> inboundTags = new ArrayList<>();
        inboundTags.add("http");
        inboundTags.add("socks");
        v2rayGeneral.setRouting(RoutingDTO.getInstance(inboundTags, "out"));
        v2rayGeneral.setLog(LogDTO.getInstance());
        List<InboundsDTO> inboundsDTOS = new ArrayList<>();
        inboundsDTOS.add(InboundsDTO.getHTTPInstance());
        inboundsDTOS.add(InboundsDTO.getSocksInstance());
        v2rayGeneral.setInbounds(inboundsDTOS);
        List<OutboundsDTO> outboundsDTOS = new ArrayList<>();
        outboundsDTOS.add(OutboundsDTO.getVmessInstance(ip, port, uuid, alterId, network, camouflageType, camouflageHost, camouflagePath, camouflageTls));
        v2rayGeneral.setOutbounds(outboundsDTOS);
        return v2rayGeneral;
    }
}

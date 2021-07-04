package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.httpRequest;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HeadersDTO {

    private List<String> Host;
    private List<String> UserAgent;
    private List<String> AcceptEncoding;
    private List<String> Connection;
    private String Pragma;

    public static HeadersDTO getInstance( String camouflageHost)
    {
        HeadersDTO headersDTO = new HeadersDTO();
        List<String> hosts = new ArrayList<>();
        hosts.add(camouflageHost);
        headersDTO.setHost(hosts);
        return headersDTO;
    }
}

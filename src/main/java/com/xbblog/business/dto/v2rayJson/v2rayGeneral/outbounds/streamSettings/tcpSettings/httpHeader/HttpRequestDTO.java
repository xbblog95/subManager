package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.httpRequest.HeadersDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HttpRequestDTO {

    private String version;

    private String method;

    private List<String> path;

    private HeadersDTO headers;


    public static HttpRequestDTO getInstance( String camouflageHost, String camouflagePath)
    {
        HttpRequestDTO requestDTO = new HttpRequestDTO();
        requestDTO.setVersion("1.1");
        requestDTO.setMethod("GET");
        List<String> paths = new ArrayList<String>();
        paths.add(camouflagePath);
        requestDTO.setPath(paths);
        requestDTO.setHeaders(HeadersDTO.getInstance(camouflageHost));
        return requestDTO;
    }

}

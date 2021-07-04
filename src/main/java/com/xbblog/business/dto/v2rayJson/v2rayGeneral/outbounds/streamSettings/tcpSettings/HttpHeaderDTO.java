package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.HTTPResponseDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.HttpRequestDTO;
import lombok.Data;

@Data
public class HttpHeaderDTO extends HeaderDTO {

    private HttpRequestDTO request;

    private HTTPResponseDTO response;


    public static HttpHeaderDTO getInstance(String camouflageType, String camouflageHost, String camouflagePath)
    {
        HttpHeaderDTO headerDTO = new HttpHeaderDTO();
        HTTPResponseDTO httpResponseDTO = HTTPResponseDTO.getInstance();
        HttpRequestDTO requestDTO = HttpRequestDTO.getInstance(camouflageHost, camouflagePath);
        headerDTO.setType(camouflageType);
        headerDTO.setRequest(requestDTO);
        headerDTO.setResponse(httpResponseDTO);
        return headerDTO;

    }
}

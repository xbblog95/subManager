package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.httpResponse.HeadersDTO;
import lombok.Data;

import java.util.List;


@Data
public class HTTPResponseDTO {

    private String version;
    private String status;
    private String reason;
    private HeadersDTO headers;

    public static HTTPResponseDTO getInstance()
    {
        HTTPResponseDTO httpResponseDTO = new HTTPResponseDTO();
        httpResponseDTO.setReason("OK");
        httpResponseDTO.setVersion("1.1");
        httpResponseDTO.setStatus("200");
        return httpResponseDTO;
    }
}

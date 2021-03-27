package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tcpSettings.httpHeader.httpResponse;

import lombok.Data;

import java.util.List;

@Data
public class HeadersDTO {

    private List<String> ContentType;
    private List<String> TransferEncoding;
    private List<String> Connection;
    private String Pragma;
}

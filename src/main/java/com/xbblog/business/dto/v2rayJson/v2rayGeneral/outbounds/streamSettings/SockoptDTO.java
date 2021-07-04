package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import lombok.Data;

@Data
public class SockoptDTO {


    private Integer mark;
    private Boolean tcpFastOpen;
    private String tproxy;
}

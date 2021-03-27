package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings.vnext;

import lombok.Data;

@Data
public class UsersDTO {


    private String id;
    private Integer alterId;
    private String security;
    private Integer level;


    public static UsersDTO getInstance(String uuid, int alterId)
    {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setAlterId(alterId);
        usersDTO.setId(uuid);
        return usersDTO;
    }
}

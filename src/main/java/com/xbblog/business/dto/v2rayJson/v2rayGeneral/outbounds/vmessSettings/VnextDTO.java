package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings.vnext.UsersDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VnextDTO {

    private String address;
    private int port;
    private List<UsersDTO> users;


    public static VnextDTO getInstance(String address, int port, List<UsersDTO> users)
    {
        VnextDTO vnextDTO = new VnextDTO();
        vnextDTO.setAddress(address);
        vnextDTO.setPort(port);
        vnextDTO.setUsers(users);
        return vnextDTO;
    }
}

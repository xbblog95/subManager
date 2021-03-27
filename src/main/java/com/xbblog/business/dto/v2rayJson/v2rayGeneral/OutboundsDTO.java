package com.xbblog.business.dto.v2rayJson.v2rayGeneral;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.*;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings.VnextDTO;
import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.vmessSettings.vnext.UsersDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OutboundsDTO {

    private String sendThrough;
    private String protocol;
    private SettingsDTO settings;
    private String tag;
    private StreamSettingsDTO streamSettings;
    private ProxySettingsDTO proxySettings;
    private MuxDTO mux;

    public static OutboundsDTO getVmessInstance(String ip, int port, String uuid, int alterId, String network, String camouflageType, String camouflageHost, String camouflagePath, String camouflageTls)
    {
        OutboundsDTO outboundsDTO = new OutboundsDTO();
        outboundsDTO.setSendThrough("0.0.0.0");
        outboundsDTO.setProtocol("vmess");
        outboundsDTO.setTag("out");
        //VmessSettingsDto
        List<UsersDTO> users = new ArrayList<UsersDTO>();
        users.add(UsersDTO.getInstance(uuid, alterId));
        List<VnextDTO> vnextDTOS = new ArrayList<>();
        vnextDTOS.add(VnextDTO.getInstance(ip, port, users));
        VmessSettingsDto vmessSettingsDto = new VmessSettingsDto();
        vmessSettingsDto.setVnext(vnextDTOS);
        outboundsDTO.setSettings(vmessSettingsDto);
        outboundsDTO.setStreamSettings(StreamSettingsDTO.getInstance(network, camouflageType, camouflageHost, camouflagePath, camouflageTls));
        outboundsDTO.setMux(MuxDTO.getInstance());
        return outboundsDTO;
    }
}

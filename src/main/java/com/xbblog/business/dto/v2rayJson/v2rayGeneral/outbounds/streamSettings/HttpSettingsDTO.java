package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import lombok.Data;

import java.util.List;

@Data
public class HttpSettingsDTO {

    private List<String> host;
    private String path;

    public static HttpSettingsDTO getInstance(List<String> hosts, String path)
    {
        HttpSettingsDTO httpSettingsDTO = new HttpSettingsDTO();
        httpSettingsDTO.setHost(hosts);
        httpSettingsDTO.setPath(path);
        return httpSettingsDTO;
    }
}

package com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings;

import com.xbblog.business.dto.v2rayJson.v2rayGeneral.outbounds.streamSettings.tlsSettings.CertificateDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TlsSettingsDTO {

    private String serverName;
    private Boolean allowInsecure;
    private Boolean allowInsecureCiphers;
    private List<String> alpn;
    private List<CertificateDTO> certificates;
    private Boolean disableSystemRoot;

    public static TlsSettingsDTO getInstance()
    {
        TlsSettingsDTO tlsSettingsDTO = new TlsSettingsDTO();
        tlsSettingsDTO.setAllowInsecure(false);
        return tlsSettingsDTO;
    }
}

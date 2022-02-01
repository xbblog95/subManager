package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

@Data
public class ClashNodePluginConfigDto {

    private String mode;

    private String host;

    private Boolean tls;

    @YamlProperty("skip-cert-verify")
    private Boolean skipCertVerify;

    private String path;

    private Boolean mux;

    private ClashNodePluginHeaderConfigDto headers;


}

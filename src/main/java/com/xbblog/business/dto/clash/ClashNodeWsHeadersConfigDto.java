package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

@Data
public class ClashNodeWsHeadersConfigDto {

    @YamlProperty("Host")
    private String host;
}

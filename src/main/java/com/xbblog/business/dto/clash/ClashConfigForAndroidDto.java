package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

@Data
public class ClashConfigForAndroidDto {

    @YamlProperty("append-system-dns")
    private Boolean  appendSystemDns;
}

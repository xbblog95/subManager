package com.xbblog.business.dto.clash;

import com.xbblog.base.annotation.YamlProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClashNodeHttpHeaderConfigDto {

    @YamlProperty("Connection")
    private List<String> connection;
}

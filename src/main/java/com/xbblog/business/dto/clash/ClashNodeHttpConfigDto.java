package com.xbblog.business.dto.clash;

import lombok.Data;

import java.util.List;

@Data
public class ClashNodeHttpConfigDto {

    private String method;

    private List<String> path;

    private ClashNodeHttpHeaderConfigDto headers;
}

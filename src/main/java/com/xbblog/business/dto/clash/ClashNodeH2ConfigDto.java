package com.xbblog.business.dto.clash;

import lombok.Data;

import java.util.List;

@Data
public class ClashNodeH2ConfigDto {

    private List<String> host;

    private String path;
}

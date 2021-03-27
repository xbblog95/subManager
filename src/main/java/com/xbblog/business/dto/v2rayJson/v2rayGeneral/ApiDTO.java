package com.xbblog.business.dto.v2rayJson.v2rayGeneral;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApiDTO {


    private String tag;
    private List<String> services;


    public static ApiDTO getInstance()
    {
        ApiDTO apiDTO = new ApiDTO();
        apiDTO.setTag("api");
        List<String> list = new ArrayList<>();
        list.add("HandlerService");
        list.add("LoggerService");
        list.add("StatsService");
        return apiDTO;
    }

}

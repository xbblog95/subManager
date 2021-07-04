package com.xbblog.business.dto.v2rayJson.v2rayGeneral;


import lombok.Data;

@Data
public class LogDTO {


    private String access;
    private String error;
    private String loglevel;

    public static LogDTO getInstance()
    {
        LogDTO logDTO = new LogDTO();
        logDTO.setAccess("");
        logDTO.setError("");
        logDTO.setLoglevel("warning");
        return logDTO;
    }
}

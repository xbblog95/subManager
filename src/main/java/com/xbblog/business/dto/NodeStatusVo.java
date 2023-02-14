package com.xbblog.business.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class NodeStatusVo {

    private Integer id;

    private String name;

    private String location;

    private Double ping;

    private Double tcpPing;

    private Double loss;

    private String nat;

    private Double speed;

    private Double maxSpeed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}

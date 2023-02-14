package com.xbblog.business.dto;

import lombok.Data;

@Data
public class NodeStatus {

    private Integer nodeId;

    private String location;

    private Double ping;

    private Double tcpPing;

    private Double loss;

    private String nat;

    private Double speed;

    private Double maxSpeed;


}

package com.xbblog.business.dto;

import lombok.Data;

@Data
public class NodeGroupKey {

    private int id;

    private Integer groupId;

    private String key;

    private int flag;

    public NodeGroupKey()
    {
        this.flag = 1;
    }
}

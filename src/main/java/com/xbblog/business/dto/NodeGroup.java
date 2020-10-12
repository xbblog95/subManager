package com.xbblog.business.dto;

import lombok.Data;

@Data
public class NodeGroup {

    private int id;

    private String name;

    private int flag;

    private int order;

    public NodeGroup()
    {
        this.flag = 1;
    }
}

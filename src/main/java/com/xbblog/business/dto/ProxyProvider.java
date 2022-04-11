package com.xbblog.business.dto;

import lombok.Data;

@Data
public class ProxyProvider {

    private int id;

    private String url;

    private int flag = 1;

    private String name;
}

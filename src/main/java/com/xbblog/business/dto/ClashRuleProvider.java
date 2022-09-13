package com.xbblog.business.dto;

import lombok.Data;

@Data
public class ClashRuleProvider {

    private Integer id;

    private String name;

    private String type;

    private String behavior;

    private String url;

    private Integer interval;

    private String ruleType;

    private Integer flag;


}

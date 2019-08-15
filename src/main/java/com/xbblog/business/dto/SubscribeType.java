package com.xbblog.business.dto;


public enum SubscribeType {


    SSDSTRING("ssdstring", "ssd字符串"),
    SSSTRING("ssstring", "ss字符串"),
    SSDSUBSCRIBE("ssdsubscribe", "ssd订阅"),
    SSSUBSCRIBE("sssubscribe", "ss订阅"),
    V2RAYNGSUBSCRIBE("v2rayNGsubscribe", "v2rayNG订阅"),
    SSRSUBSCRIBE("ssrsubscribe", "ssr订阅");

    private String code;

    private String name;

    private SubscribeType(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return code;
    }
}

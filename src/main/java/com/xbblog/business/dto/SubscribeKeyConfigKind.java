package com.xbblog.business.dto;

public enum SubscribeKeyConfigKind {

    FilterKey(1),
    ExcludeKey(2);

    private int code;

    private SubscribeKeyConfigKind(int code)
    {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

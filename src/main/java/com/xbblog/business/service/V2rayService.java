package com.xbblog.business.service;

import com.xbblog.business.dto.NodeDto;
import org.springframework.stereotype.Service;
import com.xbblog.utils.StringUtil;

@Service
public class V2rayService {

    public Boolean isKcp(NodeDto nodeDto)
    {
        if(StringUtil.isEmpty(nodeDto.getNetwork()))
        {
            return false;
        }
        return "kcp".equals(nodeDto.getNetwork()) || "mkcp".equals(nodeDto.getNetwork());
    }
}
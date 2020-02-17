package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.handler.NodeHandler;

public class Nu53jNodeHandler implements NodeHandler {
    @Override
    public void beforeInsertDBHandler(NodeBo nodeBo) {
        String remark = nodeBo.getNodeDetail().getRemarks();
        int index = nodeBo.getNodeDetail().getRemarks().indexOf("售后续费网rc.nu53j.cn");
        if(index >= 0)
        {
            nodeBo.getNodeDetail().setRemarks(remark.replaceAll("售后续费网rc.nu53j.cn", ""));
        }
    }

    @Override
    public void beforeSubDBHandler(NodeBo nodeBo) {

    }
}

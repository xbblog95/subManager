package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.handler.NodeHandler;

public class NetflixNodeHandler  implements NodeHandler {
    @Override
    public void beforeInsertDBHandler(NodeBo nodeBo) {
        nodeBo.getNodeDetail().setRemarks(nodeBo.getNodeDetail().getRemarks() + "-Netflix");
    }

    @Override
    public void beforeSubDBHandler(NodeBo nodeBo) {

    }
}

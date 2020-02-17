package com.xbblog.business.handler;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.dto.NodeDetail;

public interface NodeHandler {


    /**
     * 入库之前的动作
     * @param nodeBo
     */
    public void beforeInsertDBHandler(NodeBo nodeBo);


    /**
     * 订阅时的动作
     * @param nodeBo
     */
    public void beforeSubDBHandler(NodeBo nodeBo);
}

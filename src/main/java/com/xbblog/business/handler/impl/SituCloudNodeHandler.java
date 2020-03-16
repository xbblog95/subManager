package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.handler.NodeHandler;
import com.xbblog.utils.EmojiUtil;

public class SituCloudNodeHandler implements NodeHandler {
    @Override
    public void beforeInsertDBHandler(NodeBo nodeBo) {
        String remark = nodeBo.getNodeDetail().getRemarks();
        nodeBo.getNodeDetail().setRemarks(EmojiUtil.filterEmoji(remark));
    }

    @Override
    public void beforeSubDBHandler(NodeBo nodeBo) {

    }
}

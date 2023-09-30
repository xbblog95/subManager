package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.NodeDto;
import com.xbblog.business.dto.NodeStatus;
import com.xbblog.business.handler.StairSpeedTestMonitorNodeHandler;
import com.xbblog.business.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("noMonitorNodeHandlerImpl")
public class NoMonitorNodeHandlerImpl extends StairSpeedTestMonitorNodeHandler {

    @Autowired
    private NodeService nodeService;

    @Override
    public void monitor(List<NodeDto> nodes) {
        for(NodeDto nodeDto : nodes)
        {
            NodeStatus nodeStatus = new NodeStatus();
            nodeStatus.setNodeId(nodeDto.getNodeId());
            nodeStatus.setTcpPing(1D);
            nodeService.updateNodeStatus(nodeStatus);
        }
    }
}

package com.xbblog.business.handler;

import com.xbblog.business.dto.NodeDto;
import com.xbblog.business.dto.NodeStatus;
import com.xbblog.business.handler.impl.StairSpeedTestActiveMonitorNodeHandlerImpl;
import com.xbblog.business.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service("stairSpeedTestBaseMonitorNodeHandler")
public abstract class StairSpeedTestMonitorNodeHandler {

    protected static Logger logger = LoggerFactory.getLogger(StairSpeedTestMonitorNodeHandler.class);

    @Autowired
    private NodeService nodeService;

    public static ReentrantLock lock = new ReentrantLock();
    public abstract void monitor(final List<NodeDto> nodes);

    public void updateNodeStatus(NodeDto node, NodeStatus nodeStatus)
    {
        try
        {
            nodeStatus.setNodeId(node.getNodeId());
            nodeService.updateNodeStatus(nodeStatus);
        }
        catch (Exception e)
        {
            logger.error("update status error ");
        }

    }
}

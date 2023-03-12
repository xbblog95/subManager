package com.xbblog.business.websocket;

import com.alibaba.fastjson.JSONObject;
import com.xbblog.business.dto.NodeDto;
import com.xbblog.business.dto.NodeStatus;
import com.xbblog.business.handler.StairSpeedTestMonitorNodeHandler;
import com.xbblog.business.handler.impl.StairSpeedTestActiveMonitorNodeHandlerImpl;
import com.xbblog.business.handler.impl.StairSpeedTestSpeedMonitorNodeHandlerImpl;
import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;


public class StairSpeedTestClient extends WebSocketClient {


    private static Logger logger = LoggerFactory.getLogger(StairSpeedTestClient.class);

    private CountDownLatch latch = new CountDownLatch(1);;

    private StairSpeedTestMonitorNodeHandler stairSpeedTestMonitorNodeHandler;

    private NodeDto nodeDto;

    private NodeStatus nodeStatus = new NodeStatus();

    public StairSpeedTestClient(String url, NodeDto nodeDto, StairSpeedTestMonitorNodeHandler stairSpeedTestMonitorNodeHandler) throws URISyntaxException {
        super(new URI(url));
        this.nodeDto = nodeDto;
        this.stairSpeedTestMonitorNodeHandler = stairSpeedTestMonitorNodeHandler;
        nodeStatus.setNodeId(nodeDto.getNodeId());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        logger.info("节点id {} 名称 {} 即将检测", nodeDto.getNodeId(), nodeDto.getRemarks());
    }

    @Override
    public void onMessage(String s) {
        logger.info("节点id {} 名称 {} 收到消息 {}", nodeDto.getNodeId(), nodeDto.getRemarks(), s);
        if(StringUtils.isEmpty(s))
        {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(s);
        if("eof".equals(jsonObject.getString("info")))
        {
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, nodeStatus);
            return;
        }
        if("gotping".equals(jsonObject.getString("info")))
        {
            nodeStatus.setPing(jsonObject.getDouble("ping"));
            String loss = jsonObject.getString("loss");
            if(loss.endsWith("%"))
            {
                nodeStatus.setLoss(Double.parseDouble(loss.substring(0, loss.lastIndexOf("%"))));
            }
            return;
        }
        if("gotgeoip".equals(jsonObject.getString("info")))
        {
            nodeStatus.setLocation(jsonObject.getString("location"));
            return;
        }
        if("gotnat".equals(jsonObject.getString("info")))
        {
            nodeStatus.setNat(jsonObject.getString("result"));
            return;
        }
        if("gotgping".equals(jsonObject.getString("info")))
        {
            nodeStatus.setTcpPing(jsonObject.getDouble("ping"));
            return;
        }
        if("gotspeed".equals(jsonObject.getString("info")))
        {
            String speed = jsonObject.getString("speed");
            String maxSpeed = jsonObject.getString("maxspeed");
            //本次做的是速度检测,并且测出的速度是N/A
            if(stairSpeedTestMonitorNodeHandler instanceof StairSpeedTestSpeedMonitorNodeHandlerImpl && "N/A".equals(speed))
            {
                nodeStatus.setIsSetSpeed(true);
            }
            //本次做的是速度检测,并且测出的速度是N/A
            else if(stairSpeedTestMonitorNodeHandler instanceof StairSpeedTestSpeedMonitorNodeHandlerImpl && "N/A".equals(maxSpeed))
            {
                nodeStatus.setIsSetMaxSpeed(true);
            }
            //本次做的是TCP检测,测出的速度必定是N/A
            else if(stairSpeedTestMonitorNodeHandler instanceof StairSpeedTestActiveMonitorNodeHandlerImpl && ("N/A".equals(speed) || "N/A".equals(maxSpeed)))
            {
                return;
            }
            nodeStatus.setSpeed(formatSpeed(speed));
            nodeStatus.setIsSetSpeed(true);
            nodeStatus.setMaxSpeed(formatSpeed(maxSpeed));
            nodeStatus.setIsSetMaxSpeed(true);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("节点id {} 名称 {} 检测完成", nodeDto.getNodeId(), nodeDto.getRemarks());
        stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, nodeStatus);
        latch.countDown();
    }

    @Override
    public void onError(Exception e) {
        logger.info("节点id {} 名称 {} 检测完成 发生异常", nodeDto.getNodeId(), nodeDto.getRemarks());
        stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, nodeStatus);
        latch.countDown();
    }


    public void send(String text) {
        try
        {
            super.send(text);
        }
        catch (Exception e)
        {
            if(super.isOpen())
            {
                super.close();
            }
            throw e;
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Double formatSpeed(String speed)
    {
        if(StringUtils.isEmpty(speed))
        {
            return 0.00;
        }
        if(speed.endsWith("KB"))
        {
            return Double.parseDouble(speed.substring(0, speed.lastIndexOf("KB")));
        }
        if(speed.endsWith("MB"))
        {
            double mb = Double.parseDouble(speed.substring(0, speed.lastIndexOf("MB")));
            return new BigDecimal(mb).multiply(new BigDecimal(1024)).doubleValue();
        }
        if(speed.endsWith("GB"))
        {
            double mb = Double.parseDouble(speed.substring(0, speed.lastIndexOf("GB")));
            return new BigDecimal(mb).multiply(new BigDecimal(1024 * 1024)).doubleValue();
        }
        return 0.00;

    }
}

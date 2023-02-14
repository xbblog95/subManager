package com.xbblog.business.websocket;

import com.alibaba.fastjson.JSONObject;
import com.xbblog.business.dto.NodeDto;
import com.xbblog.business.dto.NodeStatus;
import com.xbblog.business.handler.impl.StairSpeedTestMonitorNodeHandlerImpl;
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

    private StairSpeedTestMonitorNodeHandlerImpl stairSpeedTestMonitorNodeHandler;

    private NodeDto nodeDto;

    public StairSpeedTestClient(String url, NodeDto nodeDto, StairSpeedTestMonitorNodeHandlerImpl stairSpeedTestMonitorNodeHandler) throws URISyntaxException {
        super(new URI(url));
        this.nodeDto = nodeDto;
        this.stairSpeedTestMonitorNodeHandler = stairSpeedTestMonitorNodeHandler;
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
            latch.countDown();
        }
        NodeStatus status = new NodeStatus();
        status.setNodeId(nodeDto.getNodeId());
        if("gotping".equals(jsonObject.getString("info")))
        {
            status.setPing(jsonObject.getDouble("ping"));
            String loss = jsonObject.getString("loss");
            if(loss.endsWith("%"))
            {
                status.setLoss(Double.parseDouble(loss.substring(0, loss.lastIndexOf("%"))));
            }
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, status);
            return;
        }
        if("gotgeoip".equals(jsonObject.getString("info")))
        {
            status.setLocation(jsonObject.getString("location"));
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, status);
            return;
        }
        if("gotnat".equals(jsonObject.getString("info")))
        {
            status.setNat(jsonObject.getString("result"));
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, status);
            return;
        }
        if("gotgping".equals(jsonObject.getString("info")))
        {
            status.setTcpPing(jsonObject.getDouble("ping"));
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, status);
            return;
        }
        if("gotspeed".equals(jsonObject.getString("info")))
        {
            status.setSpeed(formatSpeed(jsonObject.getString("speed"))); ;
            status.setMaxSpeed(formatSpeed(jsonObject.getString("maxspeed"))); ;
            stairSpeedTestMonitorNodeHandler.updateNodeStatus(nodeDto, status);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        logger.info("节点id {} 名称 {} 检测完成", nodeDto.getNodeId(), nodeDto.getRemarks());
        latch.countDown();
    }

    @Override
    public void onError(Exception e) {
        logger.info("节点id {} 名称 {} 检测完成 发生异常", nodeDto.getNodeId(), nodeDto.getRemarks());
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

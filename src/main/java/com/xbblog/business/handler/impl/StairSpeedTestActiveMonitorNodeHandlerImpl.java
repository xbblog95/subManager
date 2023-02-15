package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.*;
import com.xbblog.business.handler.StairSpeedTestMonitorNodeHandler;
import com.xbblog.business.websocket.StairSpeedTestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("stairSpeedTestActiveMonitorNodeHandler")
public class StairSpeedTestActiveMonitorNodeHandlerImpl extends StairSpeedTestMonitorNodeHandler {

    private static Logger logger = LoggerFactory.getLogger(StairSpeedTestActiveMonitorNodeHandlerImpl.class);


    @Value("${node.monitor.websocket.url}")
    public String monitorWsUrl;

    public static String SPEEDTESTMODE = "pingonly";

    @Value("${node.monitor.websocket.pingMethod}")
    public String pingMethod;




    @PostConstruct
    public void init() throws URISyntaxException, InterruptedException {
    }



//    @Override
//    public NodeDto monitor(NodeDto node) {
//        boolean locked = LOCK.tryLock();
//        if(locked)
//        {
//            weClient.send();
//        }
//        return null;
//    }java.util.concurrent.locks.ReentrantLock@6664bd10[Locked by thread http-nio-8080-exec-9]

    @Override
    public synchronized void monitor(List<NodeDto> nodes) {
        boolean locked = false;
        try {
            locked = lock.tryLock(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!locked)
        {
            logger.info("有其他任务正在执行，忽略本次调度");
        }
        try
        {
            for(NodeDto nodeDto : nodes)
            {
                String str ="";
                //转换为标准格式
                String type = nodeDto.getType();
                if("ss".equals(type))
                {
                    ShadowsocksNode shadowsocksNode = ShadowsocksNode.toShadowsocksNode(nodeDto);
                    str = ShadowsocksNode.parseToV2rayNgString(shadowsocksNode);
                } else if ("ssr".equals(type)) {
                    ShadowsocksRNode shadowsocksRNode = ShadowsocksRNode.toShadowsocksNodeR(nodeDto);
                    str = ShadowsocksRNode.parseToShadowsocksRString(shadowsocksRNode);
                }
                else if ("v2ray".equals(type)) {
                    V2rayNodeDetail v2rayNodeDetail = V2rayNodeDetail.toV2rayDetail(nodeDto);
                    str = V2rayNodeDetail.parseToV2rayNgString(v2rayNodeDetail);
                }
                else if ("trojan".equals(type)) {
                    str = TrojanNode.parseToV2rayNgString(nodeDto);
                }
                try
                {
                    StringBuffer builder = new StringBuffer(str).append("^?empty?^").append(SPEEDTESTMODE).append("^").append(pingMethod).append("^none^false");
                    StairSpeedTestClient wsClient = new StairSpeedTestClient(monitorWsUrl, nodeDto, this);
                    wsClient.connectBlocking();
                    wsClient.send(builder.toString());
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    logger.error("对节点{}执行检查失败，访问后端失败", nodeDto.getRemarks());
                }
            }
        }
        catch (Exception e)
        {
            logger.error("对Active任务执行检查失败");
        }
        finally {
            lock.unlock();
        }


    }


}

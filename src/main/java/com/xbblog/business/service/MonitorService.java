package com.xbblog.business.service;

import com.xbblog.business.dto.*;
import com.xbblog.business.handler.impl.StairSpeedTestActiveMonitorNodeHandlerImpl;
import com.xbblog.business.handler.impl.StairSpeedTestSpeedMonitorNodeHandlerImpl;
import com.xbblog.business.handler.impl.TelnetMonitorNodeHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.util.*;

@Service
public class MonitorService {

    private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private NodeService nodeService;



    @Autowired
    private TelnetMonitorNodeHandlerImpl telnetMonitorNodeHandler;


    @Autowired
    private StairSpeedTestActiveMonitorNodeHandlerImpl stairSpeedTestActiveMonitorNodeHandler;

    @Autowired
    private StairSpeedTestSpeedMonitorNodeHandlerImpl stairSpeedTestSpeedMonitorNodeHandler;

    @Transactional
    public void testActiveActive(TestActiveReqDto reqDto)
    {
        logger.info("开始检测服务器存活");
        Map<String, Object> paramMap = new HashMap<>();
        if(reqDto.getNodeId() != null)
        {
            paramMap.put("nodeId", reqDto.getNodeId());
        }
        if(reqDto.getSubscribeId() != null)
        {
            paramMap.put("subscribeId", reqDto.getSubscribeId());
        }
        List<NodeDto> list = new ArrayList<>();
        //检查ss节点信息
        List<NodeDto> shadowsocksNodes = nodeService.getShadowsocksNodes(paramMap);
        list.addAll(shadowsocksNodes);
        //检查v2ray节点信息
        List<NodeDto> v2rayNodes = nodeService.getV2rayNodes(paramMap);
        list.addAll(v2rayNodes);
        //检查ssr节点信息
        List<NodeDto> ssrNode = nodeService.getShadowsocksRNodes(paramMap);
        list.addAll(ssrNode);
        //检查trojan节点信息
        List<NodeDto> trojanNodes = nodeService.getTrojanNodes(paramMap);
        list.addAll(trojanNodes);
        monitorActive(list);
//        if(!CollectionUtils.isEmpty(failList))
//        {
//            //发送邮件
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("date", new Date());
//            map.put("list", failList);
//            try {
//                emailService.sendOne(toAddress,  "mail.ftl", map, "服务器异常提醒");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        logger.info("服务器存活检测任务提交成功");
    }


    @Transactional
    public void testActiveSpeed(TestActiveReqDto reqDto)
    {
        logger.info("开始检测服务器存活");
        Map<String, Object> paramMap = new HashMap<>();
        if(reqDto.getNodeId() != null)
        {
            paramMap.put("nodeId", reqDto.getNodeId());
        }
        if(reqDto.getSubscribeId() != null)
        {
            paramMap.put("subscribeId", reqDto.getSubscribeId());
        }
        List<NodeDto> list = new ArrayList<>();
        //检查ss节点信息
        List<NodeDto> shadowsocksNodes = nodeService.getShadowsocksNodes(paramMap);
        list.addAll(shadowsocksNodes);
        //检查v2ray节点信息
        List<NodeDto> v2rayNodes = nodeService.getV2rayNodes(paramMap);
        list.addAll(v2rayNodes);
        //检查ssr节点信息
        List<NodeDto> ssrNode = nodeService.getShadowsocksRNodes(paramMap);
        list.addAll(ssrNode);
        //检查trojan节点信息
        List<NodeDto> trojanNodes = nodeService.getTrojanNodes(paramMap);
        list.addAll(trojanNodes);
        monitorSpeed(list);
        logger.info("服务器存活检测结束");
    }

    @Transactional
    public void testActiveTelnet(TestActiveReqDto reqDto)
    {
        logger.info("开始检测服务器存活");
        Map<String, Object> paramMap = new HashMap<>();
        if(reqDto.getNodeId() != null)
        {
            paramMap.put("nodeId", reqDto.getNodeId());
        }
        if(reqDto.getSubscribeId() != null)
        {
            paramMap.put("subscribeId", reqDto.getSubscribeId());
        }
        List<NodeDto> list = new ArrayList<>();
        //检查ss节点信息
        List<NodeDto> shadowsocksNodes = nodeService.getShadowsocksNodes(paramMap);
        list.addAll(shadowsocksNodes);
        //检查v2ray节点信息
        List<NodeDto> v2rayNodes = nodeService.getV2rayNodes(paramMap);
        list.addAll(v2rayNodes);
        //检查ssr节点信息
        List<NodeDto> ssrNode = nodeService.getShadowsocksRNodes(paramMap);
        list.addAll(ssrNode);
        //检查trojan节点信息
        List<NodeDto> trojanNodes = nodeService.getTrojanNodes(paramMap);
        list.addAll(trojanNodes);
        monitorTelnet(list);
        logger.info("服务器存活检测结束");
    }


    private void monitorTelnet(List<NodeDto> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return ;
        }
        telnetMonitorNodeHandler.monitor(nodes);
    }

    private void monitorActive(List<NodeDto> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return ;
        }
        stairSpeedTestActiveMonitorNodeHandler.monitor(nodes);
    }

    private void monitorSpeed(List<NodeDto> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return ;
        }
        stairSpeedTestSpeedMonitorNodeHandler.monitor(nodes);
    }

}
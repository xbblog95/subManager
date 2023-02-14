package com.xbblog.business.service;

import com.xbblog.base.service.EmailService;
import com.xbblog.business.dto.*;
import com.xbblog.business.handler.MonitorNodeHandler;
import com.xbblog.utils.MonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class MonitorService {

    private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private NodeService nodeService;

//    @Autowired
//    private EmailService emailService;
//
//
//
//    @Value("${monitor.email.address}")
//    private String toAddress;


    @Autowired
    private MonitorNodeHandler monitorNodeHandler;


    @Transactional
    public void testActive()
    {
        logger.info("开始检测服务器存活");
        //检查ss节点信息
        List<NodeDto> shadowsocksNodes = nodeService.getAllShadowsocksNodes();
        monitor(shadowsocksNodes);
        //检查v2ray节点信息
        List<NodeDto> v2rayNodes = nodeService.getV2rayNodes();
        monitor(v2rayNodes);
        //检查ssr节点信息
        List<NodeDto> ssrNode = nodeService.getAllShadowsocksRNodes();
        monitor(ssrNode);
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


    private void monitor(List<NodeDto> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return ;
        }
        monitorNodeHandler.monitor(nodes);

    }

}
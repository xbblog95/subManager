package com.xbblog.business.service;

import com.xbblog.base.service.EmailService;
import com.xbblog.business.dto.*;
import com.xbblog.utils.MonitorUtils;
import com.xbblog.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Service
public class MonitorService {

    private static Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private V2rayService v2rayService;

    @Value("${monitor.email.address}")
    private String toAddress;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        executorService = new ThreadPoolExecutor(20, 100, 1, TimeUnit.SECONDS,  new LinkedBlockingQueue<Runnable>());
    }
    @Transactional
    public void testActive()
    {
        logger.info("开始检测服务器存活");
        //检查ss节点信息
        List<NodeDto> shadowsocksNodes = nodeService.getAllShadowsocksNodes();
        //发送邮件的失败列表
        List<NodeDetail> failList = new ArrayList<NodeDetail>();
        failList.addAll(monitor(shadowsocksNodes));
        //检查v2ray节点信息
        List<NodeDto> v2rayNodes = nodeService.getV2rayNodes();
        failList.addAll(monitor(v2rayNodes));
        //检查ssr节点信息
        List<NodeDto> ssrNode = nodeService.getAllShadowsocksRNodes();
        failList.addAll(monitor(ssrNode));
        if(!CollectionUtils.isEmpty(failList))
        {
            //发送邮件
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("date", new Date());
            map.put("list", failList);
            try {
                emailService.sendOne(toAddress,  "mail.ftl", map, "服务器异常提醒");
            } catch (IOException e) {
                e.printStackTrace();
            }  catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        logger.info("服务器存活检测结束");
    }


    private List<NodeDetail> monitor(List<NodeDto> nodes)
    {
        //发送邮件的失败列表
        List<NodeDetail> failList = new ArrayList<NodeDetail>();
        if(CollectionUtils.isEmpty(nodes))
        {
            return failList;
        }
        List<Future> threadList = new ArrayList<Future>();
        for(final NodeDto node : nodes)
        {
            Future<NodeDto> submit = executorService.submit(new Callable() {
                @Override
                public NodeDto call() throws Exception {
                    NodeDto result = null;
                    try
                    {
                        result = monitor(node);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return result;
                }
            });
            threadList.add(submit);
        }
        for(Future future : threadList)
        {
            try
            {
                if(future.get() != null)
                {
                    failList.add((NodeDto)future.get());
                }
            }
            catch (Exception e)
            {

            }

        }
        return failList;
    }

    private NodeDto monitor(final NodeDto node)
    {
        //使用了kcp协议的不检测
        if (v2rayService.isKcp(node)) {
            return null;
        }
        String host = node.getIp();
        int port = node.getPort();
        logger.info(String.format("正在检测服务器ip：%s, 端口：%d", host, port));
        Boolean isActive = MonitorUtils.testTCPActive(host, port) != 0;
        if (isActive != (node.getFlag() == 1))
        {
            node.setFlag(isActive ? 1 : 0);
            Node modNode = new Node(node.getId(), node.getSource(), isActive ? 1 : 0, node.getSubscribeId());
            nodeService.modNode(modNode);
        }
        if (isActive)
        {
            logger.info(String.format("服务器ip：%s, 端口：%d正常", host, port));
            return null;
        }
        else
        {
            logger.warn(String.format("服务器ip：%s, 端口：%d检测不通过", host, port));
            if ("own".equals(node.getSource())) {
                return node;
            }
            return null;
        }
    }
}
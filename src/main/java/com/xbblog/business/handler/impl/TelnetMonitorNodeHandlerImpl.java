package com.xbblog.business.handler.impl;

import com.xbblog.business.dto.Node;
import com.xbblog.business.dto.NodeDto;
import com.xbblog.business.service.NodeService;
import com.xbblog.business.service.V2rayService;
import com.xbblog.utils.MonitorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service("telnetMonitorNodeHandler")
public class TelnetMonitorNodeHandlerImpl {

    private static Logger logger = LoggerFactory.getLogger(TelnetMonitorNodeHandlerImpl.class);

    @Autowired
    private NodeService nodeService;

    @Autowired
    private V2rayService v2rayService;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        executorService = new ThreadPoolExecutor(20, 100, 1, TimeUnit.SECONDS,  new LinkedBlockingQueue<Runnable>());
    }

    public NodeDto monitor(NodeDto node) {
        //使用了kcp协议的不检测
        if (v2rayService.isKcp(node)) {
            return null;
        }
        String host = node.getIp();
        int port = node.getPort();
        logger.info("正在检测服务器ip：{}, 端口：{}", host, port);
        Boolean isActive = MonitorUtils.testTCPActive(host, port) != 0;
        if (isActive != (node.getFlag() == 1))
        {
            node.setFlag(isActive ? 1 : 0);
            Node modNode = new Node(node.getId(), node.getSource(), isActive ? 1 : 0, node.getSubscribeId());
            nodeService.modNode(modNode);
        }
        if (isActive)
        {
            logger.info("服务器ip：{}, 端口：{}正常", host, port);
            return null;
        }
        else
        {
            logger.warn("服务器ip：{}, 端口：{}检测不通过", host, port);
            if ("own".equals(node.getSource())) {
                return node;
            }
            return null;
        }
    }

    public void monitor(List<NodeDto> nodes) {
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
                future.get(30, TimeUnit.SECONDS);
            }
            catch (InterruptedException e)
            {

            }
            catch (Exception e)
            {

            }
        }
        logger.info("服务器存活检测结束");
    }
}

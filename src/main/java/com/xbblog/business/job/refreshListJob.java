package com.xbblog.business.job;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletContext;
import java.util.List;

@Repository
public class refreshListJob {


    @Autowired
    private NodeService nodeService;

    public Logger logger = LoggerFactory.getLogger(refreshListJob.class);


    @Autowired
    private ServletContext servletContext;
    public void refreshList() throws Exception {
        logger.info("开始更新订阅");
        List<NodeBo> list = nodeService.getAllssLink();
        nodeService.insertAll(list);
        logger.info("更新订阅完成");
    }
}
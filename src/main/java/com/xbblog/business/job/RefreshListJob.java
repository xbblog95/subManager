package com.xbblog.business.job;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.service.NodeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Repository;

import javax.servlet.ServletContext;
import java.util.List;

@Repository
public class RefreshListJob extends QuartzJobBean {


    @Autowired
    private NodeService nodeService;

    public Logger logger = LoggerFactory.getLogger(RefreshListJob.class);


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("开始更新订阅");
        List<NodeBo> list = null;
        try {
            list = nodeService.getAllssLink();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        nodeService.insertAll(list);
        logger.info("更新订阅完成");
    }
}
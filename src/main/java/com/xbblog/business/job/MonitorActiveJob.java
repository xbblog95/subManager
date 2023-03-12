package com.xbblog.business.job;

import com.xbblog.business.dto.NodeBo;
import com.xbblog.business.dto.TestActiveReqDto;
import com.xbblog.business.service.MonitorService;
import com.xbblog.business.service.NodeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MonitorActiveJob extends QuartzJobBean {


    @Autowired
    private NodeService nodeService;

    @Autowired
    private MonitorService monitorService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<NodeBo> list = null;
        try {
            list = nodeService.getAllssLink();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        nodeService.insertAll(list);
        monitorService.testActiveActive(new TestActiveReqDto());
    }
}

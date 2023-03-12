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
public class MonitorSpeedJob extends QuartzJobBean {


    @Autowired
    private NodeService nodeService;

    @Autowired
    private MonitorService monitorService;


    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        monitorService.testActiveSpeed(new TestActiveReqDto());
    }
}

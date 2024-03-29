package com.xbblog.config;

import com.xbblog.business.job.MonitorActiveJob;
import com.xbblog.business.job.MonitorSpeedJob;
import com.xbblog.business.job.RefreshListJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {

    @Bean
    public JobDetail monitorActiveJobDetail(){
        return JobBuilder.newJob(MonitorActiveJob.class)//PrintTimeJob我们的业务类
                .withIdentity("monitorActiveJob")//可以给该JobDetail起一个id
                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
                .build();
    }

    @Bean
    public Trigger monitorActiveQuartzTrigger(){
        CronScheduleBuilder  scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 4,8,12,16,20,0 ? * *");
        return TriggerBuilder.newTrigger().forJob(monitorActiveJobDetail())
                .withIdentity("monitorActiveTrigger")
                .withSchedule(scheduleBuilder)
                .startNow()
                .build();
    }

//    @Bean
//    public JobDetail refreshListJobDetail(){
//        return JobBuilder.newJob(RefreshListJob.class)//PrintTimeJob我们的业务类
//                .withIdentity("refreshListJob")//可以给该JobDetail起一个id
//                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
//                .build();
//    }


//    @Bean
//    public Trigger refreshListQuartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(3600)  //设置时间周期单位秒
//                .repeatForever();
//        return TriggerBuilder.newTrigger().forJob(refreshListJobDetail())
//                .withIdentity("refreshListTrigger")
//                .withSchedule(scheduleBuilder)
//                .build();
//    }

//    @Bean
//    public JobDetail monitorSpeedJobDetail(){
//        return JobBuilder.newJob(MonitorSpeedJob.class)//PrintTimeJob我们的业务类
//                .withIdentity("monitorSpeedJob")//可以给该JobDetail起一个id
//                .storeDurably()//即使没有Trigger关联时，也不需要删除该JobDetail
//                .build();
//    }
//
//    @Bean
//    public Trigger monitorSpeedQuartzTrigger(){
//        CronScheduleBuilder  scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 18 ? * *");
//        return TriggerBuilder.newTrigger().forJob(monitorSpeedJobDetail())
//                .withIdentity("monitorSpeedTrigger")
//                .withSchedule(scheduleBuilder)
//                .startNow()
//                .build();
//    }
}

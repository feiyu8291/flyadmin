package com.fly.business.modules.quartz.job;

import com.fly.business.modules.quartz.model.entity.SysQuartzJob;
import com.fly.business.modules.quartz.tool.JobInvokeUtil;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysQuartzJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}

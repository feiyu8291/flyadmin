package com.fly.business.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.quartz.job.QuartzDisallowConcurrentExecution;
import com.fly.business.modules.quartz.job.QuartzJobExecution;
import com.fly.business.modules.quartz.mapper.SysQuartzJobMapper;
import com.fly.business.modules.quartz.model.entity.SysQuartzJob;
import com.fly.common.constant.ScheduleConstants;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 */
@Service
public class SysQuartzJobService extends ServiceImpl<SysQuartzJobMapper, SysQuartzJob> {

    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     */
    @PostConstruct
    public void init() throws SchedulerException {
        List<SysQuartzJob> jobList = baseMapper.selectList(null);
        for (SysQuartzJob job : jobList) {
            updateSchedulerJob(job, job.getJobGroup());
        }
    }

    /**
     * 校验cron表达式是否有效
     */
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertJob(SysQuartzJob job) throws SchedulerException {
        job.setJobStatus(ScheduleConstants.Status.PAUSE.getValue());
        baseMapper.insert(job);
        createSchedulerJob(job);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateJob(SysQuartzJob job) throws SchedulerException {
        SysQuartzJob properties = baseMapper.selectById(job.getJobId());
        baseMapper.updateById(job);
        updateSchedulerJob(job, properties.getJobGroup());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteJob(SysQuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        baseMapper.deleteById(jobId);
        scheduler.deleteJob(getJobKey(jobId, jobGroup));
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            SysQuartzJob job = baseMapper.selectById(jobId);
            deleteJob(job);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(SysQuartzJob job) throws SchedulerException {
        String status = job.getJobStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            resumeJob(job);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            pauseJob(job);
        }
        baseMapper.updateById(job);
    }

    @Transactional(rollbackFor = Exception.class)
    public void run(SysQuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        SysQuartzJob properties = baseMapper.selectById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(getJobKey(jobId, jobGroup), dataMap);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pauseJob(SysQuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        scheduler.pauseJob(getJobKey(jobId, jobGroup));
    }

    @Transactional(rollbackFor = Exception.class)
    public void resumeJob(SysQuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        scheduler.resumeJob(getJobKey(jobId, jobGroup));
    }

    /**
     * 更新任务
     */
    public void updateSchedulerJob(SysQuartzJob job, String jobGroup) throws SchedulerException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        createSchedulerJob(job);
    }

    /**
     * 创建定时任务
     */
    public void createSchedulerJob(SysQuartzJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(getJobClass(job)).withIdentity(getJobKey(jobId, jobGroup)).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // 暂停任务
        if (job.getJobStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 设置定时任务策略
     */
    public CronScheduleBuilder handleCronScheduleMisfirePolicy(SysQuartzJob job, CronScheduleBuilder cb) {
        switch (job.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                return cb;
        }
    }

    /**
     * 获取quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getJobClass(SysQuartzJob sysJob) {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }
}

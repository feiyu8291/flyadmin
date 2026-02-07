package com.fly.business.modules.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.business.modules.quartz.model.entity.SysQuartzJob;
import com.fly.business.modules.quartz.service.SysQuartzJobService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 调度任务信息操作处理
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController {

    @Autowired
    private SysQuartzJobService jobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    public ResponseData<Page<SysQuartzJob>> list(SysQuartzJob sysJob,
                                                 @RequestParam(defaultValue = "1") Integer pageNum,
                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<SysQuartzJob> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysQuartzJob> wrapper = new LambdaQueryWrapper<>();
        if (sysJob.getJobName() != null) {
            wrapper.like(SysQuartzJob::getJobName, sysJob.getJobName());
        }
        if (sysJob.getJobGroup() != null) {
            wrapper.eq(SysQuartzJob::getJobGroup, sysJob.getJobGroup());
        }
        if (sysJob.getJobStatus() != null) {
            wrapper.eq(SysQuartzJob::getJobStatus, sysJob.getJobStatus());
        }
        Page<SysQuartzJob> result = jobService.page(page, wrapper);
        return ResponseData.success(result);
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "/{jobId}")
    public ResponseData<SysQuartzJob> getInfo(@PathVariable("jobId") Long jobId) {
        SysQuartzJob job = jobService.getById(jobId);
        if (job == null) {
            throw new ResponseException(404, "任务不存在");
        }
        return ResponseData.success(job);
    }

    /**
     * 新增定时任务
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysQuartzJob job) throws Exception {
        if (!jobService.checkCronExpressionIsValid(job.getCronExpression())) {
            throw new ResponseException("Cron表达式不正确");
        }
        jobService.insertJob(job);
        return ResponseData.success("新增成功");
    }

    /**
     * 修改定时任务
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysQuartzJob job) throws Exception {
        if (!jobService.checkCronExpressionIsValid(job.getCronExpression())) {
            throw new ResponseException("Cron表达式不正确");
        }
        jobService.updateJob(job);
        return ResponseData.success("修改成功");
    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public ResponseData<Void> changeStatus(@RequestBody SysQuartzJob job) throws Exception {
        SysQuartzJob newJob = jobService.getById(job.getJobId());
        if (newJob == null) {
            throw new ResponseException(404, "任务不存在");
        }
        newJob.setJobStatus(job.getJobStatus());
        jobService.changeStatus(newJob);
        return ResponseData.success("修改成功");
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public ResponseData<Void> run(@RequestBody SysQuartzJob job) throws Exception {
        jobService.run(job);
        return ResponseData.success("执行成功");
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobIds}")
    public ResponseData<Void> remove(@PathVariable Long[] jobIds) throws Exception {
        jobService.deleteJobByIds(jobIds);
        return ResponseData.success("删除成功");
    }
}

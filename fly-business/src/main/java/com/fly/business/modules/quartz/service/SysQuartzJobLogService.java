package com.fly.business.modules.quartz.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.quartz.mapper.SysQuartzJobLogMapper;
import com.fly.business.modules.quartz.model.entity.SysQuartzJobLog;
import org.springframework.stereotype.Service;

/**
 * 定时任务日志 服务层
 */
@Service
public class SysQuartzJobLogService extends ServiceImpl<SysQuartzJobLogMapper, SysQuartzJobLog> {

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    public void addJobLog(SysQuartzJobLog jobLog) {
        baseMapper.insert(jobLog);
    }
}

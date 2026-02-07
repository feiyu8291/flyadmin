package com.fly.business.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.quartz.model.entity.SysQuartzJobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务日志信息 数据层
 */
@Mapper
public interface SysQuartzJobLogMapper extends BaseMapper<SysQuartzJobLog> {
}

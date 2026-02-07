package com.fly.business.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.quartz.model.entity.SysQuartzJob;
import org.apache.ibatis.annotations.Mapper;

/**
 * 调度任务信息 数据层
 */
@Mapper
public interface SysQuartzJobMapper extends BaseMapper<SysQuartzJob> {
}

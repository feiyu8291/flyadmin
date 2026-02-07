package com.fly.business.modules.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.monitor.model.entity.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志 Mapper 接口
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
}

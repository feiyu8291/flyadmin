package com.fly.business.modules.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.monitor.model.entity.SysLoginLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper 接口
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {
}

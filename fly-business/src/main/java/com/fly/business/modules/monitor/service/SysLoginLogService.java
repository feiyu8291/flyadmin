package com.fly.business.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.monitor.mapper.SysLoginLogMapper;
import com.fly.business.modules.monitor.model.entity.SysLoginLog;
import org.springframework.stereotype.Service;

/**
 * 登录日志 Service
 */
@Service
public class SysLoginLogService extends ServiceImpl<SysLoginLogMapper, SysLoginLog> {
}

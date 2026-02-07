package com.fly.business.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.monitor.mapper.SysOperLogMapper;
import com.fly.business.modules.monitor.model.entity.SysOperLog;
import org.springframework.stereotype.Service;

/**
 * 操作日志 Service
 */
@Service
public class SysOperLogService extends ServiceImpl<SysOperLogMapper, SysOperLog> {
}

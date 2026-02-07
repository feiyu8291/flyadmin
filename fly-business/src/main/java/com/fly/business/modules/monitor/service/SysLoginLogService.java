package com.fly.business.modules.monitor.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.monitor.mapper.SysLoginLogMapper;
import com.fly.business.modules.monitor.model.entity.SysLoginLog;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 登录日志 Service
 */
@Service
public class SysLoginLogService extends ServiceImpl<SysLoginLogMapper, SysLoginLog> {

    /**
     * 记录登录日志
     *
     * @param username      用户名
     * @param ipaddr        IP地址
     * @param loginLocation 登录地点
     * @param browser       浏览器类型
     * @param os            操作系统
     * @param status        登录状态（0成功 1失败）
     * @param msg           提示消息
     */
    public void recordLogin(String username, String ipaddr, String loginLocation,
                           String browser, String os, String status, String msg) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUserName(username);
        loginLog.setIpaddr(ipaddr);
        loginLog.setLoginLocation(loginLocation);
        loginLog.setBrowser(browser);
        loginLog.setOs(os);
        loginLog.setUseStatus(status);
        loginLog.setMsg(msg);
        loginLog.setLoginTime(LocalDateTime.now());
        
        this.save(loginLog);
    }

    /**
     * 记录登出日志
     *
     * @param username      用户名
     * @param ipaddr        IP地址
     * @param loginLocation 登录地点
     * @param browser       浏览器类型
     * @param os            操作系统
     */
    public void recordLogout(String username, String ipaddr, String loginLocation,
                            String browser, String os) {
        SysLoginLog loginLog = new SysLoginLog();
        loginLog.setUserName(username);
        loginLog.setIpaddr(ipaddr);
        loginLog.setLoginLocation(loginLocation);
        loginLog.setBrowser(browser);
        loginLog.setOs(os);
        loginLog.setUseStatus("0"); // 登出成功
        loginLog.setMsg("登出成功");
        loginLog.setLoginTime(LocalDateTime.now());
        
        this.save(loginLog);
    }
}

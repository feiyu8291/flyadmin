package com.fly.business.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.system.mapper.SysUserRoleMapper;
import com.fly.business.modules.system.model.entity.SysUserRole;
import org.springframework.stereotype.Service;

/**
 * 用户角色关联 Service
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {
}

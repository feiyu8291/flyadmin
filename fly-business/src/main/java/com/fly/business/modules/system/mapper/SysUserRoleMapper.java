package com.fly.business.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.system.model.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色关联 Mapper 接口
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
}

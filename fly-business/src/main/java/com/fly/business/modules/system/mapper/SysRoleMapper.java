package com.fly.business.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.system.model.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色信息 Mapper 接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
}

package com.fly.business.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.system.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户信息 Mapper 接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}

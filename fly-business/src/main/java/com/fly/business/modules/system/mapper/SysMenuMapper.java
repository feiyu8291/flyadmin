package com.fly.business.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.business.modules.system.model.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单权限 Mapper 接口
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
}

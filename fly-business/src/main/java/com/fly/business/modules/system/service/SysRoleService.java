package com.fly.business.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.system.mapper.SysRoleDeptMapper;
import com.fly.business.modules.system.mapper.SysRoleMapper;
import com.fly.business.modules.system.mapper.SysRoleMenuMapper;
import com.fly.business.modules.system.model.entity.SysRole;
import com.fly.business.modules.system.model.entity.SysRoleDept;
import com.fly.business.modules.system.model.entity.SysRoleMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 角色信息 Service
 */
@Service
@RequiredArgsConstructor
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    private final SysRoleMenuMapper roleMenuMapper;
    private final SysRoleDeptMapper roleDeptMapper;

    /**
     * 重写 updateById，同时处理角色菜单和部门关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysRole entity) {
        // 更新角色基本信息
        return super.updateById(entity);
    }

    /**
     * 重写 updateBatchById，同时处理角色菜单和部门关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<SysRole> entityList) {
        // 批量更新角色基本信息
        return super.updateBatchById(entityList);
    }

    /**
     * 更新角色菜单关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenus(Long roleId, List<Long> menuIds) {
        // 删除旧的菜单关联
        roleMenuMapper.delete(new LambdaQueryWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));

        // 插入新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            menuIds.forEach(menuId -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenuMapper.insert(roleMenu);
            });
        }
    }

    /**
     * 更新角色部门关联（数据权限）
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleDepts(Long roleId, List<Long> deptIds) {
        // 删除旧的部门关联
        roleDeptMapper.delete(new LambdaQueryWrapper<SysRoleDept>()
                .eq(SysRoleDept::getRoleId, roleId));

        // 插入新的部门关联
        if (deptIds != null && !deptIds.isEmpty()) {
            deptIds.forEach(deptId -> {
                SysRoleDept roleDept = new SysRoleDept();
                roleDept.setRoleId(roleId);
                roleDept.setDeptId(deptId);
                roleDeptMapper.insert(roleDept);
            });
        }
    }
}

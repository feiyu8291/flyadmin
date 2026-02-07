package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.business.modules.system.model.entity.SysRole;
import com.fly.business.modules.system.service.SysRoleService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 角色信息 Controller
 */
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @GetMapping("/list")
    public ResponseData<Page<SysRole>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleKey) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleName), SysRole::getRoleName, roleName)
                .like(StringUtils.hasText(roleKey), SysRole::getRoleKey, roleKey)
                .eq(SysRole::getDelFlag, "0")
                .orderByAsc(SysRole::getRoleSort);
        return ResponseData.success(roleService.page(page, wrapper));
    }

    /**
     * 获取角色详情
     */
    @GetMapping("/{roleId}")
    public ResponseData<SysRole> getInfo(@PathVariable Long roleId) {
        SysRole role = roleService.getById(roleId);
        if (role == null) {
            throw new ResponseException("角色不存在");
        }
        return ResponseData.success(role);
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysRole role) {
        // 检查角色权限字符串是否已存在
        long count = roleService.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleKey, role.getRoleKey()));
        if (count > 0) {
            throw new ResponseException("角色权限字符串已存在");
        }

        role.setDelFlag("0");
        boolean result = roleService.save(role);
        return result ? ResponseData.success() : ResponseData.error("新增角色失败");
    }

    /**
     * 修改角色
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysRole role) {
        if (role.getRoleId() == null) {
            throw new ResponseException("角色ID不能为空");
        }

        boolean result = roleService.updateById(role);
        return result ? ResponseData.success() : ResponseData.error("修改角色失败");
    }

    /**
     * 删除角色（逻辑删除）
     */
    @DeleteMapping("/{roleIds}")
    public ResponseData<Void> remove(@PathVariable String roleIds) {
        List<String> idList = Arrays.asList(roleIds.split(","));
        idList.forEach(id -> {
            SysRole role = new SysRole();
            role.setRoleId(Long.parseLong(id));
            role.setDelFlag("1");
            roleService.updateById(role);
        });
        return ResponseData.success();
    }

    /**
     * 更新角色菜单
     */
    @PutMapping("/{roleId}/menus")
    public ResponseData<Void> updateMenus(
            @PathVariable Long roleId,
            @RequestBody List<Long> menuIds) {
        roleService.updateRoleMenus(roleId, menuIds);
        return ResponseData.success();
    }

    /**
     * 更新角色数据权限（部门）
     */
    @PutMapping("/{roleId}/depts")
    public ResponseData<Void> updateDepts(
            @PathVariable Long roleId,
            @RequestBody List<Long> deptIds) {
        roleService.updateRoleDepts(roleId, deptIds);
        return ResponseData.success();
    }
}

package com.fly.business.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.business.modules.system.model.entity.SysUser;
import com.fly.business.modules.system.service.SysUserService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 用户信息 Controller
 */
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 分页查询用户列表
     */
    @GetMapping("/list")
    public ResponseData<Page<SysUser>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String loginName,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Long deptId) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(loginName), SysUser::getLoginName, loginName)
                .like(StringUtils.hasText(realName), SysUser::getRealName, realName)
                .eq(deptId != null, SysUser::getDeptId, deptId)
                .eq(SysUser::getDelFlag, "0")
                .orderByDesc(SysUser::getCreateTime);
        return ResponseData.success(userService.page(page, wrapper));
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{userId}")
    public ResponseData<SysUser> getInfo(@PathVariable Long userId) {
        SysUser user = userService.getById(userId);
        if (user == null) {
            throw new ResponseException("用户不存在");
        }
        return ResponseData.success(user);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public ResponseData<Void> add(@RequestBody SysUser user) {
        // 检查登录名是否已存在
        long count = userService.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getLoginName, user.getLoginName()));
        if (count > 0) {
            throw new ResponseException("登录账号已存在");
        }

        user.setDelFlag("0");
        boolean result = userService.save(user);
        return result ? ResponseData.success() : ResponseData.error("新增用户失败");
    }

    /**
     * 修改用户
     */
    @PutMapping("/edit")
    public ResponseData<Void> edit(@RequestBody SysUser user) {
        if (user.getUserId() == null) {
            throw new ResponseException("用户ID不能为空");
        }

        boolean result = userService.updateById(user);
        return result ? ResponseData.success() : ResponseData.error("修改用户失败");
    }

    /**
     * 删除用户（逻辑删除）
     */
    @DeleteMapping("/{userIds}")
    public ResponseData<Void> remove(@PathVariable String userIds) {
        List<String> idList = Arrays.asList(userIds.split(","));
        idList.forEach(id -> {
            SysUser user = new SysUser();
            user.setUserId(Long.parseLong(id));
            user.setDelFlag("1");
            userService.updateById(user);
        });
        return ResponseData.success();
    }

    /**
     * 更新用户角色
     */
    @PutMapping("/{userId}/roles")
    public ResponseData<Void> updateRoles(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        userService.updateUserRoles(userId, roleIds);
        return ResponseData.success();
    }

    /**
     * 更新用户岗位
     */
    @PutMapping("/{userId}/posts")
    public ResponseData<Void> updatePosts(
            @PathVariable Long userId,
            @RequestBody List<Long> postIds) {
        userService.updateUserPosts(userId, postIds);
        return ResponseData.success();
    }
}

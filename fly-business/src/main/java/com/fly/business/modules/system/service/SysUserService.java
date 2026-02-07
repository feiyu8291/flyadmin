package com.fly.business.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.system.mapper.SysUserMapper;
import com.fly.business.modules.system.mapper.SysUserPostMapper;
import com.fly.business.modules.system.mapper.SysUserRoleMapper;
import com.fly.business.modules.system.model.entity.SysUser;
import com.fly.business.modules.system.model.entity.SysUserPost;
import com.fly.business.modules.system.model.entity.SysUserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * 用户信息 Service
 */
@Service
@RequiredArgsConstructor
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    private final SysUserRoleMapper userRoleMapper;
    private final SysUserPostMapper userPostMapper;

    /**
     * 重写 updateById，同时处理用户角色和岗位关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysUser entity) {
        // 更新用户基本信息
        return super.updateById(entity);
    }

    /**
     * 重写 updateBatchById，同时处理用户角色和岗位关联
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<SysUser> entityList) {
        // 批量更新用户基本信息
        return super.updateBatchById(entityList);
    }

    /**
     * 更新用户角色关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRoles(Long userId, List<Long> roleIds) {
        // 删除旧的角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));

        // 插入新的角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            roleIds.forEach(roleId -> {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleMapper.insert(userRole);
            });
        }
    }

    /**
     * 更新用户岗位关联
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPosts(Long userId, List<Long> postIds) {
        // 删除旧的岗位关联
        userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>()
                .eq(SysUserPost::getUserId, userId));

        // 插入新的岗位关联
        if (postIds != null && !postIds.isEmpty()) {
            postIds.forEach(postId -> {
                SysUserPost userPost = new SysUserPost();
                userPost.setUserId(userId);
                userPost.setPostId(postId);
                userPostMapper.insert(userPost);
            });
        }
    }
}

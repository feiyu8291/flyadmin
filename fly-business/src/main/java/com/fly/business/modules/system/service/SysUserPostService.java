package com.fly.business.modules.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.business.modules.system.mapper.SysUserPostMapper;
import com.fly.business.modules.system.model.entity.SysUserPost;
import org.springframework.stereotype.Service;

/**
 * 用户岗位关联 Service
 */
@Service
public class SysUserPostService extends ServiceImpl<SysUserPostMapper, SysUserPost> {
}

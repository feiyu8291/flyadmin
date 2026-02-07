package com.fly.business.modules.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.business.modules.auth.model.dto.LoginRequest;
import com.fly.business.modules.auth.model.dto.LoginResponse;
import com.fly.business.modules.system.model.entity.SysUser;
import com.fly.business.modules.system.service.SysUserService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import com.fly.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 * 处理登录、登出等认证相关操作
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SysUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseData<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // 查询用户
        SysUser user = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getLoginName, request.getUsername())
                .eq(SysUser::getDelFlag, "0"));

        if (user == null) {
            throw new ResponseException("用户名或密码错误");
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseException("用户名或密码错误");
        }

        // 检查用户状态
        if (!"0".equals(user.getUseStatus())) {
            throw new ResponseException("用户已被停用");
        }

        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getLoginName());

        // 返回登录信息
        LoginResponse response = new LoginResponse(token, user.getLoginName(), expiration);
        return ResponseData.success(response);
    }

    /**
     * 用户登出
     * 注意：JWT 是无状态的，登出只需要客户端删除 Token 即可
     * 这里提供一个接口用于记录登出日志等操作
     */
    @PostMapping("/logout")
    public ResponseData<Void> logout() {
        // TODO: 可以在这里记录登出日志
        return ResponseData.success("登出成功");
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public ResponseData<SysUser> getCurrentUser(@RequestHeader("Authorization") String token) {
        // 从 Token 中获取用户名
        String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        String username = jwtUtil.getUsernameFromToken(actualToken);

        // 查询用户信息
        SysUser user = userService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getLoginName, username)
                .eq(SysUser::getDelFlag, "0"));

        if (user == null) {
            throw new ResponseException("用户不存在");
        }

        // 隐藏密码
        user.setPassword(null);
        return ResponseData.success(user);
    }
}

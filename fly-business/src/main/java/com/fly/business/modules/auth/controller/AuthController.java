package com.fly.business.modules.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fly.business.modules.auth.model.dto.LoginRequest;
import com.fly.business.modules.auth.model.dto.LoginResponse;
import com.fly.business.modules.monitor.service.SysLoginLogService;
import com.fly.business.modules.system.model.entity.SysUser;
import com.fly.business.modules.system.service.SysUserService;
import com.fly.common.exception.ResponseException;
import com.fly.common.model.ResponseData;
import com.fly.common.util.JwtUtil;
import com.fly.common.util.ServletUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private final SysLoginLogService loginLogService;

    @Value("${jwt.expiration:604800000}")
    private Long expiration;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseData<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                              HttpServletRequest httpRequest) {
        String username = request.getUsername();
        String ipaddr = ServletUtil.getClientIp(httpRequest);
        String browser = ServletUtil.getBrowser(httpRequest);
        String os = ServletUtil.getOperatingSystem(httpRequest);
        String loginLocation = com.fly.common.util.IpUtil.getLocationByIp(ipaddr);

        try {
            // 查询用户
            SysUser user = userService.getOne(new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getLoginName, username)
                    .eq(SysUser::getDelFlag, "0"));

            if (user == null) {
                // 记录登录失败日志
                loginLogService.recordLogin(username, ipaddr, loginLocation, browser, os, "1", "用户不存在");
                throw new ResponseException("用户名或密码错误");
            }

            // 验证密码
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                // 记录登录失败日志
                loginLogService.recordLogin(username, ipaddr, loginLocation, browser, os, "1", "密码错误");
                throw new ResponseException("用户名或密码错误");
            }

            // 检查用户状态
            if (!"0".equals(user.getUseStatus())) {
                // 记录登录失败日志
                loginLogService.recordLogin(username, ipaddr, loginLocation, browser, os, "1", "用户已被停用");
                throw new ResponseException("用户已被停用");
            }

            // 生成 JWT Token
            String token = jwtUtil.generateToken(user.getLoginName());

            // 记录登录成功日志
            loginLogService.recordLogin(username, ipaddr, loginLocation, browser, os, "0", "登录成功");

            // 返回登录信息
            LoginResponse response = new LoginResponse(token, user.getLoginName(), expiration);
            return ResponseData.success(response);

        } catch (ResponseException e) {
            // 重新抛出业务异常
            throw e;
        } catch (Exception e) {
            // 记录系统异常
            loginLogService.recordLogin(username, ipaddr, loginLocation, browser, os, "1", "系统异常：" + e.getMessage());
            throw new ResponseException("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     * 注意：JWT 是无状态的，登出只需要客户端删除 Token 即可
     * 这里提供一个接口用于记录登出日志等操作
     */
    @PostMapping("/logout")
    public ResponseData<Void> logout(@RequestHeader(value = "Authorization", required = false) String token,
                                      HttpServletRequest request) {
        try {
            // 获取用户名
            String username = "未知用户";
            if (token != null && !token.isEmpty()) {
                String actualToken = token.startsWith("Bearer ") ? token.substring(7) : token;
                try {
                    username = jwtUtil.getUsernameFromToken(actualToken);
                } catch (Exception e) {
                    // Token 解析失败，使用默认值
                }
            }

            // 获取请求信息
            String ipaddr = ServletUtil.getClientIp(request);
            String browser = ServletUtil.getBrowser(request);
            String os = ServletUtil.getOperatingSystem(request);
            String loginLocation = com.fly.common.util.IpUtil.getLocationByIp(ipaddr);

            // 记录登出日志
            loginLogService.recordLogout(username, ipaddr, loginLocation, browser, os);

        } catch (Exception e) {
            // 记录日志失败不影响登出操作
            e.printStackTrace();
        }

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

package com.fly.common.security;

import com.fly.common.context.UserContextHolder;
import com.fly.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT 认证过滤器
 * 从请求头中提取 JWT Token，验证并设置认证信息
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 从请求头中获取 Token
            String token = getTokenFromRequest(request);

            // 验证 Token
            if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
                // 从 Token 中获取用户名
                String username = jwtUtil.getUsernameFromToken(token);

                // 设置到 UserContextHolder（用于 MyBatis-Plus 自动填充）
                UserContextHolder.setCurrentUser(username);

                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

                // 设置到 Spring Security 上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 继续过滤链
            filterChain.doFilter(request, response);
        } finally {
            // 请求结束后清除 ThreadLocal，防止内存泄漏
            UserContextHolder.clear();
        }
    }

    /**
     * 从请求头中提取 Token
     * 支持两种格式：
     * 1. Authorization: Bearer <token>
     * 2. Authorization: <token>
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return bearerToken;
        }
        return null;
    }
}

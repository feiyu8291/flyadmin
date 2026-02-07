package com.fly.common.context;

/**
 * 用户上下文工具类
 * 用于在 ThreadLocal 中存储和获取当前登录用户信息
 */
public class UserContextHolder {

    private static final ThreadLocal<String> USER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(String username) {
        USER_CONTEXT.set(username);
    }

    /**
     * 获取当前用户
     */
    public static String getCurrentUser() {
        String username = USER_CONTEXT.get();
        return username != null ? username : "system";
    }

    /**
     * 清除当前用户
     */
    public static void clear() {
        USER_CONTEXT.remove();
    }
}

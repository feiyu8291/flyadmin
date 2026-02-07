package com.fly.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fly.common.context.UserContextHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 * 自动填充创建时间、修改时间、创建人、修改人
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取当前用户（暂时使用默认值，后续可以从 SecurityContext 或 ThreadLocal 获取）
        String currentUser = getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        // 自动填充创建时间
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        // 自动填充创建人
        this.strictInsertFill(metaObject, "createBy", String.class, currentUser);
        // 自动填充更新时间
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        // 自动填充更新人
        this.strictInsertFill(metaObject, "updateBy", String.class, currentUser);
    }

    /**
     * 更新时自动填充
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取当前用户
        String currentUser = getCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        // 自动填充更新人
        this.strictUpdateFill(metaObject, "updateBy", String.class, currentUser);
    }

    /**
     * 获取当前用户
     * 从 UserContextHolder 中获取当前登录用户
     */
    private String getCurrentUser() {
        return UserContextHolder.getCurrentUser();
    }
}

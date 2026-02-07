/*==============================================================*/
/* DBMS name:      MySQL 8.0                                    */
/* Created on:     2026/2/5 17:52:00                            */
/* Description:    Enhanced System Database Schema              */
/*==============================================================*/

CREATE DATABASE IF NOT EXISTS fly_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE fly_admin;

-- 删除已存在的表
DROP TABLE IF EXISTS sys_user_post;
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_login_log;
DROP TABLE IF EXISTS sys_oper_log;
DROP TABLE IF EXISTS sys_notice;
DROP TABLE IF EXISTS sys_config;
DROP TABLE IF EXISTS sys_post;
DROP TABLE IF EXISTS sys_dept;
DROP TABLE IF EXISTS sys_user_org_role;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_dict_data;
DROP TABLE IF EXISTS sys_dict_type;
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_org_tree;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user;

/*==============================================================*/
/* Table: sys_user - 用户信息表                                  */
/*==============================================================*/
CREATE TABLE sys_user
(
    user_id      BIGINT      NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    dept_id      BIGINT       DEFAULT NULL COMMENT '部门ID',
    login_name   VARCHAR(64) NOT NULL COMMENT '登录账号',
    real_name    VARCHAR(64)  DEFAULT '' COMMENT '真实姓名',
    user_type    VARCHAR(2)   DEFAULT '00' COMMENT '用户类型（00系统用户）',
    email        VARCHAR(64)  DEFAULT '' COMMENT '用户邮箱',
    phone_number VARCHAR(32)  DEFAULT '' COMMENT '手机号码',
    sex          CHAR(1)      DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
    avatar       VARCHAR(255) DEFAULT '' COMMENT '头像地址',
    password     VARCHAR(128) DEFAULT '' COMMENT '密码',
    use_status   CHAR(1)      DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
    del_flag     CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    login_ip     VARCHAR(128) DEFAULT '' COMMENT '最后登录IP',
    login_date   DATETIME COMMENT '最后登录时间',
    create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_login_name (login_name),
    KEY idx_dept_id (dept_id),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='用户信息表';

/*==============================================================*/
/* Table: sys_dept - 部门表                                      */
/*==============================================================*/
CREATE TABLE sys_dept
(
    dept_id     BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门id',
    parent_id   BIGINT       DEFAULT 0 COMMENT '父部门id',
    ancestors   VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    dept_name   VARCHAR(64)  DEFAULT '' COMMENT '部门名称',
    order_num   INT          DEFAULT 0 COMMENT '显示顺序',
    leader      VARCHAR(64)  DEFAULT NULL COMMENT '负责人',
    phone       VARCHAR(32)  DEFAULT NULL COMMENT '联系电话',
    email       VARCHAR(64)  DEFAULT NULL COMMENT '邮箱',
    use_status  CHAR(1)      DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
    del_flag    CHAR(1)      DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by   VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (dept_id),
    KEY idx_parent_id (parent_id),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 200 COMMENT ='部门表';

/*==============================================================*/
/* Table: sys_post - 岗位信息表                                  */
/*==============================================================*/
CREATE TABLE sys_post
(
    post_id     BIGINT      NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
    post_code   VARCHAR(64) NOT NULL COMMENT '岗位编码',
    post_name   VARCHAR(64) NOT NULL COMMENT '岗位名称',
    post_sort   INT         NOT NULL DEFAULT 0 COMMENT '显示顺序',
    use_status  CHAR(1)              DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by   VARCHAR(64)          DEFAULT '' COMMENT '创建者',
    create_time DATETIME             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   VARCHAR(64)          DEFAULT '' COMMENT '更新者',
    update_time DATETIME             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500)         DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (post_id),
    UNIQUE KEY uk_post_code (post_code),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='岗位信息表';

/*==============================================================*/
/* Table: sys_role - 角色信息表                                  */
/*==============================================================*/
CREATE TABLE sys_role
(
    role_id     BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    role_name   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    role_key    VARCHAR(128) NOT NULL COMMENT '角色权限字符串',
    role_sort   INT          NOT NULL DEFAULT 0 COMMENT '显示顺序',
    data_scope  CHAR(1)               DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限）',
    use_status  CHAR(1)               DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
    del_flag    CHAR(1)               DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
    create_by   VARCHAR(64)           DEFAULT '' COMMENT '创建者',
    create_time DATETIME              DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   VARCHAR(64)           DEFAULT '' COMMENT '更新者',
    update_time DATETIME              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500)          DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (role_id),
    UNIQUE KEY uk_role_key (role_key),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='角色信息表';

/*==============================================================*/
/* Table: sys_menu - 菜单权限表                                  */
/*==============================================================*/
CREATE TABLE sys_menu
(
    menu_id     BIGINT      NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    menu_name   VARCHAR(64) NOT NULL COMMENT '菜单名称',
    parent_id   BIGINT       DEFAULT 0 COMMENT '父菜单ID',
    order_num   INT          DEFAULT 0 COMMENT '显示顺序',
    path        VARCHAR(255) DEFAULT '' COMMENT '路由地址',
    component   VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
    query       VARCHAR(255) DEFAULT NULL COMMENT '路由参数',
    frame_flag  CHAR(1)      DEFAULT '1' COMMENT '是否为外链（0是 1否）',
    cache_flag  CHAR(1)      DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
    menu_type   CHAR(1)      DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
    visible     CHAR(1)      DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
    use_status  CHAR(1)      DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
    perms       VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
    icon        VARCHAR(128) DEFAULT '#' COMMENT '菜单图标',
    create_by   VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500) DEFAULT '' COMMENT '备注',
    PRIMARY KEY (menu_id),
    KEY idx_parent_id (parent_id),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2000 COMMENT ='菜单权限表';

/*==============================================================*/
/* Table: sys_user_role - 用户和角色关联表                       */
/*==============================================================*/
CREATE TABLE sys_user_role
(
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE = InnoDB COMMENT ='用户和角色关联表';

/*==============================================================*/
/* Table: sys_user_post - 用户与岗位关联表                       */
/*==============================================================*/
CREATE TABLE sys_user_post
(
    user_id BIGINT NOT NULL COMMENT '用户ID',
    post_id BIGINT NOT NULL COMMENT '岗位ID',
    PRIMARY KEY (user_id, post_id)
) ENGINE = InnoDB COMMENT ='用户与岗位关联表';

/*==============================================================*/
/* Table: sys_role_menu - 角色和菜单关联表                       */
/*==============================================================*/
CREATE TABLE sys_role_menu
(
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE = InnoDB COMMENT ='角色和菜单关联表';

/*==============================================================*/
/* Table: sys_role_dept - 角色和部门关联表                       */
/*==============================================================*/
CREATE TABLE sys_role_dept
(
    role_id BIGINT NOT NULL COMMENT '角色ID',
    dept_id BIGINT NOT NULL COMMENT '部门ID',
    PRIMARY KEY (role_id, dept_id)
) ENGINE = InnoDB COMMENT ='角色和部门关联表';

/*==============================================================*/
/* Table: sys_config - 参数配置表                                */
/*==============================================================*/
CREATE TABLE sys_config
(
    config_id    INT NOT NULL AUTO_INCREMENT COMMENT '参数主键',
    config_name  VARCHAR(128) DEFAULT '' COMMENT '参数名称',
    config_key   VARCHAR(128) DEFAULT '' COMMENT '参数键名',
    config_value VARCHAR(512) DEFAULT '' COMMENT '参数键值',
    config_type  CHAR(1)      DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
    create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (config_id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='参数配置表';

/*==============================================================*/
/* Table: sys_notice - 通知公告表                                */
/*==============================================================*/
CREATE TABLE sys_notice
(
    notice_id      INT         NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    notice_title   VARCHAR(64) NOT NULL COMMENT '公告标题',
    notice_type    CHAR(1)     NOT NULL COMMENT '公告类型（1通知 2公告）',
    notice_content LONGBLOB     DEFAULT NULL COMMENT '公告内容',
    use_status     CHAR(1)      DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
    create_by      VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by      VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time    DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark         VARCHAR(255) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (notice_id),
    KEY idx_use_status (use_status)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1 COMMENT ='通知公告表';

/*==============================================================*/
/* Table: sys_oper_log - 操作日志记录                            */
/*==============================================================*/
CREATE TABLE sys_oper_log
(
    oper_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
    title          VARCHAR(64)   DEFAULT '' COMMENT '模块标题',
    business_type  INT           DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
    method         VARCHAR(128)  DEFAULT '' COMMENT '方法名称',
    request_method VARCHAR(16)   DEFAULT '' COMMENT '请求方式',
    operator_type  INT           DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
    oper_name      VARCHAR(64)   DEFAULT '' COMMENT '操作人员',
    dept_name      VARCHAR(64)   DEFAULT '' COMMENT '部门名称',
    oper_url       VARCHAR(255)  DEFAULT '' COMMENT '请求URL',
    oper_ip        VARCHAR(128)  DEFAULT '' COMMENT '主机地址',
    oper_location  VARCHAR(255)  DEFAULT '' COMMENT '操作地点',
    oper_param     VARCHAR(2000) DEFAULT '' COMMENT '请求参数',
    json_result    VARCHAR(2000) DEFAULT '' COMMENT '返回参数',
    use_status     INT           DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
    error_msg      VARCHAR(2000) DEFAULT '' COMMENT '错误消息',
    oper_time      DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (oper_id),
    KEY idx_business_type (business_type),
    KEY idx_use_status (use_status),
    KEY idx_oper_time (oper_time)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='操作日志记录';

/*==============================================================*/
/* Table: sys_login_log - 系统访问记录                           */
/*==============================================================*/
CREATE TABLE sys_login_log
(
    info_id        BIGINT NOT NULL AUTO_INCREMENT COMMENT '访问ID',
    user_name      VARCHAR(64)  DEFAULT '' COMMENT '用户账号',
    ipaddr         VARCHAR(128) DEFAULT '' COMMENT '登录IP地址',
    login_location VARCHAR(255) DEFAULT '' COMMENT '登录地点',
    browser        VARCHAR(64)  DEFAULT '' COMMENT '浏览器类型',
    os             VARCHAR(64)  DEFAULT '' COMMENT '操作系统',
    use_status     CHAR(1)      DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
    msg            VARCHAR(255) DEFAULT '' COMMENT '提示消息',
    login_time     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    PRIMARY KEY (info_id),
    KEY idx_use_status (use_status),
    KEY idx_login_time (login_time)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='系统访问记录';

/*==============================================================*/
/* Table: sys_dict_type - 字典类型表                             */
/*==============================================================*/
CREATE TABLE sys_dict_type
(
    dict_id     BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典主键',
    dict_name   VARCHAR(128) DEFAULT '' COMMENT '字典名称',
    dict_type   VARCHAR(128) DEFAULT '' COMMENT '字典类型',
    use_status  CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by   VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by   VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (dict_id),
    UNIQUE KEY uk_dict_type (dict_type)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='字典类型表';

/*==============================================================*/
/* Table: sys_dict_data - 字典数据表                             */
/*==============================================================*/
CREATE TABLE sys_dict_data
(
    dict_code    BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典编码',
    dict_sort    INT          DEFAULT 0 COMMENT '字典排序',
    dict_label   VARCHAR(128) DEFAULT '' COMMENT '字典标签',
    dict_value   VARCHAR(128) DEFAULT '' COMMENT '字典键值',
    dict_type    VARCHAR(128) DEFAULT '' COMMENT '字典类型',
    css_class    VARCHAR(128) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
    list_class   VARCHAR(128) DEFAULT NULL COMMENT '表格回显样式',
    default_flag CHAR(1)      DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
    use_status   CHAR(1)      DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by    VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by    VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    remark       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (dict_code),
    KEY idx_dict_type (dict_type)
) ENGINE = InnoDB
  AUTO_INCREMENT = 100 COMMENT ='字典数据表';

-- ========================================
-- 初始化数据
-- ========================================

-- 部门数据
INSERT INTO sys_dept
VALUES (100, 0, '0', 'FlyAdmin科技', 0, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '', NULL);
INSERT INTO sys_dept
VALUES (101, 100, '0,100', '深圳总公司', 1, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '',
        NULL);
INSERT INTO sys_dept
VALUES (102, 100, '0,100', '北京分公司', 2, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '',
        NULL);
INSERT INTO sys_dept
VALUES (103, 101, '0,100,101', '研发部门', 1, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '',
        NULL);
INSERT INTO sys_dept
VALUES (104, 101, '0,100,101', '市场部门', 2, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '',
        NULL);
INSERT INTO sys_dept
VALUES (105, 101, '0,100,101', '测试部门', 3, '管理员', '15888888888', 'admin@fly.com', '0', '0', 'admin', NOW(), '',
        NULL);

-- 岗位数据
INSERT INTO sys_post
VALUES (1, 'ceo', '董事长', 1, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post
VALUES (2, 'se', '项目经理', 2, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post
VALUES (3, 'hr', '人力资源', 3, '0', 'admin', NOW(), '', NULL, '');
INSERT INTO sys_post
VALUES (4, 'user', '普通员工', 4, '0', 'admin', NOW(), '', NULL, '');

-- 角色数据
INSERT INTO sys_role
VALUES (1, '超级管理员', 'admin', 1, '1', '0', '0', 'admin', NOW(), '', NULL, '超级管理员');
INSERT INTO sys_role
VALUES (2, '普通角色', 'common', 2, '2', '0', '0', 'admin', NOW(), '', NULL, '普通角色');

-- 用户数据 (密码: admin123)
INSERT INTO sys_user
VALUES (1, 103, 'admin', '管理员', '00', 'admin@fly.com', '15888888888', '1', '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU1KkOq6Gba', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(),
        '', NULL, '管理员');
INSERT INTO sys_user
VALUES (2, 105, 'fly', '测试', '00', 'fly@fly.com', '15666666666', '1', '',
        '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/TU1KkOq6Gba', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(),
        '', NULL, '测试员');

-- 用户角色关联
INSERT INTO sys_user_role
VALUES (1, 1);
INSERT INTO sys_user_role
VALUES (2, 2);

-- 用户岗位关联
INSERT INTO sys_user_post
VALUES (1, 1);
INSERT INTO sys_user_post
VALUES (2, 4);

-- 菜单数据
INSERT INTO sys_menu
VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '1', '0', 'M', '0', '0', '', 'system', 'admin', NOW(), '', NULL,
        '系统管理目录');
INSERT INTO sys_menu
VALUES (2, '系统监控', 0, 2, 'monitor', NULL, '', '1', '0', 'M', '0', '0', '', 'monitor', 'admin', NOW(), '', NULL,
        '系统监控目录');
INSERT INTO sys_menu
VALUES (3, '系统工具', 0, 3, 'tool', NULL, '', '1', '0', 'M', '0', '0', '', 'tool', 'admin', NOW(), '', NULL,
        '系统工具目录');

INSERT INTO sys_menu
VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user',
        'admin', NOW(), '', NULL, '用户管理菜单');
INSERT INTO sys_menu
VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples',
        'admin', NOW(), '', NULL, '角色管理菜单');
INSERT INTO sys_menu
VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '1', '0', 'C', '0', '0', 'system:menu:list',
        'tree-table', 'admin', NOW(), '', NULL, '菜单管理菜单');
INSERT INTO sys_menu
VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '1', '0', 'C', '0', '0', 'system:dept:list', 'tree',
        'admin', NOW(), '', NULL, '部门管理菜单');
INSERT INTO sys_menu
VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', '1', '0', 'C', '0', '0', 'system:post:list', 'post',
        'admin', NOW(), '', NULL, '岗位管理菜单');
INSERT INTO sys_menu
VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '1', '0', 'C', '0', '0', 'system:dict:list', 'dict',
        'admin', NOW(), '', NULL, '字典管理菜单');
INSERT INTO sys_menu
VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '1', '0', 'C', '0', '0', 'system:config:list',
        'edit', 'admin', NOW(), '', NULL, '参数设置菜单');
INSERT INTO sys_menu
VALUES (107, '通知公告', 1, 8, 'notice', 'system/notice/index', '', '1', '0', 'C', '0', '0', 'system:notice:list',
        'message', 'admin', NOW(), '', NULL, '通知公告菜单');
INSERT INTO sys_menu
VALUES (108, '日志管理', 1, 9, 'log', '', '', '1', '0', 'M', '0', '0', '', 'log', 'admin', NOW(), '', NULL,
        '日志管理菜单');

INSERT INTO sys_menu
VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', '1', '0', 'C', '0', '0',
        'monitor:operlog:list', 'form', 'admin', NOW(), '', NULL, '操作日志菜单');
INSERT INTO sys_menu
VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', '1', '0', 'C', '0', '0',
        'monitor:logininfor:list', 'logininfor', 'admin', NOW(), '', NULL, '登录日志菜单');

INSERT INTO sys_menu
VALUES (109, '在线用户', 2, 1, 'online', 'monitor/online/index', '', '1', '0', 'C', '0', '0', 'monitor:online:list',
        'online', 'admin', NOW(), '', NULL, '在线用户菜单');
INSERT INTO sys_menu
VALUES (110, '定时任务', 2, 2, 'job', 'monitor/job/index', '', '1', '0', 'C', '0', '0', 'monitor:job:list', 'job',
        'admin', NOW(), '', NULL, '定时任务菜单');

-- 角色菜单关联（超级管理员拥有所有权限）
INSERT INTO sys_role_menu
VALUES (1, 1);
INSERT INTO sys_role_menu
VALUES (1, 2);
INSERT INTO sys_role_menu
VALUES (1, 3);
INSERT INTO sys_role_menu
VALUES (1, 100);
INSERT INTO sys_role_menu
VALUES (1, 101);
INSERT INTO sys_role_menu
VALUES (1, 102);
INSERT INTO sys_role_menu
VALUES (1, 103);
INSERT INTO sys_role_menu
VALUES (1, 104);
INSERT INTO sys_role_menu
VALUES (1, 105);
INSERT INTO sys_role_menu
VALUES (1, 106);
INSERT INTO sys_role_menu
VALUES (1, 107);
INSERT INTO sys_role_menu
VALUES (1, 108);
INSERT INTO sys_role_menu
VALUES (1, 109);
INSERT INTO sys_role_menu
VALUES (1, 110);
INSERT INTO sys_role_menu
VALUES (1, 500);
INSERT INTO sys_role_menu
VALUES (1, 501);

-- 系统配置数据
INSERT INTO sys_config
VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', NOW(), '', NULL,
        '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO sys_config
VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', NOW(), '', NULL,
        '初始化密码 123456');
INSERT INTO sys_config
VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', NOW(), '', NULL,
        '深色主题theme-dark，浅色主题theme-light');

-- 字典类型数据
INSERT INTO sys_dict_type
VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', NOW(), '', NULL, '用户性别列表');
INSERT INTO sys_dict_type
VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', NOW(), '', NULL, '菜单状态列表');
INSERT INTO sys_dict_type
VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', NOW(), '', NULL, '系统开关列表');
INSERT INTO sys_dict_type
VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', NOW(), '', NULL, '任务状态列表');
INSERT INTO sys_dict_type
VALUES (5, '系统是否', 'sys_yes_no', '0', 'admin', NOW(), '', NULL, '系统是否列表');
INSERT INTO sys_dict_type
VALUES (6, '通知类型', 'sys_notice_type', '0', 'admin', NOW(), '', NULL, '通知类型列表');
INSERT INTO sys_dict_type
VALUES (7, '通知状态', 'sys_notice_status', '0', 'admin', NOW(), '', NULL, '通知状态列表');
INSERT INTO sys_dict_type
VALUES (8, '操作类型', 'sys_oper_type', '0', 'admin', NOW(), '', NULL, '操作类型列表');
INSERT INTO sys_dict_type
VALUES (9, '系统状态', 'sys_common_status', '0', 'admin', NOW(), '', NULL, '登录状态列表');

-- 字典数据
INSERT INTO sys_dict_data
VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', NOW(), '', NULL, '性别男');
INSERT INTO sys_dict_data
VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', NOW(), '', NULL, '性别女');
INSERT INTO sys_dict_data
VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', NOW(), '', NULL, '性别未知');

INSERT INTO sys_dict_data
VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '显示菜单');
INSERT INTO sys_dict_data
VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '隐藏菜单');

INSERT INTO sys_dict_data
VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '停用状态');

INSERT INTO sys_dict_data
VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '系统默认是');
INSERT INTO sys_dict_data
VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '系统默认否');

INSERT INTO sys_dict_data
VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', NOW(), '', NULL, '通知');
INSERT INTO sys_dict_data
VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', NOW(), '', NULL, '公告');

INSERT INTO sys_dict_data
VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', NOW(), '', NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '关闭状态');

INSERT INTO sys_dict_data
VALUES (18, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', NOW(), '', NULL, '新增操作');
INSERT INTO sys_dict_data
VALUES (19, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', NOW(), '', NULL, '修改操作');
INSERT INTO sys_dict_data
VALUES (20, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '删除操作');
INSERT INTO sys_dict_data
VALUES (21, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', NOW(), '', NULL, '授权操作');
INSERT INTO sys_dict_data
VALUES (22, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '导出操作');
INSERT INTO sys_dict_data
VALUES (23, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '导入操作');
INSERT INTO sys_dict_data
VALUES (24, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '强退操作');
INSERT INTO sys_dict_data
VALUES (25, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', NOW(), '', NULL, '生成操作');
INSERT INTO sys_dict_data
VALUES (26, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '清空操作');

INSERT INTO sys_dict_data
VALUES (27, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', NOW(), '', NULL, '正常状态');
INSERT INTO sys_dict_data
VALUES (28, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', NOW(), '', NULL, '停用状态');

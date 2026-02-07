/*==============================================================*/
/* DBMS name:      MySQL 8.0                                    */
/* Created on:     2026/2/5 10:18:03                            */
/*==============================================================*/

CREATE DATABASE IF NOT EXISTS fly_admin;

use fly_admin;

drop table if exists sys_data_dictionary;

drop table if exists sys_menu;

drop table if exists sys_org_tree;

drop table if exists sys_role;

drop table if exists sys_role_menu;

drop table if exists sys_user;

drop table if exists sys_user_org_role;

drop table if exists drone.tool_s3_storage;

/*==============================================================*/
/* Table: sys_data_dictionary                                   */
/*==============================================================*/
create table sys_data_dictionary
(
    dict_id        bigint       not null auto_increment comment '数据字典id',
    data_type_name varchar(64) comment '类型名字',
    data_type      varchar(64)  not null comment '类型',
    dict_sort      tinyint      not null default 0 comment '排序',
    data_code      varchar(64)  not null comment '编码',
    data_value     varchar(255) not null comment '值',
    parent_type    varchar(64)           default '' comment '上级类型',
    parent_code    tinyint               default 0 comment '上级编码',
    data_remark    varchar(255) comment '字典备注',
    web_read_only  tinyint               default 0 comment '页面是否只读0否1是',
    default_state  tinyint               default 0 comment '是否默认选中0否1是',
    del_flag       tinyint      not null default 0 comment '是否删除 0否1是',
    create_time    datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time    datetime     not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    create_by      varchar(64)  not null comment '创建人',
    update_by      varchar(64)  not null comment '修改人',
    primary key (dict_id)
);

alter table sys_data_dictionary
    comment '系统数据字典表';

/*==============================================================*/
/* Table: sys_menu                                              */
/*==============================================================*/
create table sys_menu
(
    id          bigint      not null auto_increment comment '菜单主键',
    parent_id   bigint      not null default 0 comment '父级id',
    menu_name   varchar(64) comment '菜单名字',
    menu_code   varchar(64) comment '菜单code',
    request_url varchar(255) comment '菜单url',
    menu_type   varchar(255) comment '菜单类型 1菜单 2按钮 3其他',
    sort_order  tinyint comment '排序号',
    hidden      tinyint comment '0启用 1隐藏',
    description varchar(255) comment '描述',
    del_flag    tinyint     not null default 0 comment '是否删除 0否1是',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    create_by   varchar(64) not null comment '创建人',
    update_by   varchar(64) not null comment '修改人',
    primary key (id)
);

alter table sys_menu
    comment '菜单信息表';

/*==============================================================*/
/* Table: sys_org_tree                                          */
/*==============================================================*/
create table sys_org_tree
(
    org_id       bigint       not null comment '主键ID',
    org_name     varchar(255) not null comment '组织节点名称',
    org_type     tinyint      not null comment '组织节点类型',
    parent_id    bigint       not null default 0 comment '上级节点id',
    parent_type  tinyint      not null default 0 comment '上级节点类型',
    child_number int comment '子节点数量',
    root_id      varchar(255) not null comment '上级节点层次，格式1,2,3',
    del_flag     tinyint      not null default 0 comment '是否删除 0否1是',
    create_time  datetime     not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time  datetime     not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    create_by    varchar(64)  not null comment '创建人',
    update_by    varchar(64)  not null comment '修改人',
    primary key (org_id)
);

alter table sys_org_tree
    comment '组织结构表';

/*==============================================================*/
/* Table: sys_role                                              */
/*==============================================================*/
create table sys_role
(
    role_id     bigint      not null comment '角色主键',
    role_name   varchar(60) not null comment '角色名字',
    role_type   tinyint     not null comment '角色类型',
    role_remark varchar(255) comment '角色说明备注',
    org_id      bigint               default 0 comment '组织ID',
    use_state   tinyint     not null default 0 comment '是否禁用 0否1是',
    del_flag    tinyint     not null default 0 comment '是否删除 0否1是',
    create_time datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time datetime    not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    create_by   varchar(64) not null comment '创建人',
    update_by   varchar(64) not null comment '修改人',
    primary key (role_id)
);

alter table sys_role
    comment '角色信息表';

/*==============================================================*/
/* Table: sys_role_menu                                         */
/*==============================================================*/
create table sys_role_menu
(
    role_id bigint not null comment '角色ID',
    menu_id bigint not null comment '组织ID',
    primary key (role_id, menu_id)
);

alter table sys_role_menu
    comment '用户组织-角色中间表';

/*==============================================================*/
/* Table: sys_user                                              */
/*==============================================================*/
create table sys_user
(
    user_id        bigint      not null comment '主键ID',
    real_name      varchar(64) comment '真实姓名',
    login_name     varchar(64) not null comment '登录用户名',
    login_phone    varchar(32) not null comment '登录手机号',
    id_card        varchar(64) not null comment '身份证号码',
    login_password varchar(64) not null comment '用户密码',
    email          varchar(32) comment '用户邮箱',
    sex            tinyint comment '性别',
    post_id        tinyint comment '职务',
    default_org_id bigint      not null default 0 comment '默认选中的组织',
    current_org_id bigint      not null default 0 comment '当前组织',
    use_state      tinyint     not null default 0 comment '是否禁用 0否1是',
    del_flag       tinyint     not null default 0 comment '是否删除 0否1是',
    create_time    datetime    not null default CURRENT_TIMESTAMP comment '创建时间',
    update_time    datetime    not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '修改时间',
    create_by      varchar(64) not null comment '创建人',
    update_by      varchar(64) not null comment '修改人',
    primary key (user_id)
);

alter table sys_user
    comment '用户信息表';

/*==============================================================*/
/* Table: sys_user_org_role                                     */
/*==============================================================*/
create table sys_user_org_role
(
    user_id bigint not null comment '用户ID',
    org_id  bigint not null comment '组织ID',
    role_id bigint not null comment '角色ID',
    primary key (user_id, org_id, role_id)
);

alter table sys_user_org_role
    comment '用户组织-角色中间表';

/*==============================================================*/
/* Table: tool_s3_storage                                       */
/*==============================================================*/
create table drone.tool_s3_storage
(
    storage_id     bigint       not null auto_increment comment '主键',
    bucket_number  tinyint      not null default 1 comment '存储桶序号',
    file_name      varchar(255) not null comment '文件名称',
    file_real_name varchar(255) not null comment '真实存储的名称',
    file_size      varchar(100) not null comment '文件大小',
    file_mime_type varchar(50)  not null comment '文件MIME 类型',
    file_type      varchar(50)  not null comment '文件类型',
    file_path      tinytext     not null comment '文件路径',
    create_by      varchar(255) not null comment '创建者',
    update_by      varchar(255) not null comment '更新者',
    create_time    datetime     not null comment '创建日期',
    update_time    datetime     not null comment '更新时间',
    primary key (storage_id)
);

alter table drone.tool_s3_storage
    comment 's3 协议对象存储';


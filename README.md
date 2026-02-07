# flyadmin
一个基于 Spring Boot 2.7.18 、 MybatisPlus、 JWT、Spring Security、Redis、Vue的前后端分离的后台管理系统

# FlyAdmin 项目分析

## 概述
FlyAdmin 是一个基于 Spring Boot 2.7.18 构建的后台管理系统，采用多模块架构。它利用现代 Java 技术栈，注重效率和可扩展性。

## 技术栈
- **核心框架**: Spring Boot 2.7.18
- **ORM 框架**: MyBatis Plus 3.5.3.1
- **数据库连接池**: Druid 1.2.19
- **数据库**: MySQL (根据连接器推断)
- **安全框架**: Spring Security
- **缓存**: Redis (Spring Data Redis), Redisson 3.17.1
- **JSON 处理**: Fastjson2 2.0.54
- **API 文档**: SpringDoc OpenApi / Knife4j 3.0.3
- **工具库**: Lombok, Hutool (通常此类项目会包含), Easy Captcha (验证码), IP2Region (IP地址定位).

## 项目结构
项目分为两个主要模块：

### 1. `fly-common`
包含共享的工具类和基础设施代码。
- **Config**: 全局配置 (OpenAPI, Redis 等)。
- **Constant**: 系统常量。
- **Exception**: 全局异常处理。
- **Model**: 共享数据模型。
- **Tool**: 通用工具类。

### 2. `fly-business`
包含核心业务逻辑和功能实现。
- **Modules**:
    - `system`: 核心管理功能 (用户管理, 角色, 权限等)。
    - `quartz`: 任务调度管理。
    - `monitor`: 系统监控和日志。
- **AppRun.java**: 应用程序的主入口点。

## 主要功能 (推断)
- **RBAC**: 基于角色的访问控制，通过 Spring Security 实现。
- **任务调度**: 使用 Quartz 实现分布式任务调度。
- **监控**: 服务器状态和 SQL 执行监控 (P6Spy)。
- **API 文档**: 使用 Knife4j 自动生成 API 文档。
- **代码生成**: 可能支持 (通常 MyBatis Plus 项目会集成，需进一步确认)。

## 建议
- **Java 版本**: 项目目前使用 Java 1.8 (`<java.version>1.8</java.version>`)。如果部署环境允许，建议升级到 Java 17 或 21 以获得更好的性能和新特性。
- **依赖升级**: Spring Boot 2.7.x 版本已停止 OSS 支持 (2023年11月)。建议规划升级到 Spring Boot 3.x (需要 Java 17+ 和迁移到 Jakarta EE)。

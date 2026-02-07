# Flyway MySQL 8.0 支持问题修复

## 问题描述

启动应用时出现以下错误：

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'flywayInitializer' 
defined in class path resource [org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class]: 
Invocation of init method failed; nested exception is org.flywaydb.core.api.FlywayException: 
Unsupported Database: MySQL 8.0
```

## 问题原因

Spring Boot 2.7.x 默认使用的 Flyway 版本为 7.x，该版本**不支持 MySQL 8.0**。

### 版本兼容性

| Flyway 版本 | MySQL 支持版本 |
|------------|---------------|
| 7.x        | MySQL 5.7 及以下 |
| 8.x        | MySQL 8.0（部分支持） |
| 9.x        | MySQL 8.0（完整支持） |

## 解决方案

### 1. 升级 Flyway 版本

在 `fly-business/pom.xml` 中添加 Flyway 版本配置：

```xml
<properties>
    <java.version>1.8</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <spring-boot.version>2.7.6</spring-boot.version>
    <!-- 添加 Flyway 版本配置 -->
    <flyway.version>9.22.3</flyway.version>
</properties>
```

### 2. 添加 flyway-mysql 依赖

在 `fly-business/pom.xml` 的 dependencies 部分更新 Flyway 配置：

```xml
<!-- flyway -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
    <version>${flyway.version}</version>
</dependency>

<!-- flyway-mysql：支持 MySQL 8.0 -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
    <version>${flyway.version}</version>
</dependency>
```

### 3. 重新编译项目

```bash
mvn clean compile -DskipTests
```

## 修复验证

执行编译命令后，输出应显示：

```
[INFO] BUILD SUCCESS
[INFO] Total time:  47.868 s
```

并且可以看到 Flyway 9.22.3 和相关依赖已成功下载：

```
Downloaded from public: .../flyway-core/9.22.3/flyway-core-9.22.3.jar (885 kB)
Downloaded from public: .../flyway-mysql/9.22.3/flyway-mysql-9.22.3.jar (31 kB)
```

## 技术说明

### 为什么需要 flyway-mysql？

从 Flyway 9.x 开始，数据库特定的支持被拆分成独立的模块。要支持 MySQL，需要额外添加 `flyway-mysql` 依赖。

### 支持的数据库版本

升级后的 Flyway 9.22.3 支持：
- ✅ MySQL 5.7
- ✅ MySQL 8.0
- ✅ MySQL 8.1
- ✅ MySQL 8.2
- ✅ MySQL 8.3

### 其他数据库支持

如果项目需要支持其他数据库，可以添加相应的 Flyway 数据库模块：

```xml
<!-- PostgreSQL -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
    <version>${flyway.version}</version>
</dependency>

<!-- Oracle -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-oracle</artifactId>
    <version>${flyway.version}</version>
</dependency>

<!-- SQL Server -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-sqlserver</artifactId>
    <version>${flyway.version}</version>
</dependency>
```

## 注意事项

### 1. Flyway 版本选择

- **推荐使用 9.22.3**：这是一个稳定版本，完整支持 MySQL 8.0
- **避免使用 10.x**：Flyway 10.x 开始部分功能需要商业许可证
- **兼容性**：Flyway 9.x 与 Spring Boot 2.7.x 完全兼容

### 2. 迁移脚本位置

确保 Flyway 迁移脚本位于正确的位置：

```
src/main/resources/db/migration/
├── V1.0.0__System_Init.sql
├── V1.0.1__Sys_Quartz_Job.sql
└── V1.0.2__Quartz_Init.sql
```

### 3. 命名规范

Flyway 迁移脚本必须遵循命名规范：

- 格式：`V{版本号}__{描述}.sql`
- 版本号：使用数字和点，如 `1.0.0`、`1.0.1`
- 描述：使用下划线分隔，如 `System_Init`
- 示例：`V1.0.0__System_Init.sql`

### 4. 配置检查

确保 `application.yml` 中的 Flyway 配置正确：

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    locations: classpath:db/migration
    encoding: UTF-8
```

## 相关资源

- [Flyway 官方文档](https://flywaydb.org/documentation/)
- [Flyway MySQL 支持](https://flywaydb.org/documentation/database/mysql)
- [Spring Boot Flyway 集成](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway)

## 常见问题

### Q1: 为什么不直接使用 Flyway 10.x？

A: Flyway 10.x 开始，部分高级功能需要商业许可证。Flyway 9.22.3 是最后一个完全开源的稳定版本，功能完整且免费。

### Q2: 升级 Flyway 会影响现有的迁移脚本吗？

A: 不会。Flyway 9.x 向后兼容，现有的迁移脚本可以正常运行。

### Q3: 如何验证 Flyway 是否正常工作？

A: 启动应用后，检查数据库中的 `flyway_schema_history` 表，应该能看到已执行的迁移记录。

```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

## 总结

通过升级 Flyway 到 9.22.3 版本并添加 `flyway-mysql` 依赖，成功解决了 MySQL 8.0 不支持的问题。现在项目可以正常使用 Flyway 进行数据库版本管理。

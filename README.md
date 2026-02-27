# 民宿自然教育赋能系统 - 后端服务

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg" alt="Spring Boot">
  <img src="https://img.shields.io/badge/JDK-17-orange.svg" alt="JDK">
  <img src="https://img.shields.io/badge/MySQL-8.0-blue.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/MyBatis-3.0.3-yellow.svg" alt="MyBatis">
  <img src="https://img.shields.io/badge/Sa--Token-Auth-red.svg" alt="Sa-Token">
  <img src="https://img.shields.io/badge/Maven-Build-blueviolet.svg" alt="Maven">
</p>

## 📋 项目简介

民宿自然教育赋能系统是一个基于 Spring Boot 3.x 开发的全栈应用，专为民宿行业设计，提供自然教育活动方案的管理与执行支持。系统包含完整的 RBAC 权限管理、活动方案管理、员工管理等功能模块，支持管理员后台和微信小程序双端访问。

### ✨ 核心功能

- **👤 员工与权限管理** - 基于 RBAC 模型的完整权限控制体系
- **📋 活动方案管理** - 自然教育活动方案的 CRUD 操作
- **🔐 安全认证** - 基于 Sa-Token 的会话管理与权限验证
- **📱 多端支持** - 同时支持管理后台 API 和微信小程序 API
- **📚 接口文档** - 集成 Knife4j 自动生成 API 文档

---

## 🏗️ 技术架构

### 技术栈

| 层级 | 技术选型 | 版本 |
|------|---------|------|
| 基础框架 | Spring Boot | 3.5.9 |
| JDK 版本 | Java | 17 |
| 数据库 | MySQL | 8.0.33 |
| 连接池 | Druid | 1.2.1 |
| ORM 框架 | MyBatis | 3.0.3 |
| 安全框架 | Sa-Token | 最新版 |
| 接口文档 | Knife4j + SpringDoc | 4.5.0 |
| 工具类库 | Hutool | 5.8.11 |
| 分页插件 | PageHelper | 1.4.7 |
| 构建工具 | Maven | - |

### 项目结构

```
mingSu/
├── homestay-common/          # 公共模块 - 常量、枚举、异常、工具类
│   ├── constant/             # 常量定义
│   ├── enumeration/          # 枚举类
│   ├── exception/            # 自定义异常
│   ├── json/                 # JSON 处理
│   └── result/               # 统一响应结果
│
├── homestay-pojo/            # 实体类模块 - DTO、Entity、VO
│   ├── annotation/           # 自定义注解
│   ├── aspect/               # AOP 切面
│   ├── dto/                  # 数据传输对象
│   │   ├── ActivityDTO/      # 活动相关 DTO
│   │   └── LoginDTO/         # 登录相关 DTO
│   ├── entity/               # 数据库实体
│   │   ├── ActivityPO/       # 活动实体
│   │   └── LoginPO/          # 登录相关实体
│   └── vo/                   # 视图对象
│
├── homestay-service/         # 主服务模块 - 业务逻辑层
│   ├── config/               # 配置类
│   ├── controller/           # 控制器
│   │   ├── admin/            # 管理端接口
│   │   └── user/             # 用户端接口
│   ├── handler/              # 全局异常处理器
│   ├── mapper/               # MyBatis Mapper
│   ├── service/              # 业务服务层
│   │   └── Impl/             # 服务实现类
│   └── resources/
│       ├── mapper/           # MyBatis XML 映射文件
│       └── application*.yaml # 配置文件
│
└── docs/                     # 项目文档
    ├── plan/                 # 规划文档
    └── uml/                  # UML 设计图
```

---

## 🚀 快速开始

### 环境要求

- **JDK**: 17 或更高版本
- **MySQL**: 8.0 或更高版本
- **Maven**: 3.6 或更高版本

### 1. 克隆项目

```bash
git clone <repository-url>
cd mingSu
```

### 2. 数据库配置

创建数据库并导入初始数据：

```sql
CREATE DATABASE nature_education_activity CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

在 `homestay-service/src/main/resources/application-dev.yaml` 中配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nature_education_activity?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: your_password
```

### 3. 编译运行

```bash
# 编译项目
mvn clean install

# 运行服务
cd homestay-service
mvn spring-boot:run
```

或使用 IDEA 直接运行 `HomestayServiceApplication` 主类。

### 4. 访问接口文档

服务启动后，访问以下地址查看 API 文档：

- **Knife4j 文档**: http://localhost:8080/doc.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## 📦 模块说明

### homestay-common 模块

公共基础模块，包含项目通用的常量、枚举、异常和工具类。

**核心组件**：
- `Result<T>` - 统一 API 响应封装
- `PageResult` - 分页结果封装
- `BaseException` - 基础异常类
- `MessageConstant` - 消息常量
- `JacksonObjectMapper` - 自定义 JSON 序列化

### homestay-pojo 模块

数据对象模块，定义所有与数据交互相关的类。

**包结构**：
- `entity` - 数据库表对应的实体类
- `dto` - 数据传输对象（接收请求参数）
- `vo` - 视图对象（返回给前端的数据）
- `annotation` + `aspect` - 自动填充创建/更新时间

### homestay-service 模块

核心业务模块，包含所有的业务逻辑和接口实现。

**功能模块**：
- **员工管理** - 员工 CRUD、登录、密码修改
- **角色管理** - 角色定义与权限分配
- **权限管理** - 菜单/按钮级权限控制
- **活动方案管理** - 自然教育活动方案的完整生命周期管理

---

## 🔐 权限设计

系统采用标准的 RBAC（Role-Based Access Control）权限模型：

```
用户(Employee) ←→ 角色(Role) ←→ 权限(Permission)
```

### 权限标识规范

权限标识采用 `模块:资源:操作` 的格式：

| 权限标识 | 说明 |
|---------|------|
| `system:employee:list` | 查看员工列表 |
| `system:employee:add` | 新增员工 |
| `system:employee:update` | 编辑员工 |
| `system:employee:delete` | 删除员工 |
| `system:role:list` | 查看角色列表 |
| `system:permission:list` | 查看权限列表 |
| `activity:plan:list` | 查看活动方案 |
| `activity:plan:add` | 创建活动方案 |

---

## ⚙️ 配置文件

### application.yaml（主配置）

```yaml
server:
  port: 8080

spring:
  application:
    name: homestay-service
  profiles:
    active: dev  # 激活开发环境配置

# MyBatis 配置
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.tanyde.entity
  configuration:
    map-underscore-to-camel-case: true  # 开启驼峰命名转换
```

### Sa-Token 配置

```yaml
sa-token:
  token-name: satoken
  timeout: 2592000          # Token 有效期 30 天
  is-concurrent: true       # 允许同一账号多地登录
  is-share: false           # 每次登录新建 Token
  token-style: uuid         # Token 风格
```

---

## 📡 API 接口概览

### 管理端接口（/admin）

| 接口路径 | 方法 | 功能 |
|---------|------|------|
| `/admin/employee/login` | POST | 员工登录 |
| `/admin/employee/logout` | POST | 员工登出 |
| `/admin/employee` | GET/POST/PUT/DELETE | 员工管理 |
| `/admin/role` | GET/POST/PUT/DELETE | 角色管理 |
| `/admin/permission` | GET | 权限管理 |
| `/admin/activity/plan` | GET/POST/PUT/DELETE | 活动方案管理 |

### 用户端接口（/user）

| 接口路径 | 方法 | 功能 |
|---------|------|------|
| `/user/activity/plans` | GET | 获取活动方案列表 |
| `/user/activity/plan/{id}` | GET | 获取方案详情 |
| `/user/activity/plan/{id}/step/{step}` | GET | 获取执行步骤 |

---

## 🧪 测试

```bash
# 运行单元测试
mvn test

# 跳过测试编译
mvn clean install -DskipTests
```

---

## 📚 相关文档

- [项目简介](./docs/plan/项目简介.txt)
- [RBAC 权限设计](./docs/plan/rbac.txt)
- [数据库设计 UML](./docs/uml/数据库设计V2.0.puml)
- [API 文档](../doc/api.yaml)

---

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

---

## 📄 许可证

[MIT](LICENSE) © 2025 Tanyde Team

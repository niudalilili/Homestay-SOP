# 民宿自然教育赋能系统后端 (mingSu)

> 基于 Spring Boot 3 + MyBatis-Plus + Sa-Token 的民宿活动方案管理系统，为管理端和小程序提供统一的 RESTful API。

[![JDK](https://img.shields.io/badge/JDK-17-orange.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.7-brightgreen.svg)](https://baomidou.com/)
[![Sa-Token](https://img.shields.io/badge/Sa--Token-1.45.0-brightgreen.svg)](https://sa-token.cc/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0-red.svg)](https://redis.io/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

---

## 目录

- [项目简介](#-项目简介)
- [技术架构](#-技术架构)
- [核心功能](#-核心功能)
- [技术亮点](#-技术亮点)
- [快速开始](#-快速开始)
- [项目结构](#-项目结构)
- [API 接口](#-api-接口)
- [数据库设计](#-数据库设计)
- [性能优化](#-性能优化)
- [环境要求](#-环境要求)

---

## 项目简介

**mingSu** 是一个面向民宿自然教育场景的活动方案管理系统，帮助民宿管理者高效创建、管理和展示自然教育活动方案。

### 业务场景

- **活动方案管理**：支持创建包含目标、材料、步骤的完整活动方案
- **多维度筛选**：按季节、年龄段、活动类别、场景等维度快速检索
- **状态流转**：草稿 → 上线 → 下线 的全生命周期管理
- **双端支持**：同时服务管理后台（React）和小程序（Taro）

---

## 技术架构

```
┌─────────────────────────────────────────────────────────────┐
│                        Client Layer                         │
│              ┌──────────────┐    ┌──────────────┐          │
│              │  管理后台 React │    │  小程序 Taro  │          │
│              └──────┬───────┘    └──────┬───────┘          │
│                     │                   │                   │
│                     └────────┬──────────┘                   │
│                              │                              │
│                         RESTful API                         │
└──────────────────────────────┼──────────────────────────────┘
                               │
┌──────────────────────────────┼──────────────────────────────┐
│                    Spring Boot 3.5.9                        │
│  ┌────────────────────────────┼─────────────────────────┐  │
│  │         Controller Layer   │  @SaCheckRole, @Valid   │  │
│  ├────────────────────────────┼─────────────────────────┤  │
│  │         Service Layer      │  Transaction, AOP       │  │
│  ├────────────────────────────┼─────────────────────────┤  │
│  │         Mapper Layer       │  MyBatis-Plus, XML      │  │
│  └────────────────────────────┼─────────────────────────┘  │
│                               │                            │
│  ┌────────────────────────────┴─────────────────────────┐  │
│  │                   Data Layer                         │  │
│  │    ┌──────────────┐           ┌──────────────┐      │  │
│  │    │  MySQL 8.0   │           │   Redis 6.0  │      │  │
│  │    │  (主数据)     │           │  (缓存/Session)│      │  │
│  │    └──────────────┘           └──────────────┘      │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### 核心技术栈

| 类别 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **后端框架** | Spring Boot | 3.5.9 | 核心框架 |
| **ORM** | MyBatis-Plus | 3.5.7 | 数据持久化 |
| **认证授权** | Sa-Token | 1.45.0 | 登录/权限控制 |
| **数据库** | MySQL | 8.0.33 | 关系型数据存储 |
| **缓存** | Redis | 6.0+ | 缓存/分布式 Session |
| **连接池** | Druid | 1.2.1 | 数据库连接池 |
| **API 文档** | Knife4j | 4.5.0 | 接口文档与调试 |
| **对象存储** | 阿里云 OSS | - | 图片/文件存储 |
| **构建工具** | Maven | 3.9+ | 项目构建与依赖管理 |

---

## 核心功能

### 管理端 (`/admin`)

| 模块 | 功能 | 接口路径 |
|------|------|----------|
| **认证** | 员工登录/退出、密码修改 | `/admin/employee/login` |
| **员工管理** | 员工 CRUD、状态管理、分页查询 | `/admin/employee` |
| **活动方案** | 方案 CRUD、状态流转、仪表盘统计 | `/admin/activityPlan` |
| **角色权限** | 角色管理、权限分配、页面授权 | `/admin/role`, `/admin/permission` |
| **收藏管理** | 收藏列表、取消收藏 | `/admin/favorite` |
| **反馈管理** | 反馈列表、状态处理 | `/admin/feedback` |

### 用户端 (`/user`)

| 模块 | 功能 | 接口路径 |
|------|------|----------|
| **微信登录** | 微信授权登录、用户信息同步 | `/user/auth/wx-login` |
| **活动列表** | 分页查询、多维度筛选、缓存加速 | `/user/activityPlan/page` |
| **活动详情** | 完整方案信息（含步骤） | `/user/activityPlan/{id}` |
| **收藏** | 添加/取消收藏、收藏列表 | `/user/favorite` |
| **反馈** | 提交反馈、反馈列表 | `/user/feedback` |

---

## 技术亮点

### 1. Redis 缓存加速

**场景**：活动列表和详情页高频读取

```java
// 详情页缓存 - String 结构
String cacheKey = "activity:detail:" + id;
redisService.set(cacheKey, activityPlanDTO, 3600);

// 列表页缓存 - String 结构（含查询条件）
String cacheKey = "activity:list:" + buildListCacheKey(dto, page, pageSize);
redisService.set(cacheKey, pageResult, 1800);
```

**设计要点**：
- **数据结构选择**：采用 `String` 存储序列化后的 JSON，而非 Hash
  - 整体读写，无需字段级操作
  - 嵌套对象（内容、步骤）天然适合 JSON 存储
  - 相比 Hash 节省 40%+ 内存
- **缓存策略**：更新时删除缓存，而非更新缓存
  - 避免更新缓存时需重新联表查询
  - 利用 TTL 兜底（详情 1h/列表 30min）
- **Key 设计**：列表缓存 Key 包含完整查询条件，实现精准缓存

### 2. 事务性缓存一致性

**问题**：事务回滚时缓存已删除，导致脏数据

**解决方案**：使用 Spring 事务同步机制

```java
@Transactional(rollbackFor = Exception.class)
public void update(ActivityPlanDTO dto) {
    // 1. 数据库操作
    activityPlanMapper.updateById(activityPlan);

    // 2. 注册事务提交后回调
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisService.delete(cacheKey); // 事务提交后再删缓存
            }
        }
    );
}
```

### 3. MyBatis-Plus 深度集成

**自动填充审计字段**

```java
// 自定义注解标记需要自动填充的实体
@AutoFill(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
public class ActivityPlan {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
}

// AOP 切面实现自动填充
@Aspect
@Component
public class AutoFillAspect {
    @Before("@annotation(autoFill)")
    public void autoFill(JoinPoint point, AutoFill autoFill) {
        // 通过反射自动设置审计字段
    }
}
```

**构造器注入替代字段注入**

```java
@Service
@RequiredArgsConstructor  // Lombok 生成构造器
public class ActivityPlanServiceImpl implements ActivityPlanService {
    private final ActivityPlanMapper activityPlanMapper;  // final + 构造器注入
    private final RedisService redisService;
}
```

### 4. 位运算优化多维度筛选

**季节字段使用位掩码存储**

```sql
-- season 字段：BIT(4)
-- 1=春，2=夏，4=秋，8=冬
-- 支持多选：春 + 夏 = 3，全季 = 15
```

```java
// 按季节筛选（位运算）
if (dto.getSeason() != null) {
    queryWrapper.apply("(season & {0}) != 0", dto.getSeason());
}
```

### 5. 统一异常处理

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleBaseException(BaseException ex) {
        log.warn("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(Exception ex) {
        log.error("系统异常：", ex);
        return Result.error("系统繁忙，请稍后再试");
    }
}
```

### 6. Sa-Token 灵活权限控制

```java
// 角色权限检查
@SaCheckRole("admin")
@PostMapping
public Result save(@RequestBody ActivityPlanDTO dto) { ... }

// 登录状态检查
@SaCheckLogin
@GetMapping("/page")
public Result<PageResult> pageQuery(...) { ... }

// 获取当前登录 ID
Long employeeId = StpUtil.getLoginIdAsLong();
```

---

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/your-username/mingSu.git
cd mingSu
```

### 2. 配置环境变量

```bash
# 创建 .env 文件或配置环境变量
export ty.db.host=localhost
export ty.db.pw=your_password
export ty.redis.host=localhost
export ty.redis.port=6379
export ty.redis.database=0
export ty.redis.pw=your_redis_password
# 阿里云 OSS 配置
export ty.alioss.endpoint=oss-cn-hangzhou.aliyuncs.com
export ty.alioss.access-key-id=your_access_key
export ty.alioss.access-key-secret=your_access_secret
export ty.alioss.bucket-name=your_bucket_name
# 微信小程序配置
export wx.appid=your_wx_appid
export wx.secret=your_wx_secret
```

### 3. 初始化数据库

```sql
CREATE DATABASE nature_education_activity DEFAULT CHARACTER SET utf8mb4;
```

### 4. 构建并启动

```bash
# 安装依赖
mvn clean install -DskipTests

# 启动服务
mvn -pl homestay-service spring-boot:run
```

### 5. 验证启动

```bash
# 访问健康检查接口
curl http://localhost:8080/admin/employee/login

# 访问 API 文档
open http://localhost:8080/doc.html
```

---

## 项目结构

```
mingSu/
├── homestay-common/           # 公共模块
│   ├── src/main/java/com/tanyde/
│   │   ├── annotation/        # 自定义注解 (@AutoFill)
│   │   ├── aspect/            # AOP 切面 (自动填充)
│   │   ├── constant/          # 常量定义 (RedisConstant, MessageConstant)
│   │   ├── enumeration/       # 枚举类 (ActivityStatus, OperationType)
│   │   ├── exception/         # 自定义异常 (BaseException)
│   │   ├── json/              # JSON 配置 (JacksonObjectMapper)
│   │   ├── result/            # 统一返回体 (Result, PageResult)
│   │   └── utils/             # 工具类 (BatchCopyUtil)
│   └── pom.xml
│
├── homestay-pojo/             # 数据模型模块
│   ├── src/main/java/com/tanyde/
│   │   ├── dto/               # 数据传输对象
│   │   │   ├── ActivityDTO/   # 活动相关 DTO
│   │   │   ├── EmployeeDTO/   # 员工相关 DTO
│   │   │   └── PageDTO/       # 分页查询 DTO
│   │   ├── entity/            # 数据库实体
│   │   │   ├── ActivityPO/    # 活动实体
│   │   │   └── Employee.java  # 员工实体
│   │   └── vo/                # 视图对象
│   └── pom.xml
│
├── homestay-service/          # 业务服务模块
│   ├── src/main/java/com/tanyde/
│   │   ├── config/            # 配置类 (RedisConfig, WebConfig)
│   │   ├── controller/        # 控制器层
│   │   │   ├── admin/         # 管理端接口
│   │   │   └── user/          # 用户端接口
│   │   ├── handler/           # 异常处理器 (GlobalExceptionHandler)
│   │   ├── mapper/            # 数据访问层
│   │   ├── service/           # 服务层接口
│   │   │   └── Impl/          # 服务实现类
│   │   └── interceptor/       # 拦截器
│   ├── src/main/resources/
│   │   ├── mapper/            # MyBatis XML 映射文件
│   │   └── application.yaml   # 应用配置
│   └── pom.xml
│
└── docs/                      # 文档目录
    ├── plans/                 # 设计文档
    ├── uml/                   # UML 图 (PlantUML)
    └── *.jmx                  # JMeter 压测脚本
```

---

## API 接口

### 接口前缀

| 端点 | 前缀 | 说明 |
|------|------|------|
| 管理端 | `/admin/**` | 管理员/员工使用 |
| 用户端 | `/user/**` | 小程序用户使用 |

### 核心接口

#### 活动方案管理

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/admin/activityPlan` | 新增活动方案 |
| PUT | `/admin/activityPlan/update` | 更新活动方案 |
| DELETE | `/admin/activityPlan/batch` | 批量删除 |
| GET | `/admin/activityPlan/{id}` | 查询详情 |
| GET | `/admin/activityPlan/page` | 分页查询 |
| PUT | `/admin/activityPlan/status/{status}` | 修改状态 |

#### 用户活动接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/user/activityPlan/page` | 分页查询（带缓存） |
| GET | `/user/activityPlan/{id}` | 查询详情（带缓存） |

### API 文档

启动后访问：
- **Knife4j UI**: http://localhost:8080/doc.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

---

## 数据库设计

### 核心表

#### activity_plan (活动方案主表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| plan_name | VARCHAR(100) | 方案名称 |
| min_age | INT | 最小年龄 |
| max_age | INT | 最大年龄 |
| activity_category | VARCHAR(50) | 活动类别 |
| season | BIT(4) | 适合季节（位掩码） |
| duration | INT | 活动时长（分钟） |
| scene | BIT(2) | 活动场景 |
| status | TINYINT | 状态（1=草稿 2=上线 3=下线） |

#### activity_plan_content (扩展内容表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| activity_plan_id | BIGINT | 关联主表 ID |
| knowledge_goal | VARCHAR(500) | 知识目标 |
| emotion_goal | VARCHAR(500) | 情感目标 |
| materials | TEXT | 所需材料 |
| space_requirement | VARCHAR(500) | 场地要求 |

#### activity_step (活动步骤表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| activity_plan_id | BIGINT | 关联主表 ID |
| step_number | INT | 步骤顺序 |
| step_title | VARCHAR(100) | 步骤标题 |
| step_type | TINYINT | 步骤类型（1=讲解 2=总结） |
| knowledge_explanation | TEXT | 知识讲解 |
| action_desc | TEXT | 操作说明 |

---

## 性能优化

### 1. Redis 缓存策略

| 缓存类型 | Key 前缀 | 过期时间 | 数据结构 |
|---------|---------|---------|---------|
| 活动详情 | `activity:detail:` | 1 小时 | String |
| 活动列表 | `activity:list:` | 30 分钟 | String |
| 用户信息 | `user:info:` | 2 小时 | String |
| 登录凭证 | `wx:login:` | 7 天 | String |

### 2. 数据库优化

- **索引设计**：在 `status`、`activity_category`、`season` 等查询字段建立索引
- **批量操作**：使用 `batchInsert` 批量写入步骤数据
- **分页查询**：使用 MyBatis-Plus 分页，避免全表扫描

### 3. 连接池配置

```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 200  # 最大连接数
          max-idle: 10     # 最大空闲连接
          min-idle: 0      # 最小空闲连接
```

---

## 环境要求

| 依赖 | 最低版本 | 推荐版本 |
|------|---------|---------|
| JDK | 17+ | 17 |
| Maven | 3.9+ | 3.9.0 |
| MySQL | 8.0+ | 8.0.33 |
| Redis | 6.0+ | 7.0 |

---

## License

[MIT License](LICENSE)

---

## 联系方式

- **作者**: TanyDe
- **邮箱**: your-email@example.com

---

<div align="center">
  <strong>如果这个项目对你有帮助，请给一个 ⭐️ Star 支持！</strong>
</div>

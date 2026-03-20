# 民宿自然教育赋能系统后端（mingSu）

## 项目说明
该目录是后端服务，采用 Spring Boot + MyBatis-Plus + Sa-Token，向管理端（React）和小程序端（Taro）提供统一 API。

当前结构：

```text
mingSu/
├── homestay-common/   # 公共常量、异常、返回体、工具
├── homestay-pojo/     # DTO/Entity/VO、注解与切面
├── homestay-service/  # Controller/Service/Mapper 主业务模块
└── docs/              # 设计文档、UML、压测脚本
```

## 技术栈
- JDK 17
- Spring Boot 3.5.9
- MyBatis-Plus
- MySQL 8
- Redis
- Sa-Token
- Maven

## 环境要求
- JDK 17+
- Maven 3.9+
- MySQL 8+
- Redis 6+

## 配置说明
服务配置文件位置：

`homestay-service/src/main/resources/application.yaml`

该文件使用占位符读取敏感配置，启动前需要准备如下配置项：
- `ty.db.host`
- `ty.db.pw`
- `ty.redis.host`
- `ty.redis.port`
- `ty.redis.database`
- `ty.redis.pw`
- `ty.alioss.endpoint`
- `ty.alioss.access-key-id`
- `ty.alioss.access-key-secret`
- `ty.alioss.bucket-name`
- `wx.appid`
- `wx.secret`

建议通过环境变量或 IDE 启动参数注入，不要写死在仓库中。

## 本地启动
在 `mingSu/` 目录执行：

```bash
mvn clean install
mvn -pl homestay-service spring-boot:run
```

默认启动端口：`8080`

## 测试命令
在 `mingSu/` 目录执行：

```bash
mvn -pl homestay-service -am test -DskipTests=false
```

## 接口入口
- 管理端接口前缀：`/admin`
- 用户端接口前缀：`/user`

关键模块：
- 员工/角色/权限管理
- 活动方案管理（方案、内容、步骤）
- 收藏管理
- 反馈管理
- 微信登录与用户信息

## API 文档
启用后可访问：
- Knife4j：`http://localhost:8080/doc.html`
- OpenAPI：`http://localhost:8080/v3/api-docs`

## 与前端联调
- 管理后台默认对接该服务的 `/admin/**`
- 小程序默认对接该服务的 `/user/**`
- 跨端接口字段以 DTO/VO 为准，涉及字段调整时优先同步 `homestay-pojo` 与前端 `services/types`

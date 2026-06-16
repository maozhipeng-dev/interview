# 面试题网站

一个基于 Spring Boot + Vue 3 的面试题管理系统，支持题目管理、分类筛选、难度筛选，以及从多个平台爬虫获取题目。

## 技术栈

### 后端
- Spring Boot 3.2.5
- Spring Data JPA
- Spring Security
- Redis
- MySQL/H2 Database
- Jsoup (爬虫)
- Resilience4j (限流)
- SpringDoc OpenAPI (API 文档)
- Alibaba Bailian SDK (AI 面试)

### 前端
- Vue 3
- Vite
- Element Plus
- Axios

## 功能特性

- ✅ 面试题的增删改查
- ✅ 按分类和难度筛选
- ✅ 分页查询
- ✅ 从多个平台爬虫获取题目
- ✅ 接口限流保护
- ✅ Swagger API 文档
- ✅ Docker 容器化部署
- ✅ 加载状态和错误提示
- ✅ 日志记录和文件输出
- 🆕 AI 智能面试题生成
- 🆕 AI 答案评估和建议
- 🆕 个性化面试配置（职位、经验、技术栈）

## 快速开始

### 本地开发

#### 后端
```bash
cd interview-backend
mvn spring-boot:run
```

服务启动后访问：
- API: http://localhost:8080/api
- Swagger 文档: http://localhost:8080/swagger-ui.html
- H2 控制台: http://localhost:8080/h2-console (仅开发环境)

#### 前端
```bash
cd interview-frontend
npm install
npm run dev
```

访问应用: http://localhost:5173

### Docker 部署

使用 Docker Compose 一键部署：

```bash
docker-compose up -d
```

访问应用：
- 前端: http://localhost
- 后端 API: http://localhost:8080/api

## AI 面试功能

### 配置

要使用 AI 面试功能，需要配置阿里云百炼 API Key：

1. 访问 [阿里云百炼控制台](https://bailian.console.aliyun.com/) 获取 API Key
2. 在环境变量中配置：
```bash
BAILIAN_API_KEY=your-api-key-here
BAILIAN_MODEL=qwen-turbo
```

### 使用说明

1. 点击导航栏中的 "AI 模拟面试"
2. 填写面试配置信息：
   - 应聘职位（必选）
   - 经验年限
   - 技术栈
   - 职位级别
   - 特殊要求
3. 点击 "开始 AI 面试"，系统会自动生成 5 道专业面试题
4. 逐题回答后提交，AI 会给出评分、评估和改进建议

### 主要功能

- **智能题目生成**：根据你的背景和目标职位定制面试题
- **实时评估**：AI 即时评估你的答案并给出评分
- **改进建议**：获得专业的面试建议和参考答案
- **面试建议**：获取整体面试技巧和注意事项

## 环境变量配置

复制 `.env.example` 为 `.env` 并根据需要修改配置：

```bash
cp .env.example .env
```

主要配置项：
- `SPRING_PROFILES_ACTIVE`: 激活的配置文件 (h2/mysql/prod)
- `DB_URL`: 数据库连接 URL
- `REDIS_HOST`: Redis 地址
- `LOG_LEVEL`: 日志级别 (INFO/DEBUG)
- `BAILIAN_API_KEY`: 阿里云百炼 API Key（AI 功能必需）
- `BAILIAN_MODEL`: AI 模型名称，默认 qwen-turbo

## API 文档

启动后端服务后访问 Swagger UI 查看完整 API 文档：
http://localhost:8080/swagger-ui.html

主要接口：
- `GET /api/questions`: 获取题目列表
- `POST /api/questions`: 创建题目
- `PUT /api/questions/{id}`: 更新题目
- `DELETE /api/questions/{id}`: 删除题目
- `GET /api/crawler/xxx`: 爬虫相关接口（已限流）
- `POST /api/ai-interview/generate`: 生成 AI 面试题
- `POST /api/ai-interview/evaluate`: AI 评估答案

## 项目结构

```
.
├── interview-backend/         # 后端项目
│   ├── src/main/java/
│   │   └── com/example/interview/
│   │       ├── config/        # 配置类
│   │       ├── controller/    # 控制器
│   │       ├── dto/           # 数据传输对象
│   │       ├── entity/        # 实体类
│   │       ├── exception/     # 异常处理
│   │       ├── repository/    # 数据访问层
│   │       └── service/       # 业务逻辑层
│   └── src/main/resources/    # 配置文件
├── interview-frontend/        # 前端项目
│   ├── src/
│   │   ├── api/               # API 请求
│   │   └── components/        # 组件
│   └── public/
├── .env.example               # 环境变量示例
├── .gitignore                 # Git 忽略文件
├── docker-compose.yml         # Docker 编排配置
└── README.md
```

## 部署建议

### 生产环境部署要点

1. **安全配置**
   - 使用 `prod` profile 启用 Spring Security
   - 配置 HTTPS 和 SSL 证书
   - 使用环境变量管理敏感信息

2. **数据库**
   - 使用 MySQL/PostgreSQL 替代 H2
   - 配置数据库连接池
   - 定期备份数据

3. **缓存**
   - 使用独立的 Redis 服务
   - 配置缓存过期策略

4. **日志**
   - 日志文件定期归档
   - 考虑使用日志收集服务（如 ELK）

5. **AI 服务**
   - 确保 API Key 安全存储
   - 监控 API 调用次数和费用
   - 考虑添加用户限流机制

## 许可证

MIT License

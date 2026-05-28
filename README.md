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

### 前端
- Vue 3
- Vite
- Element Plus
- Axios

## 快速开始

### 本地开发

#### 后端
```bash
cd interview-backend
mvn spring-boot:run
```

#### 前端
```bash
cd interview-frontend
npm install
npm run dev
```

### Docker 部署

使用 Docker Compose 一键部署：

```bash
docker-compose up -d
```

访问应用：
- 前端: http://localhost
- 后端 API: http://localhost:8080/api

## 环境变量配置

复制 `.env.example` 为 `.env` 并根据需要修改配置：

```bash
cp .env.example .env
```

主要配置项：
- `SPRING_PROFILES_ACTIVE`: 激活的配置文件 (h2/mysql/prod)
- `DB_URL`: 数据库连接 URL
- `REDIS_HOST`: Redis 地址

## 项目结构

```
.
├── interview-backend/     # 后端项目
│   ├── src/main/java/
│   └── src/main/resources/
├── interview-frontend/    # 前端项目
│   ├── src/
│   └── public/
└── docker-compose.yml     # Docker 编排配置
```


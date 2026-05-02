# AI Blog - 智能个人博客系统

一个现代化的个人博客系统，集成 AI 辅助写作功能，支持 Markdown 编辑、图片上传、文章置顶、评论管理等完整博客功能。

## 技术栈

### 后端
- **Spring Boot 3.4.0** - Java 17 基础框架
- **MyBatis-Plus 3.5.5** - ORM 框架
- **Spring AI** - DeepSeek AI 集成
- **JWT** - 身份认证
- **MySQL** - 数据存储

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **Vite 5** - 前端构建工具
- **Vue Router 4** - 路由管理
- **Pinia** - 状态管理
- **Markdown-it** - Markdown 渲染
- **Highlight.js** - 代码语法高亮

## 功能特性

### 用户端
- 博客文章列表与搜索
- 文章详情页（Markdown 渲染、代码高亮、图片查看器）
- 评论系统
- 文章点赞
- 护眼深色主题 / 浅色主题切换
- 响应式设计（桌面端、平板、移动端适配）

### 管理端
- 文章管理（增删改查、置顶、发布/草稿）
- AI 辅助写作（标题生成、内容续写、摘要生成）
- 分类管理
- 标签管理
- 评论管理
- 数据统计仪表盘
- 图片上传（支持拖拽、粘贴）

### 特色功能
- **AI 辅助写作**：集成 DeepSeek 大模型，支持智能标题生成、内容续写、文章摘要
- **专业代码块**：Gemini 风格语法高亮、行号显示、一键复制、折叠展开、自动换行
- **图片查看器**：点击放大、缩放、旋转、拖拽、键盘快捷键
- **护眼模式**：基于 Gemini 深色主题设计，符合 WCAG AAA 对比度标准
- **文章置顶**：支持文章置顶展示，列表优先排序
- **Toast 通知**：优雅的提示框替代原生 alert

## 项目结构

```
Ai_Blog/
├── backend/                    # 后端项目
│   ├── src/main/java/
│   │   └── com/aiblog/
│   │       ├── common/         # 通用类（JWT、全局异常处理）
│   │       ├── config/         # 配置类
│   │       ├── controller/     # 控制器
│   │       ├── entity/         # 实体类
│   │       ├── interceptor/    # JWT 拦截器
│   │       ├── mapper/         # MyBatis Mapper
│   │       └── service/        # 业务逻辑层
│   ├── src/main/resources/
│   │   ├── application.yml     # 配置文件
│   │   └── schema.sql          # 数据库初始化脚本
│   ├── uploads/                # 文件上传目录
│   └── pom.xml
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求封装
│   │   ├── components/         # 公共组件
│   │   ├── composables/        # 组合式函数
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── views/              # 页面组件
│   │   ├── App.vue             # 根组件
│   │   ── main.js             # 入口文件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
└── doc/                        # 文档目录
```

## 快速开始

### 环境要求
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 后端启动

1. 创建数据库并初始化表结构：
```bash
mysql -u root -p -e "CREATE DATABASE aiblog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p aiblog_db < backend/src/main/resources/schema.sql
```

2. 修改配置文件 `backend/src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
  ai:
    deepseek:
      api-key: your_deepseek_api_key
```

3. 启动后端服务：
```bash
cd backend
mvn spring-boot:run
```

后端服务运行在 `http://localhost:8080`

### 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务运行在 `http://localhost:3000`

### 构建生产版本

```bash
# 前端构建
cd frontend
npm run build

# 后端打包
cd backend
mvn clean package
```

## 配置说明

### JWT 配置
```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24小时
```

### 文件上传配置
```yaml
upload:
  path: uploads/images
  max-size: 10485760  # 10MB
```

### AI 配置
```yaml
spring:
  ai:
    deepseek:
      api-key: sk-xxx
      chat:
        options:
          model: deepseek-chat
          temperature: 0.3
          max-tokens: 1000
```

## API 接口

### 公开接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/articles` | 获取文章列表 |
| GET | `/api/articles/{id}` | 获取文章详情 |
| GET | `/api/articles/search` | 搜索文章 |
| POST | `/api/articles/{id}/like` | 点赞文章 |
| GET | `/api/comments/{articleId}` | 获取文章评论 |
| POST | `/api/comments` | 发表评论 |
| POST | `/api/auth/login` | 管理员登录 |
| POST | `/api/uploads/images` | 上传图片 |

### 管理接口（需 JWT 认证）
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/admin/articles` | 获取文章列表 |
| POST | `/api/admin/articles` | 创建文章 |
| PUT | `/api/admin/articles/{id}` | 更新文章 |
| DELETE | `/api/admin/articles/{id}` | 删除文章 |
| POST | `/api/admin/articles/{id}/top` | 设置文章置顶 |
| POST | `/api/admin/articles/{id}/untop` | 取消文章置顶 |
| GET/POST/PUT/DELETE | `/api/admin/categories` | 分类管理 |
| GET/POST/PUT/DELETE | `/api/admin/tags` | 标签管理 |
| GET/DELETE | `/api/admin/comments` | 评论管理 |
| GET | `/api/admin/statistics` | 统计数据 |
| POST | `/api/admin/ai/generate-title` | AI 生成标题 |
| POST | `/api/admin/ai/generate-content` | AI 生成内容 |
| POST | `/api/admin/ai/generate-summary` | AI 生成摘要 |

## 数据库设计

### 核心表
- **user** - 管理员用户表
- **article** - 文章表（含 `is_top` 置顶字段）
- **category** - 分类表
- **tag** - 标签表
- **comment** - 评论表

## 主题设计

### 浅色模式
- 白色背景，蓝色强调色
- 适合白天阅读

### 护眼深色模式
- 基于 Gemini 深色主题设计
- 深空灰背景 (#1e1e1e)
- 高对比度暖白文字 (#e8e6e1)
- 柔和蓝绿强调色 (#7eb8a0)
- 符合 WCAG AAA 对比度标准
- 减少蓝光辐射，降低视觉疲劳

## 代码块设计

- Gemini 风格语法高亮（One Dark 配色）
- 行号显示（半透明效果，悬停高亮）
- 语言标识与行数统计
- 一键复制功能
- 折叠/展开功能
- 自动换行切换
- 无横线简洁设计
- 跟随系统主题自动适配

## 许可证

MIT License

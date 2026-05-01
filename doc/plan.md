我来为你规划这个"AI + 个人博客系统"项目。首先让我了解一下当前项目的状态。


        
            
toolName: view_folder
            
status: success
          
f:\projects\Ai_Blog
          
好的，项目目录为空，我们将从零开始。以下是完整的项目规划：

---

## 1. 项目理解

这是一个**面向个人博主的 AI 增强型博客系统**，核心特点：

| 维度 | 说明 |
|------|------|
| **用户角色** | 单用户（管理员）+ 游客（仅浏览/评论） |
| **核心价值** | 用 AI 降低内容创作成本（自动摘要、自动标签、问答助手） |
| **设计哲学** | 内容优先、极简主义、Ghost 风格 |
| **技术架构** | 前后端分离，RESTful API 通信 |

### 技术选型理由

- **Vue3 + Vite**：轻量、快速、组合式 API 适合中小型项目
- **Pinia**：Vue3 官方推荐状态管理，比 Vuex 更简单
- **Spring Boot 3**：成熟稳定，生态完善
- **MyBatis-Plus**：减少样板 SQL 代码，开发效率高
- **JWT**：无状态鉴权，前后端分离最佳实践

---

## 2. MVP 范围

### ✅ 包含（MVP）

| 模块 | 功能 |
|------|------|
| 文章系统 | 创建、编辑、删除、草稿、发布、列表、详情 |
| 分类与标签 | 创建、编辑、删除、文章关联多标签 |
| 搜索 | 按标题搜索、按标签搜索 |
| 评论系统 | 游客评论、后台审核（通过/拒绝） |
| 统计 | 阅读量、点赞 |
| UI | 深色模式、响应式布局 |
| AI 功能 | 自动生成摘要、自动生成标签 |
| 鉴权 | 管理员 JWT 登录 |

### ❌ 不包含（后续迭代）

- 博客问答助手（需要 RAG/向量数据库，MVP 后做）
- 图片上传/CDN（先用占位图）
- RSS 订阅
- SEO 优化
- 多语言
- 自定义主题

---

## 3. 目录结构

```
Ai_Blog/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/aiblog/
│   │   ├── AiBlogApplication.java    # 启动类
│   │   ├── config/                   # 配置类
│   │   │   ├── SecurityConfig.java   # 安全配置（JWT）
│   │   │   ├── CorsConfig.java       # 跨域配置
│   │   │   └── MybatisPlusConfig.java
│   │   ├── controller/               # REST 控制器
│   │   │   ├── AuthController.java
│   │   │   ├── ArticleController.java
│   │   │   ├── CategoryController.java
│   │   │   ├── TagController.java
│   │   │   ├── CommentController.java
│   │   │   └── AiController.java
│   │   ├── entity/                   # 实体类
│   │   │   ├── Article.java
│   │   │   ├── Category.java
│   │   │   ├── Tag.java
│   │   │   ├── ArticleTag.java       # 多对多关联
│   │   │   ├── Comment.java
│   │   │   └── User.java
│   │   ├── mapper/                   # MyBatis-Plus Mapper
│   │   ├── service/                  # 业务逻辑
│   │   │   └── impl/
│   │   ├── dto/                      # 数据传输对象
│   │   ├── vo/                       # 视图对象
│   │   ├── common/                   # 通用类
│   │   │   ├── Result.java           # 统一响应
│   │   │   └── JwtUtil.java
│   │   └── interceptor/              # 拦截器
│   │       └── JwtInterceptor.java
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── schema.sql                # 数据库初始化
│   └── pom.xml
│
├── frontend/                         # Vue3 前端
│   ├── src/
│   │   ├── api/                      # API 请求封装
│   │   ├── assets/                   # 静态资源
│   │   ├── components/               # 通用组件
│   │   │   ├── Header.vue
│   │   │   ├── Footer.vue
│   │   │   ├── ArticleCard.vue
│   │   │   ├── MarkdownEditor.vue
│   │   │   └── CommentSection.vue
│   │   ├── views/                    # 页面
│   │   │   ├── Home.vue              # 首页/文章列表
│   │   │   ├── ArticleDetail.vue     # 文章详情
│   │   │   ├── Admin/
│   │   │   │   ├── Login.vue
│   │   │   │   ├── Dashboard.vue
│   │   │   │   ├── ArticleEdit.vue
│   │   │   │   ├── CategoryManage.vue
│   │   │   │   ├── TagManage.vue
│   │   │   │   └── CommentManage.vue
│   │   │   └── Search.vue
│   │   ├── stores/                   # Pinia 状态
│   │   │   ├── auth.js
│   │   │   └── theme.js
│   │   ├── router/
│   │   │   └── index.js
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
└── README.md
```

---

## 4. 核心类职责

### 后端

| 类 | 职责 |
|----|------|
| `ArticleController` | 文章的 CRUD、列表分页、详情（含阅读量+1） |
| `ArticleService` | 文章业务逻辑，状态管理（草稿/发布） |
| `CommentController` | 评论提交、审核列表、审核操作 |
| `AiController` | 调用 AI API 生成摘要和标签 |
| `JwtInterceptor` | 拦截需要认证的请求，验证 JWT |
| `Result<T>` | 统一 API 响应格式 `{code, message, data}` |

### 前端

| 组件/模块 | 职责 |
|-----------|------|
| `auth store` | 登录状态、token 管理 |
| `theme store` | 深色/浅色模式切换 |
| `MarkdownEditor` | 封装 Markdown 编辑器（使用 md-editor-v3） |
| `ArticleCard` | 文章卡片展示（标题、摘要、标签、阅读量） |
| `ArticleEdit` | 后台文章创建/编辑页面 |
| `CommentManage` | 后台评论审核列表 |

---

## 5. 开发阶段拆分

| 阶段 | 内容 | 可交付物 |
|------|------|----------|
| **Phase 1** | 项目初始化 + 数据库 + 文章 CRUD | 后端可运行，文章 API 可用 |
| **Phase 2** | 前端初始化 + 文章列表/详情 | 前端可运行，能浏览文章 |
| **Phase 3** | 管理员登录 + JWT 鉴权 | 能登录后台 |
| **Phase 4** | 后台文章管理（编辑器） | 能创建/编辑/发布文章 |
| **Phase 5** | 分类与标签 | 完整的分类标签管理 |
| **Phase 6** | 搜索功能 | 标题/标签搜索 |
| **Phase 7** | 评论系统 | 游客评论 + 后台审核 |
| **Phase 8** | 统计功能 | 阅读量、点赞 |
| **Phase 9** | AI 功能 | 自动摘要、自动标签 |
| **Phase 10** | UI 完善 | 深色模式、响应式、Ghost 风格 |

-自动运行和测试，验证功能的实现
---

## 6. 第一阶段任务（Phase 1）

### 目标：后端项目初始化 + 文章基础 CRUD API

**具体任务：**

1. **初始化 Spring Boot 项目**
   - 创建 `pom.xml`（Spring Boot 3、MyBatis-Plus、MySQL、JWT 依赖）
   - 创建启动类 `AiBlogApplication.java`
   - 创建 `application.yml` 配置

2. **数据库设计**
   - 创建 `schema.sql`：`article`、`category`、`tag`、`article_tag`、`comment`、`user` 表
   - 仅 MVP 必要字段

3. **实体类 + Mapper**
   - `Article.java` 实体
   - `ArticleMapper.java`

4. **统一响应格式**
   - `Result.java`

5. **文章 Controller + Service**
   - `ArticleController`：列表（分页）、详情、创建、更新、删除
   - `ArticleService` + `ArticleServiceImpl`

6. **初始化 Git 仓库并推送**

**不涉及：** 鉴权、AI、前端、评论（后续阶段做）

---

是否开始 Phase 1？我会先说明要创建/修改哪些文件，然后再写代码。
USE aiblog_db;

INSERT INTO article (title, content, summary, status, view_count, like_count) VALUES 
('Hello World - 我的第一篇博客', '# Hello World

这是我的第一篇博客文章。

## 关于AI博客

这是一个基于AI的个人博客系统，使用Vue3和Spring Boot构建。

### 代码示例

```python
print("Hello, AI Blog!")
```

> AI让写作更简单

- 支持Markdown
- AI自动摘要
- AI自动标签', '这是我的第一篇博客文章，介绍了AI博客系统的基本功能', 1, 10, 3);

INSERT INTO article (title, content, summary, status, view_count, like_count) VALUES 
('Vue3 组合式API入门指南', '# Vue3 组合式API入门

Vue3带来了全新的组合式API（Composition API）。

## 什么是组合式API？

组合式API是一组API，允许我们使用函数而不是选项的方式来组织组件逻辑。

## setup函数

```javascript
import { ref } from "vue"

export default {
  setup() {
    const count = ref(0)
    return { count }
  }
}
```

## 响应式数据

使用`ref`和`reactive`来创建响应式数据。', 'Vue3组合式API的快速入门教程，包含setup函数和响应式数据的使用', 1, 25, 8);

INSERT INTO article (title, content, summary, status, view_count, like_count) VALUES 
('Spring Boot 3 新特性解析', '# Spring Boot 3 新特性

Spring Boot 3带来了许多令人兴奋的新特性。

## Java 17 基线

Spring Boot 3最低要求Java 17。

## 原生支持

通过GraalVM实现原生编译。

## 观测性改进

内置Micrometer支持。', 'Spring Boot 3的主要新特性介绍', 1, 15, 5);

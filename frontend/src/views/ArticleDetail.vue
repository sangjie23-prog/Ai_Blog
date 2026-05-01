<template>
  <div class="article-detail" v-if="article">
    <!-- 文章头部 -->
    <header class="article-header">
      <h1 class="article-title">{{ article.title }}</h1>
      <div class="article-meta">
        <span class="meta-date">{{ formatDate(article.createdAt) }}</span>
        <span class="meta-views">{{ article.viewCount || 0 }} 阅读</span>
        <span class="meta-likes">{{ article.likeCount || 0 }} 点赞</span>
      </div>
    </header>

    <!-- 文章内容 -->
    <div class="article-content" v-html="renderedContent"></div>

    <!-- 点赞按钮 -->
    <div class="article-actions">
      <button class="like-btn" @click="handleLike">
        ❤️ 点赞 ({{ article.likeCount || 0 }})
      </button>
    </div>

    <!-- 评论区 -->
    <CommentForm :article-id="parseInt(route.params.id)" />
  </div>

  <!-- 加载中 -->
  <div v-else-if="loading" class="loading">加载中...</div>

  <!-- 文章不存在 -->
  <div v-else class="not-found">
    <h2>文章不存在</h2>
    <router-link to="/">返回首页</router-link>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, likeArticle } from '../api/article'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import CommentForm from '../components/CommentForm.vue'

const route = useRoute()
const article = ref(null)
const loading = ref(false)

// 初始化Markdown渲染器
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
               hljs.highlight(str, { language: lang }).value +
               '</code></pre>'
      } catch (__) {}
    }
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>'
  }
})

// 渲染Markdown内容为HTML
const renderedContent = computed(() => {
  if (!article.value || !article.value.content) return ''
  return md.render(article.value.content)
})

// 加载文章详情
async function loadArticle() {
  loading.value = true
  try {
    const res = await getArticleDetail(route.params.id)
    article.value = res.data
  } catch (error) {
    console.error('加载文章失败:', error)
    article.value = null
  } finally {
    loading.value = false
  }
}

// 处理点赞
async function handleLike() {
  try {
    await likeArticle(route.params.id)
    // 重新加载文章获取最新点赞数
    await loadArticle()
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  max-width: 800px;
  margin: 0 auto;
}

.article-header {
  margin-bottom: 40px;
  text-align: center;
}

.article-title {
  font-size: 36px;
  font-weight: 700;
  line-height: 1.3;
  margin-bottom: 16px;
}

.article-meta {
  display: flex;
  justify-content: center;
  gap: 20px;
  font-size: 14px;
  color: var(--text-secondary);
}

.article-content {
  font-size: 17px;
  line-height: 1.8;
}

/* Markdown内容样式 */
.article-content :deep(h1) {
  font-size: 28px;
  margin: 40px 0 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid var(--border-color);
}

.article-content :deep(h2) {
  font-size: 24px;
  margin: 32px 0 16px;
}

.article-content :deep(h3) {
  font-size: 20px;
  margin: 24px 0 12px;
}

.article-content :deep(p) {
  margin-bottom: 16px;
}

.article-content :deep(code) {
  background-color: var(--code-bg);
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.9em;
}

.article-content :deep(pre) {
  background-color: var(--code-bg);
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  margin-bottom: 16px;
}

.article-content :deep(pre code) {
  background: none;
  padding: 0;
}

.article-content :deep(blockquote) {
  border-left: 4px solid var(--accent-color);
  padding-left: 16px;
  margin: 16px 0;
  color: var(--text-secondary);
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 16px 0;
}

.article-content :deep(ul), .article-content :deep(ol) {
  padding-left: 24px;
  margin-bottom: 16px;
}

.article-content :deep(li) {
  margin-bottom: 8px;
}

.article-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
}

.article-content :deep(th), .article-content :deep(td) {
  border: 1px solid var(--border-color);
  padding: 8px 12px;
  text-align: left;
}

.article-content :deep(th) {
  background-color: var(--code-bg);
  font-weight: 600;
}

.article-actions {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
  text-align: center;
}

.like-btn {
  padding: 10px 24px;
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 24px;
  cursor: pointer;
  font-size: 15px;
  color: var(--text-color);
  transition: all 0.2s;
}

.like-btn:hover {
  background-color: #ff4757;
  color: white;
  border-color: #ff4757;
}

.loading, .not-found {
  text-align: center;
  padding: 60px 20px;
}

.not-found h2 {
  margin-bottom: 16px;
}

@media (max-width: 768px) {
  .article-title {
    font-size: 28px;
  }
  
  .article-content {
    font-size: 16px;
  }
  
  .article-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>

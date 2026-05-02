<template>
  <div class="article-detail" v-if="article">
    <header class="article-header animate-fade-in">
      <div class="article-header-inner">
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <time class="meta-item" :datetime="article.createdAt">
            <span class="meta-icon">📅</span>
            {{ formatDate(article.createdAt) }}
          </time>
          <span class="meta-item">
            <span class="meta-icon">👁️</span>
            {{ article.viewCount || 0 }} 阅读
          </span>
          <span class="meta-item">
            <span class="meta-icon">❤️</span>
            {{ article.likeCount || 0 }} 点赞
          </span>
        </div>
        <div class="article-tags" v-if="article.tags">
          <span 
            v-for="(tag, i) in article.tags.split(',')" 
            :key="i" 
            class="article-tag"
          >
            {{ tag.trim() }}
          </span>
        </div>
      </div>
    </header>

    <div class="article-divider"></div>

    <article class="article-content animate-slide-up" v-html="renderedContent"></article>

    <div class="article-footer animate-fade-in">
      <div class="article-actions">
        <button class="like-btn" @click="handleLike" aria-label="点赞">
          <span class="like-icon">❤️</span>
          <span class="like-text">点赞</span>
          <span class="like-count">{{ article.likeCount || 0 }}</span>
        </button>
      </div>
      
      <div class="article-share">
        <span class="share-label">分享文章</span>
        <div class="share-buttons">
          <button class="share-btn" @click="copyLink" aria-label="复制链接">
            🔗 复制链接
          </button>
        </div>
      </div>
    </div>

    <div class="article-divider"></div>

    <section class="article-comments">
      <h3 class="comments-title">评论</h3>
      <CommentForm :article-id="parseInt(route.params.id)" />
    </section>
  </div>

  <div v-else-if="loading" class="loading">
    <div class="loading-spinner"></div>
    <p class="loading-text">加载文章中...</p>
  </div>

  <div v-else class="not-found animate-fade-in">
    <div class="not-found-icon">📄</div>
    <h2 class="not-found-title">文章不存在</h2>
    <p class="not-found-desc">该文章可能已被删除或移动</p>
    <router-link to="/" class="back-link">← 返回首页</router-link>
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

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
               hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
               '</code></pre>'
      } catch (__) {}
    }
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>'
  }
})

const renderedContent = computed(() => {
  if (!article.value || !article.value.content) return ''
  let content = article.value.content
  // 兼容旧图片URL格式：将 /api/uploads/images/ 转换为 /api/uploads/
  content = content.replace(/\/api\/uploads\/images\//g, '/api/uploads/')
  return md.render(content)
})

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

async function handleLike() {
  try {
    await likeArticle(route.params.id)
    await loadArticle()
  } catch (error) {
    console.error('点赞失败:', error)
  }
}

function copyLink() {
  navigator.clipboard.writeText(window.location.href)
  alert('✅ 链接已复制到剪贴板')
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric'
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
  margin-bottom: var(--space-xl);
}

.article-header-inner {
  text-align: center;
}

.article-title {
  font-size: 2.5rem;
  font-weight: 700;
  line-height: 1.3;
  margin-bottom: var(--space-md);
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.article-meta {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-lg);
  flex-wrap: wrap;
  margin-bottom: var(--space-md);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  font-size: 0.875rem;
  color: var(--text-secondary);
}

.meta-icon {
  font-size: 14px;
  opacity: 0.7;
}

.article-tags {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-sm);
  flex-wrap: wrap;
}

.article-tag {
  display: inline-block;
  padding: var(--space-xs) var(--space-sm);
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--tag-text);
  background-color: var(--tag-bg);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.article-tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

.article-divider {
  height: 1px;
  background-color: var(--border-color);
  margin: var(--space-xl) 0;
}

.article-content {
  font-size: 1.0625rem;
  line-height: 1.8;
  color: var(--text-color);
}

.article-content :deep(h1) {
  font-size: 1.75rem;
  margin: var(--space-2xl) 0 var(--space-lg);
  padding-bottom: var(--space-sm);
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
}

.article-content :deep(h2) {
  font-size: 1.5rem;
  margin: var(--space-xl) 0 var(--space-md);
  font-weight: 600;
}

.article-content :deep(h3) {
  font-size: 1.25rem;
  margin: var(--space-lg) 0 var(--space-sm);
  font-weight: 600;
}

.article-content :deep(p) {
  margin-bottom: var(--space-md);
}

.article-content :deep(a) {
  color: var(--accent-color);
  text-decoration: underline;
  text-underline-offset: 2px;
}

.article-content :deep(a:hover) {
  color: var(--accent-hover);
}

.article-content :deep(code) {
  background-color: var(--code-bg);
  padding: 2px 6px;
  border-radius: var(--radius-sm);
  font-size: 0.9em;
  font-family: var(--font-mono);
  color: var(--code-text);
}

.article-content :deep(pre) {
  background-color: var(--code-bg);
  padding: var(--space-lg);
  border-radius: var(--radius-md);
  overflow-x: auto;
  margin-bottom: var(--space-md);
  border: 1px solid var(--border-color);
}

.article-content :deep(pre code) {
  background: none;
  padding: 0;
  color: inherit;
}

.article-content :deep(blockquote) {
  border-left: 4px solid var(--accent-color);
  padding: var(--space-md) var(--space-lg);
  margin: var(--space-md) 0;
  color: var(--text-color);
  background-color: var(--bg-secondary);
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
  font-style: italic;
}

.article-content :deep(img) {
  max-width: 100%;
  border-radius: var(--radius-md);
  margin: var(--space-md) 0;
  box-shadow: var(--card-shadow);
}

.article-content :deep(ul), .article-content :deep(ol) {
  padding-left: var(--space-xl);
  margin-bottom: var(--space-md);
}

.article-content :deep(li) {
  margin-bottom: var(--space-sm);
}

.article-content :deep(li input[type="checkbox"]) {
  margin-right: 8px;
}

.article-content :deep(hr) {
  border: none;
  height: 1px;
  background-color: var(--border-color);
  margin: var(--space-xl) 0;
}

.article-content :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: var(--space-md);
}

.article-content :deep(th), .article-content :deep(td) {
  border: 1px solid var(--border-color);
  padding: var(--space-sm) var(--space-md);
  text-align: left;
}

.article-content :deep(th) {
  background-color: var(--bg-secondary);
  font-weight: 600;
}

.article-footer {
  margin-top: var(--space-2xl);
}

.article-actions {
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-xl);
}

.like-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-xl);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-full);
  cursor: pointer;
  font-size: 0.9375rem;
  color: var(--text-color);
  transition: all var(--transition-fast);
}

.like-btn:hover {
  background-color: #ff4757;
  color: white;
  border-color: #ff4757;
  transform: scale(1.05);
}

.like-icon {
  font-size: 16px;
}

.like-count {
  font-weight: 600;
  min-width: 20px;
}

.article-share {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-lg);
  background-color: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.share-label {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.share-buttons {
  display: flex;
  gap: var(--space-sm);
}

.share-btn {
  padding: var(--space-xs) var(--space-md);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.8125rem;
  color: var(--text-color);
  transition: all var(--transition-fast);
}

.share-btn:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.article-comments {
  margin-top: var(--space-2xl);
}

.comments-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-lg);
  color: var(--text-color);
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-3xl) 0;
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--border-color);
  border-top-color: var(--accent-color);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-text {
  color: var(--text-secondary);
  font-size: 14px;
  margin: 0;
}

.not-found {
  text-align: center;
  padding: var(--space-3xl) var(--space-xl);
}

.not-found-icon {
  font-size: 48px;
  margin-bottom: var(--space-lg);
  opacity: 0.8;
}

.not-found-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-sm);
  color: var(--text-color);
}

.not-found-desc {
  color: var(--text-secondary);
  margin-bottom: var(--space-lg);
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: var(--space-xs);
  padding: var(--space-sm) var(--space-lg);
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  color: var(--text-color);
  text-decoration: none;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.back-link:hover {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
  text-decoration: none;
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .article-title {
    font-size: 2.25rem;
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .article-title {
    font-size: 1.75rem;
  }
  
  .article-meta {
    flex-direction: column;
    gap: var(--space-sm);
  }
  
  .article-content {
    font-size: 1rem;
  }
  
  .article-content :deep(h1) {
    font-size: 1.5rem;
  }
  
  .article-content :deep(h2) {
    font-size: 1.25rem;
  }
  
  .article-share {
    flex-direction: row;
    justify-content: center;
    gap: var(--space-md);
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .article-title {
    font-size: 1.5rem;
  }
  
  .article-content {
    font-size: 0.9375rem;
  }
  
  .like-btn {
    padding: var(--space-sm) var(--space-lg);
  }
  
  .share-buttons {
    flex-direction: column;
    width: 100%;
  }
  
  .share-btn {
    width: 100%;
    text-align: center;
  }
}

/* 打印样式 */
@media print {
  .article-actions,
  .article-share,
  .article-comments {
    display: none;
  }
  
  .article-divider {
    display: none;
  }
}
</style>

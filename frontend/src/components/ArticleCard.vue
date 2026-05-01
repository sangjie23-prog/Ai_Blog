<template>
  <article class="article-card">
    <router-link :to="`/article/${article.id}`" class="card-link">
      <h2 class="card-title">{{ article.title }}</h2>
      <p class="card-summary">{{ article.summary || extractSummary(article.content) }}</p>
      <div class="card-meta">
        <span class="card-date">{{ formatDate(article.createdAt) }}</span>
        <span class="card-views">{{ article.viewCount || 0 }} 阅读</span>
      </div>
    </router-link>
  </article>
</template>

<script setup>
defineProps({
  article: {
    type: Object,
    required: true
  }
})

// 从内容中提取摘要（如果没有摘要）
function extractSummary(content) {
  if (!content) return ''
  // 移除Markdown标记，取前150个字符
  const plainText = content.replace(/[#*`_\[\]()]/g, '').trim()
  return plainText.substring(0, 150) + (plainText.length > 150 ? '...' : '')
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}
</script>

<style scoped>
.article-card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  transition: box-shadow 0.2s;
}

.article-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.card-link {
  text-decoration: none;
  color: inherit;
}

.card-link:hover {
  text-decoration: none;
}

.card-title {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 10px;
  color: var(--text-color);
}

.card-title:hover {
  color: var(--accent-color);
}

.card-summary {
  font-size: 15px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 12px;
}

.card-meta {
  display: flex;
  gap: 16px;
  font-size: 13px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .article-card {
    padding: 16px;
  }
  
  .card-title {
    font-size: 18px;
  }
}
</style>

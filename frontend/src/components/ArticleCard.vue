<template>
  <article 
    class="article-card animate-slide-up" 
    :class="{ 'top-article': article.isTop === 1 }"
    :style="{ animationDelay: `${index * 0.05}s`, animationFillMode: 'both' }"
  >
    <router-link :to="`/article/${article.id}`" class="card-link">
      <div class="card-content">
        <div class="title-row">
          <span v-if="article.isTop === 1" class="top-badge">置顶</span>
          <h2 class="card-title">{{ article.title }}</h2>
        </div>
        <p class="card-summary">{{ article.summary || extractSummary(article.content) }}</p>
        <div class="card-footer">
          <div class="card-meta">
            <time class="card-date" :datetime="article.createdAt">
              <span class="meta-icon">📅</span>
              {{ formatDate(article.createdAt) }}
            </time>
            <span class="card-views">
              <span class="meta-icon">👁️</span>
              {{ article.viewCount || 0 }}
            </span>
          </div>
          <div class="card-tags" v-if="article.tags">
            <span 
              v-for="(tag, i) in article.tags.split(',').slice(0, 3)" 
              :key="i" 
              class="card-tag"
            >
              {{ tag.trim() }}
            </span>
          </div>
        </div>
      </div>
      <div class="card-arrow">
        <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
          <path d="M4 10H16M16 10L11 5M16 10L11 15" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
    </router-link>
  </article>
</template>

<script setup>
defineProps({
  article: {
    type: Object,
    required: true
  },
  index: {
    type: Number,
    default: 0
  }
})

function extractSummary(content) {
  if (!content) return ''
  const plainText = content.replace(/[#*`_\[\]()]/g, '').trim()
  return plainText.substring(0, 150) + (plainText.length > 150 ? '...' : '')
}

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
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  transition: all var(--transition-base);
  position: relative;
}

.article-card.top-article {
  border-color: var(--accent-color);
  background-color: var(--card-bg);
  box-shadow: 0 0 0 1px var(--accent-color);
}

.article-card:hover {
  box-shadow: var(--card-shadow-hover);
  border-color: var(--accent-color);
  transform: translateY(-2px);
}

.card-link {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: var(--space-lg);
  text-decoration: none;
  color: inherit;
}

.card-link:hover {
  text-decoration: none;
}

.title-row {
  display: flex;
  align-items: center;
  gap: var(--space-sm);
  margin-bottom: var(--space-sm);
}

.top-badge {
  display: inline-flex;
  align-items: center;
  padding: 2px var(--space-sm);
  font-size: 0.6875rem;
  font-weight: 600;
  color: #ff6b7a;
  background-color: rgba(255, 71, 87, 0.15);
  border-radius: var(--radius-sm);
  border: 1px solid rgba(255, 71, 87, 0.3);
  white-space: nowrap;
}

.card-content {
  flex: 1;
  min-width: 0;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: var(--space-sm);
  color: var(--text-color);
  line-height: 1.4;
  transition: color var(--transition-fast);
}

.card-link:hover .card-title {
  color: var(--accent-color);
}

.card-summary {
  font-size: 0.9375rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: var(--space-md);
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: var(--space-sm);
}

.card-meta {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  font-size: 0.8125rem;
  color: var(--text-secondary);
}

.card-date {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
}

.card-views {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
}

.meta-icon {
  font-size: 14px;
  opacity: 0.7;
}

.card-tags {
  display: flex;
  align-items: center;
  gap: var(--space-xs);
  flex-wrap: wrap;
}

.card-tag {
  display: inline-block;
  padding: 2px var(--space-sm);
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--tag-text);
  background-color: var(--tag-bg);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.card-tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

.card-arrow {
  flex-shrink: 0;
  color: var(--text-secondary);
  opacity: 0;
  transform: translateX(-8px);
  transition: all var(--transition-base);
}

.article-card:hover .card-arrow {
  opacity: 1;
  transform: translateX(0);
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .article-card {
    padding: var(--space-md);
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .article-card {
    padding: var(--space-md);
  }
  
  .card-title {
    font-size: 1.125rem;
  }
  
  .card-summary {
    font-size: 0.875rem;
    -webkit-line-clamp: 2;
  }
  
  .card-link {
    flex-direction: column;
    gap: var(--space-sm);
  }
  
  .card-arrow {
    display: none;
  }
  
  .card-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-xs);
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .article-card {
    padding: var(--space-sm);
  }
  
  .card-title {
    font-size: 1rem;
  }
  
  .card-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-xs);
  }
}

/* 打印样式 */
@media print {
  .article-card {
    break-inside: avoid;
    box-shadow: none;
    border: 1px solid #ccc;
  }
  
  .card-arrow {
    display: none;
  }
}
</style>

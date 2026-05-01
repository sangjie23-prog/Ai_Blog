<template>
  <div class="home">
    <header class="home-header">
      <h1 class="page-title">最新文章</h1>
      <p class="page-subtitle">探索技术与生活的点滴</p>
    </header>
    
    <div class="content-wrapper">
      <div class="article-list">
        <ArticleCard 
          v-for="(article, index) in articles" 
          :key="article.id" 
          :article="article"
          :index="index"
        />
      </div>

      <div v-if="articles.length === 0 && !loading" class="empty-state animate-fade-in">
        <div class="empty-icon">📝</div>
        <h3 class="empty-title">暂无文章</h3>
        <p class="empty-desc">这里还没有任何内容，快去添加一些吧！</p>
      </div>

      <div v-if="loading" class="loading">
        <div class="loading-spinner"></div>
        <p class="loading-text">加载中...</p>
      </div>

      <div v-if="totalPages > 1" class="pagination animate-fade-in">
        <button 
          :disabled="currentPage <= 1" 
          @click="changePage(currentPage - 1)"
          class="page-btn"
          aria-label="上一页"
        >
          <span class="page-btn-icon">←</span>
          <span class="page-btn-text">上一页</span>
        </button>
        <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
        <button 
          :disabled="currentPage >= totalPages" 
          @click="changePage(currentPage + 1)"
          class="page-btn"
          aria-label="下一页"
        >
          <span class="page-btn-text">下一页</span>
          <span class="page-btn-icon">→</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArticleList } from '../api/article'
import ArticleCard from '../components/ArticleCard.vue'

const articles = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = 10
const totalPages = ref(1)

async function loadArticles() {
  loading.value = true
  try {
    const res = await getArticleList(currentPage.value, pageSize, 1)
    articles.value = res.data.records
    totalPages.value = res.data.pages || 1
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

function changePage(page) {
  currentPage.value = page
  loadArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.home {
  animation: fadeIn 0.4s ease-out;
}

.home-header {
  text-align: center;
  margin-bottom: var(--space-3xl);
  padding: var(--space-xl) 0;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: var(--space-sm);
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.page-subtitle {
  font-size: 1.125rem;
  color: var(--text-secondary);
  margin: 0;
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.empty-state {
  text-align: center;
  padding: var(--space-3xl) var(--space-xl);
}

.empty-icon {
  font-size: 48px;
  margin-bottom: var(--space-lg);
  opacity: 0.8;
}

.empty-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-sm);
  color: var(--text-color);
}

.empty-desc {
  color: var(--text-secondary);
  font-size: 1rem;
  margin: 0;
}

.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-md);
  padding: var(--space-2xl) 0;
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

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-lg);
  margin-top: var(--space-2xl);
  padding: var(--space-lg) 0;
}

.page-btn {
  display: inline-flex;
  align-items: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-lg);
  border: 1px solid var(--border-color);
  background-color: var(--card-bg);
  color: var(--text-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.page-btn:hover:not(:disabled) {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
  transform: translateY(-1px);
}

.page-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.page-btn:disabled:hover {
  transform: none;
  background-color: var(--card-bg);
  color: var(--text-color);
  border-color: var(--border-color);
}

.page-btn-icon {
  font-size: 14px;
}

.page-btn-text {
  display: inline;
}

.page-info {
  font-size: 14px;
  color: var(--text-secondary);
  padding: 0 var(--space-sm);
}

/* 响应式 - 平板端 */
@media (max-width: 1199px) and (min-width: 768px) {
  .page-title {
    font-size: 2.25rem;
  }
  
  .home-header {
    margin-bottom: var(--space-2xl);
  }
}

/* 响应式 - 移动端 */
@media (max-width: 767px) {
  .home-header {
    margin-bottom: var(--space-xl);
    padding: var(--space-md) 0;
  }
  
  .page-title {
    font-size: 1.75rem;
  }
  
  .page-subtitle {
    font-size: 1rem;
  }
  
  .pagination {
    gap: var(--space-md);
    margin-top: var(--space-xl);
  }
  
  .page-btn {
    padding: var(--space-sm) var(--space-md);
  }
  
  .page-btn-text {
    display: none;
  }
  
  .page-btn-icon {
    font-size: 18px;
  }
}

/* 响应式 - 小移动端 */
@media (max-width: 479px) {
  .page-title {
    font-size: 1.5rem;
  }
  
  .page-subtitle {
    font-size: 0.875rem;
  }
  
  .empty-state {
    padding: var(--space-2xl) var(--space-md);
  }
  
  .empty-icon {
    font-size: 36px;
  }
  
  .empty-title {
    font-size: 1.25rem;
  }
}

/* 打印样式 */
@media print {
  .pagination {
    display: none;
  }
}
</style>

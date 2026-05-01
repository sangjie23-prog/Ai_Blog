<template>
  <div class="home">
    <h1 class="page-title">最新文章</h1>
    
    <!-- 文章列表 -->
    <div class="article-list">
      <ArticleCard 
        v-for="article in articles" 
        :key="article.id" 
        :article="article" 
      />
    </div>

    <!-- 空状态 -->
    <div v-if="articles.length === 0 && !loading" class="empty-state">
      <p>暂无文章</p>
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading">加载中...</div>

    <!-- 分页 -->
    <div v-if="totalPages > 1" class="pagination">
      <button 
        :disabled="currentPage <= 1" 
        @click="changePage(currentPage - 1)"
        class="page-btn"
      >
        上一页
      </button>
      <span class="page-info">第 {{ currentPage }} / {{ totalPages }} 页</span>
      <button 
        :disabled="currentPage >= totalPages" 
        @click="changePage(currentPage + 1)"
        class="page-btn"
      >
        下一页
      </button>
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

// 加载文章列表
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

// 切换页码
function changePage(page) {
  currentPage.value = page
  loadArticles()
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 30px;
  text-align: center;
}

.article-list {
  max-width: 800px;
  margin: 0 auto;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
  font-size: 16px;
}

.loading {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  margin-top: 40px;
}

.page-btn {
  padding: 8px 20px;
  border: 1px solid var(--border-color);
  background-color: var(--card-bg);
  color: var(--text-color);
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.page-btn:hover:not(:disabled) {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .page-title {
    font-size: 24px;
    margin-bottom: 20px;
  }
}
</style>

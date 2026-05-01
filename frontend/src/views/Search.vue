<template>
  <div class="search-page">
    <h1 class="page-title">搜索文章</h1>

    <!-- 搜索框 -->
    <div class="search-box">
      <div class="search-tabs">
        <button :class="['tab-btn', { active: searchType === 'keyword' }]" @click="switchSearchType('keyword')">标题搜索</button>
        <button :class="['tab-btn', { active: searchType === 'tag' }]" @click="switchSearchType('tag')">标签搜索</button>
      </div>
      <form @submit.prevent="handleSearch" class="search-form">
        <input 
          v-model="searchQuery" 
          :placeholder="searchType === 'keyword' ? '输入文章标题关键词...' : '输入标签名称...'" 
          class="search-input"
          required
        />
        <button type="submit" class="search-btn" :disabled="loading">
          {{ loading ? '搜索中...' : '搜索' }}
        </button>
      </form>
    </div>

    <!-- 搜索结果 -->
    <div v-if="hasSearched" class="search-results">
      <div class="results-header">
        <p class="results-count">找到 {{ total }} 条结果</p>
        <button v-if="searchedQuery" class="clear-btn" @click="clearSearch">清除搜索</button>
      </div>

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
        <p>没有找到相关文章</p>
      </div>

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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchArticlesByKeyword, searchArticlesByTag } from '../api/article'
import ArticleCard from '../components/ArticleCard.vue'

const route = useRoute()
const router = useRouter()

const searchType = ref('keyword')
const searchQuery = ref('')
const searchedQuery = ref('')
const articles = ref([])
const loading = ref(false)
const hasSearched = ref(false)
const currentPage = ref(1)
const pageSize = 10
const totalPages = ref(1)
const total = ref(0)

// 切换搜索类型
function switchSearchType(type) {
  searchType.value = type
  searchQuery.value = ''
}

// 执行搜索
async function handleSearch() {
  if (!searchQuery.value.trim()) return
  
  loading.value = true
  hasSearched.value = true
  searchedQuery.value = searchQuery.value
  currentPage.value = 1
  
  try {
    let res
    if (searchType.value === 'keyword') {
      res = await searchArticlesByKeyword(searchQuery.value, currentPage.value, pageSize)
    } else {
      res = await searchArticlesByTag(searchQuery.value, currentPage.value, pageSize)
    }
    articles.value = res.data.records || []
    totalPages.value = res.data.pages || 1
    total.value = res.data.total || 0
  } catch (error) {
    console.error('搜索失败:', error)
    alert('搜索失败，请重试')
  } finally {
    loading.value = false
  }
}

// 切换页码
async function changePage(page) {
  currentPage.value = page
  loading.value = true
  
  try {
    let res
    if (searchType.value === 'keyword') {
      res = await searchArticlesByKeyword(searchedQuery.value, currentPage.value, pageSize)
    } else {
      res = await searchArticlesByTag(searchedQuery.value, currentPage.value, pageSize)
    }
    articles.value = res.data.records || []
    totalPages.value = res.data.pages || 1
    total.value = res.data.total || 0
  } catch (error) {
    console.error('加载失败:', error)
  } finally {
    loading.value = false
  }
  
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 清除搜索
function clearSearch() {
  searchQuery.value = ''
  searchedQuery.value = ''
  hasSearched.value = false
  articles.value = []
  total.value = 0
}

// 从URL参数初始化搜索
onMounted(() => {
  const keyword = route.query.keyword
  const tag = route.query.tag
  
  if (keyword) {
    searchType.value = 'keyword'
    searchQuery.value = keyword
    handleSearch()
  } else if (tag) {
    searchType.value = 'tag'
    searchQuery.value = tag
    handleSearch()
  }
})
</script>

<style scoped>
.search-page {
  max-width: var(--container-lg);
  margin: 0 auto;
}

.page-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin-bottom: var(--space-2xl);
  text-align: center;
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.search-box {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-xl);
  margin-bottom: var(--space-2xl);
  box-shadow: var(--card-shadow);
}

.search-tabs {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);
  border-bottom: 1px solid var(--border-color);
  padding-bottom: var(--space-sm);
}

.tab-btn {
  padding: var(--space-sm) var(--space-lg);
  border: none;
  background: none;
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all var(--transition-fast);
  position: relative;
  bottom: -1px;
}

.tab-btn:hover {
  color: var(--accent-color);
}

.tab-btn.active {
  color: var(--accent-color);
  border-bottom-color: var(--accent-color);
}

.search-form {
  display: flex;
  gap: var(--space-md);
}

.search-input {
  flex: 1;
  padding: var(--space-md) var(--space-lg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 1rem;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
  transition: all var(--transition-fast);
}

.search-input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.search-btn {
  padding: var(--space-md) var(--space-2xl);
  border: none;
  border-radius: var(--radius-md);
  background-color: var(--accent-color);
  color: white;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.search-btn:hover:not(:disabled) {
  background-color: var(--accent-hover);
  transform: translateY(-1px);
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.search-results {
  margin-top: var(--space-xl);
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-xl);
}

.results-count {
  font-size: 1rem;
  color: var(--text-secondary);
}

.clear-btn {
  padding: var(--space-xs) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  background: none;
  color: var(--text-secondary);
  font-size: 0.9375rem;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.clear-btn:hover {
  background-color: var(--hover-bg);
  color: var(--text-color);
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.empty-state {
  text-align: center;
  padding: var(--space-3xl) var(--space-xl);
  color: var(--text-secondary);
  font-size: 1.125rem;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-xl);
  margin-top: var(--space-2xl);
}

.page-btn {
  padding: var(--space-sm) var(--space-xl);
  border: 1px solid var(--border-color);
  background-color: var(--card-bg);
  color: var(--text-color);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-size: 0.9375rem;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.page-btn:hover:not(:disabled) {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
  transform: translateY(-1px);
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  font-size: 0.9375rem;
  color: var(--text-secondary);
}

@media (max-width: 767px) {
  .page-title {
    font-size: 2rem;
    margin-bottom: var(--space-xl);
  }

  .search-box {
    padding: var(--space-lg);
  }

  .search-form {
    flex-direction: column;
  }

  .search-btn {
    width: 100%;
  }

  .results-header {
    flex-direction: column;
    gap: var(--space-md);
    align-items: flex-start;
  }

  .pagination {
    gap: var(--space-md);
  }
}

@media (max-width: 479px) {
  .page-title {
    font-size: 1.75rem;
  }

  .search-tabs {
    gap: var(--space-sm);
  }

  .tab-btn {
    padding: var(--space-xs) var(--space-md);
    font-size: 0.9375rem;
  }
}
</style>

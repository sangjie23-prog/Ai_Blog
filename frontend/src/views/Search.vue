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
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 30px;
  text-align: center;
}

.search-box {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
}

.search-tabs {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.tab-btn {
  padding: 8px 16px;
  border: none;
  background: none;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
  border-bottom: 2px solid transparent;
}

.tab-btn.active {
  color: var(--accent-color);
  border-bottom-color: var(--accent-color);
}

.search-form {
  display: flex;
  gap: 12px;
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 15px;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
}

.search-input:focus {
  border-color: var(--accent-color);
}

.search-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  background-color: var(--accent-color);
  color: white;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
}

.search-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.search-results {
  margin-top: 20px;
}

.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.results-count {
  font-size: 14px;
  color: var(--text-secondary);
}

.clear-btn {
  padding: 6px 12px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background: none;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
}

.clear-btn:hover {
  background-color: var(--hover-bg);
}

.article-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: var(--text-secondary);
  font-size: 16px;
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

  .search-form {
    flex-direction: column;
  }

  .search-btn {
    width: 100%;
  }
}
</style>

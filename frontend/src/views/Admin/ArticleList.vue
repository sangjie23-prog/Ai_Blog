<template>
  <div class="article-list-page">
    <div class="page-header">
      <h1 class="page-title">文章管理</h1>
      <router-link to="/admin/articles/new" class="new-btn">+ 新建文章</router-link>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar">
      <input v-model="keyword" type="text" placeholder="搜索文章标题..." class="search-input" @keyup.enter="handleSearch" />
      <select v-model="statusFilter" class="status-select" @change="handleSearch">
        <option value="">全部状态</option>
        <option value="1">已发布</option>
        <option value="0">草稿</option>
      </select>
    </div>

    <!-- 文章列表 -->
    <div class="article-table">
      <table>
        <thead>
          <tr>
            <th>标题</th>
            <th>状态</th>
            <th>阅读</th>
            <th>点赞</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="article in articles" :key="article.id">
            <td class="title-cell">{{ article.title }}</td>
            <td>
              <span :class="['status-badge', article.status === 1 ? 'published' : 'draft']">
                {{ article.status === 1 ? '已发布' : '草稿' }}
              </span>
            </td>
            <td>{{ article.viewCount }}</td>
            <td>{{ article.likeCount }}</td>
            <td>{{ formatDate(article.createdAt) }}</td>
            <td class="action-cell">
              <button class="action-btn edit-btn" @click="editArticle(article.id)">编辑</button>
              <button v-if="article.status === 0" class="action-btn publish-btn" @click="handlePublish(article.id)">发布</button>
              <button v-else class="action-btn unpublish-btn" @click="handleUnpublish(article.id)">撤回</button>
              <button class="action-btn delete-btn" @click="handleDelete(article.id)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>

      <p v-if="articles.length === 0 && !loading" class="empty-msg">暂无文章</p>
    </div>

    <!-- 分页 -->
    <div v-if="total > pageSize" class="pagination">
      <button :disabled="pageNum === 1" @click="changePage(pageNum - 1)">上一页</button>
      <span>第 {{ pageNum }} / {{ Math.ceil(total / pageSize) }} 页</span>
      <button :disabled="pageNum >= Math.ceil(total / pageSize)" @click="changePage(pageNum + 1)">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getAdminArticles, publishArticle, unpublishArticle, deleteAdminArticle } from '../../api/admin/article'

const router = useRouter()

const articles = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')
const statusFilter = ref('')

// 加载文章列表
async function loadArticles() {
  loading.value = true
  try {
    const params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }
    if (statusFilter.value !== '') {
      params.status = parseInt(statusFilter.value)
    }
    if (keyword.value) {
      params.keyword = keyword.value
    }

    const res = await getAdminArticles(params)
    articles.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载文章失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pageNum.value = 1
  loadArticles()
}

// 翻页
function changePage(page) {
  pageNum.value = page
  loadArticles()
}

// 编辑文章
function editArticle(id) {
  router.push(`/admin/articles/edit/${id}`)
}

// 发布文章
async function handlePublish(id) {
  if (!confirm('确定发布这篇文章吗？')) return
  try {
    await publishArticle(id)
    loadArticles()
  } catch (error) {
    alert('发布失败')
  }
}

// 撤回文章
async function handleUnpublish(id) {
  if (!confirm('确定撤回这篇文章吗？')) return
  try {
    await unpublishArticle(id)
    loadArticles()
  } catch (error) {
    alert('撤回失败')
  }
}

// 删除文章
async function handleDelete(id) {
  if (!confirm('确定删除这篇文章吗？此操作不可恢复。')) return
  try {
    await deleteAdminArticle(id)
    loadArticles()
  } catch (error) {
    alert('删除失败')
  }
}

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadArticles()
})
</script>

<style scoped>
.article-list-page {
  max-width: var(--container-xl);
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-xl);
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-color);
  letter-spacing: -0.5px;
}

.new-btn {
  padding: var(--space-sm) var(--space-xl);
  background-color: var(--accent-color);
  color: white;
  text-decoration: none;
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.new-btn:hover {
  background-color: var(--accent-hover);
  text-decoration: none;
  transform: translateY(-1px);
}

.search-bar {
  display: flex;
  gap: var(--space-md);
  margin-bottom: var(--space-lg);
}

.search-input {
  flex: 1;
  padding: var(--space-sm) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  background-color: var(--card-bg);
  color: var(--text-color);
  transition: all var(--transition-fast);
}

.search-input:focus {
  outline: none;
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.status-select {
  padding: var(--space-sm) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  background-color: var(--card-bg);
  color: var(--text-color);
  cursor: pointer;
}

.article-table {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--card-shadow);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: var(--space-md);
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

th {
  font-size: 0.8125rem;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  background-color: var(--bg-secondary);
}

td {
  font-size: 0.9375rem;
}

.title-cell {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.status-badge {
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-full);
  font-size: 0.75rem;
  font-weight: 500;
  display: inline-block;
}

.status-badge.published {
  background-color: rgba(107, 158, 107, 0.25);
  color: #a8d8a8;
}

.status-badge.draft {
  background-color: rgba(196, 163, 90, 0.25);
  color: #e8c878;
}

.action-cell {
  display: flex;
  gap: var(--space-sm);
}

.action-btn {
  padding: var(--space-xs) var(--space-sm);
  border: none;
  border-radius: var(--radius-sm);
  font-size: 0.8125rem;
  cursor: pointer;
  color: white;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.action-btn:hover {
  transform: translateY(-1px);
  opacity: 0.9;
}

.edit-btn {
  background-color: #2196f3;
}

.publish-btn {
  background-color: #4caf50;
}

.unpublish-btn {
  background-color: #ff9800;
}

.delete-btn {
  background-color: #f44336;
}

.empty-msg {
  text-align: center;
  padding: var(--space-3xl);
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-lg);
  margin-top: var(--space-xl);
}

.pagination button {
  padding: var(--space-sm) var(--space-lg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  background-color: var(--card-bg);
  color: var(--text-color);
  cursor: pointer;
  font-size: 0.9375rem;
  transition: all var(--transition-fast);
}

.pagination button:hover:not(:disabled) {
  background-color: var(--accent-color);
  color: white;
  border-color: var(--accent-color);
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  font-size: 0.9375rem;
  color: var(--text-secondary);
}

@media (max-width: 767px) {
  .page-header {
    flex-direction: column;
    gap: var(--space-md);
    align-items: flex-start;
  }

  .search-bar {
    flex-direction: column;
  }

  .action-cell {
    flex-direction: column;
    gap: var(--space-xs);
  }

  .article-table {
    overflow-x: auto;
  }

  table {
    min-width: 600px;
  }

  th, td {
    padding: var(--space-sm);
    font-size: 0.875rem;
  }
}

@media (max-width: 479px) {
  .page-title {
    font-size: 1.5rem;
  }

  .pagination {
    gap: var(--space-md);
  }

  .pagination button {
    padding: var(--space-xs) var(--space-md);
    font-size: 0.875rem;
  }
}
</style>

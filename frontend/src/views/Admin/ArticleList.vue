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
  max-width: 1100px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 28px;
  font-weight: 700;
}

.new-btn {
  padding: 10px 20px;
  background-color: var(--accent-color);
  color: white;
  text-decoration: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
}

.new-btn:hover {
  opacity: 0.9;
  text-decoration: none;
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  background-color: var(--card-bg);
  color: var(--text-color);
}

.status-select {
  padding: 10px 14px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 14px;
  background-color: var(--card-bg);
  color: var(--text-color);
}

.article-table {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

th {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

td {
  font-size: 14px;
}

.title-cell {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.published {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.status-badge.draft {
  background-color: #fff3e0;
  color: #e65100;
}

.action-cell {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  color: white;
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
  padding: 40px;
  color: var(--text-secondary);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 16px;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background-color: var(--card-bg);
  color: var(--text-color);
  cursor: pointer;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  font-size: 14px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }

  .action-cell {
    flex-direction: column;
    gap: 4px;
  }

  table {
    font-size: 12px;
  }

  th, td {
    padding: 10px 8px;
  }
}
</style>

<template>
  <div class="tag-manage">
    <div class="page-header">
      <h1 class="page-title">标签管理</h1>
      <button class="add-btn" @click="showAddDialog">+ 新建标签</button>
    </div>

    <!-- 标签列表 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>标识</th>
            <th>文章数</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in tags" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td><code>{{ item.slug }}</code></td>
            <td>{{ item.articleCount || 0 }}</td>
            <td>{{ formatDate(item.createdAt) }}</td>
            <td class="actions">
              <button class="btn-edit" @click="showEditDialog(item)">编辑</button>
              <button class="btn-delete" @click="handleDelete(item)">删除</button>
            </td>
          </tr>
          <tr v-if="tags.length === 0">
            <td colspan="6" class="empty">暂无标签</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新建/编辑对话框 -->
    <div v-if="dialogVisible" class="dialog-overlay" @click.self="closeDialog">
      <div class="dialog">
        <h2 class="dialog-title">{{ isEdit ? '编辑标签' : '新建标签' }}</h2>
        <form @submit.prevent="handleSubmit" class="dialog-form">
          <div class="form-group">
            <label>名称</label>
            <input v-model="form.name" type="text" placeholder="请输入标签名称" required />
          </div>
          <div class="form-group">
            <label>标识</label>
            <input v-model="form.slug" type="text" placeholder="URL标识（英文）" required />
          </div>
          <div class="dialog-actions">
            <button type="button" class="btn-cancel" @click="closeDialog">取消</button>
            <button type="submit" class="btn-submit" :disabled="loading">
              {{ loading ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTags, createTag, updateTag, deleteTag } from '../../api/admin/tag'

const tags = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const currentId = ref(null)

const form = reactive({
  name: '',
  slug: ''
})

// 格式化日期
function formatDate(dateStr) {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN')
}

// 加载标签列表
async function loadTags() {
  try {
    const res = await getTags()
    tags.value = res.data || []
  } catch (error) {
    alert('加载失败')
  }
}

// 显示新建对话框
function showAddDialog() {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 显示编辑对话框
function showEditDialog(item) {
  isEdit.value = true
  currentId.value = item.id
  form.name = item.name || ''
  form.slug = item.slug || ''
  dialogVisible.value = true
}

// 关闭对话框
function closeDialog() {
  dialogVisible.value = false
  resetForm()
}

// 重置表单
function resetForm() {
  form.name = ''
  form.slug = ''
  currentId.value = null
}

// 提交表单
async function handleSubmit() {
  loading.value = true
  try {
    if (isEdit.value) {
      await updateTag(currentId.value, form)
      alert('更新成功')
    } else {
      await createTag(form)
      alert('创建成功')
    }
    closeDialog()
    await loadTags()
  } catch (error) {
    alert(error.response?.data?.message || '操作失败')
  } finally {
    loading.value = false
  }
}

// 删除标签
async function handleDelete(item) {
  if (!confirm(`确定要删除标签"${item.name}"吗？`)) return
  try {
    await deleteTag(item.id)
    alert('删除成功')
    await loadTags()
  } catch (error) {
    alert(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tag-manage {
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

.add-btn {
  padding: var(--space-sm) var(--space-xl);
  border: none;
  border-radius: var(--radius-md);
  background-color: var(--accent-color);
  color: white;
  font-size: 0.9375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.add-btn:hover {
  background-color: var(--accent-hover);
  transform: translateY(-1px);
}

.table-container {
  background-color: var(--card-bg);
  border-radius: var(--radius-lg);
  overflow: hidden;
  box-shadow: var(--card-shadow);
  border: 1px solid var(--border-color);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: var(--space-md);
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.data-table th {
  background-color: var(--bg-secondary);
  font-weight: 600;
  font-size: 0.8125rem;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.data-table td {
  font-size: 0.9375rem;
}

.data-table td code {
  background-color: var(--bg-tertiary);
  padding: var(--space-xs) var(--space-sm);
  border-radius: var(--radius-sm);
  font-size: 0.8125rem;
}

.data-table .actions {
  display: flex;
  gap: var(--space-sm);
}

.btn-edit,
.btn-delete {
  padding: var(--space-xs) var(--space-md);
  border: none;
  border-radius: var(--radius-sm);
  font-size: 0.8125rem;
  cursor: pointer;
  transition: all var(--transition-fast);
  font-weight: 500;
}

.btn-edit {
  background-color: rgba(25, 118, 210, 0.1);
  color: #1976d2;
}

.btn-edit:hover {
  background-color: rgba(25, 118, 210, 0.2);
}

.btn-delete {
  background-color: rgba(211, 47, 47, 0.1);
  color: #d32f2f;
}

.btn-delete:hover {
  background-color: rgba(211, 47, 47, 0.2);
}

.empty {
  text-align: center;
  color: var(--text-secondary);
  padding: var(--space-3xl) !important;
  font-size: 1rem;
}

/* 对话框样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--overlay-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn var(--transition-base);
}

.dialog {
  background-color: var(--card-bg);
  border-radius: var(--radius-xl);
  padding: var(--space-2xl);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  animation: scaleIn var(--transition-base);
}

.dialog-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: var(--space-xl);
  color: var(--text-color);
}

.dialog-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-lg);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-sm);
}

.form-group label {
  font-size: 0.9375rem;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-group input {
  padding: var(--space-sm) var(--space-md);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-md);
  font-size: 1rem;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
  transition: all var(--transition-fast);
  font-family: var(--font-sans);
}

.form-group input:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--space-md);
  margin-top: var(--space-md);
}

.btn-cancel,
.btn-submit {
  padding: var(--space-sm) var(--space-xl);
  border: none;
  border-radius: var(--radius-md);
  font-size: 0.9375rem;
  font-weight: 500;
  cursor: pointer;
  transition: all var(--transition-fast);
}

.btn-cancel {
  background-color: var(--bg-tertiary);
  color: var(--text-color);
}

.btn-cancel:hover {
  background-color: var(--hover-bg);
}

.btn-submit {
  background-color: var(--accent-color);
  color: white;
}

.btn-submit:hover:not(:disabled) {
  background-color: var(--accent-hover);
  transform: translateY(-1px);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 767px) {
  .page-header {
    flex-direction: column;
    gap: var(--space-md);
    align-items: flex-start;
  }

  .data-table {
    font-size: 0.875rem;
  }

  .data-table th,
  .data-table td {
    padding: var(--space-sm);
  }

  .dialog {
    padding: var(--space-xl);
    width: 95%;
  }
}

@media (max-width: 479px) {
  .page-title {
    font-size: 1.5rem;
  }

  .data-table .actions {
    flex-direction: column;
    gap: var(--space-xs);
  }
}
</style>

<template>
  <div class="category-manage">
    <div class="page-header">
      <h1 class="page-title">分类管理</h1>
      <button class="add-btn" @click="showAddDialog">+ 新建分类</button>
    </div>

    <!-- 分类列表 -->
    <div class="table-container">
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>标识</th>
            <th>描述</th>
            <th>排序</th>
            <th>文章数</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in categories" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.name }}</td>
            <td><code>{{ item.slug }}</code></td>
            <td>{{ item.description || '-' }}</td>
            <td>{{ item.sortOrder }}</td>
            <td>{{ item.articleCount || 0 }}</td>
            <td class="actions">
              <button class="btn-edit" @click="showEditDialog(item)">编辑</button>
              <button class="btn-delete" @click="handleDelete(item)">删除</button>
            </td>
          </tr>
          <tr v-if="categories.length === 0">
            <td colspan="7" class="empty">暂无分类</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新建/编辑对话框 -->
    <div v-if="dialogVisible" class="dialog-overlay" @click.self="closeDialog">
      <div class="dialog">
        <h2 class="dialog-title">{{ isEdit ? '编辑分类' : '新建分类' }}</h2>
        <form @submit.prevent="handleSubmit" class="dialog-form">
          <div class="form-group">
            <label>名称</label>
            <input v-model="form.name" type="text" placeholder="请输入分类名称" required />
          </div>
          <div class="form-group">
            <label>标识</label>
            <input v-model="form.slug" type="text" placeholder="URL标识（英文）" required />
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-model="form.description" placeholder="分类描述（可选）" rows="3"></textarea>
          </div>
          <div class="form-group">
            <label>排序</label>
            <input v-model.number="form.sortOrder" type="number" placeholder="数字越小越靠前" />
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
import { ref, reactive, onMounted, inject } from 'vue'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../../api/admin/category'

const toast = inject('toast')

const categories = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const loading = ref(false)
const currentId = ref(null)

const form = reactive({
  name: '',
  slug: '',
  description: '',
  sortOrder: 0
})

// 加载分类列表
async function loadCategories() {
  try {
    const res = await getCategories()
    categories.value = res.data || []
  } catch (error) {
    toast.value?.error('加载失败')
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
  form.description = item.description || ''
  form.sortOrder = item.sortOrder || 0
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
  form.description = ''
  form.sortOrder = 0
  currentId.value = null
}

// 提交表单
async function handleSubmit() {
  loading.value = true
  try {
    if (isEdit.value) {
      await updateCategory(currentId.value, form)
      toast.value?.success('更新成功')
    } else {
      await createCategory(form)
      toast.value?.success('创建成功')
    }
    closeDialog()
    await loadCategories()
  } catch (error) {
    toast.value?.error(error.response?.data?.message || '操作失败')
  } finally {
    loading.value = false
  }
}

// 删除分类
async function handleDelete(item) {
  if (!confirm(`确定要删除分类"${item.name}"吗？`)) return
  try {
    await deleteCategory(item.id)
    toast.value?.success('删除成功')
    await loadCategories()
  } catch (error) {
    toast.value?.error(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-manage {
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
  background-color: rgba(107, 158, 138, 0.25);
  color: #8ac8b0;
}

.btn-edit:hover {
  background-color: rgba(107, 158, 138, 0.35);
}

.btn-delete {
  background-color: rgba(196, 107, 107, 0.25);
  color: #e88a8a;
}

.btn-delete:hover {
  background-color: rgba(196, 107, 107, 0.35);
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

.form-group input,
.form-group textarea {
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

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
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

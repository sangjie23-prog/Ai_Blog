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
import { ref, reactive, onMounted } from 'vue'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../../api/admin/category'

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
      alert('更新成功')
    } else {
      await createCategory(form)
      alert('创建成功')
    }
    closeDialog()
    await loadCategories()
  } catch (error) {
    alert(error.response?.data?.message || '操作失败')
  } finally {
    loading.value = false
  }
}

// 删除分类
async function handleDelete(item) {
  if (!confirm(`确定要删除分类"${item.name}"吗？`)) return
  try {
    await deleteCategory(item.id)
    alert('删除成功')
    await loadCategories()
  } catch (error) {
    alert(error.response?.data?.message || '删除失败')
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-manage {
  max-width: 1000px;
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

.add-btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  background-color: var(--accent-color);
  color: white;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.add-btn:hover {
  opacity: 0.9;
}

.table-container {
  background-color: var(--card-bg);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.data-table th {
  background-color: var(--header-bg);
  font-weight: 600;
  font-size: 14px;
  color: var(--text-secondary);
}

.data-table td {
  font-size: 14px;
}

.data-table td code {
  background-color: var(--hover-bg);
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 13px;
}

.data-table .actions {
  display: flex;
  gap: 8px;
}

.btn-edit,
.btn-delete {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.btn-edit {
  background-color: #e3f2fd;
  color: #1976d2;
}

.btn-edit:hover {
  background-color: #bbdefb;
}

.btn-delete {
  background-color: #ffebee;
  color: #d32f2f;
}

.btn-delete:hover {
  background-color: #ffcdd2;
}

.empty {
  text-align: center;
  color: var(--text-secondary);
  padding: 40px !important;
}

/* 对话框样式 */
.dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.dialog {
  background-color: var(--card-bg);
  border-radius: 12px;
  padding: 24px;
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.dialog-title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 20px;
}

.dialog-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
}

.form-group input,
.form-group textarea {
  padding: 10px 14px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 15px;
  background-color: var(--bg-color);
  color: var(--text-color);
  outline: none;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--accent-color);
}

.dialog-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 8px;
}

.btn-cancel,
.btn-submit {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.btn-cancel {
  background-color: var(--hover-bg);
  color: var(--text-color);
}

.btn-submit {
  background-color: var(--accent-color);
  color: white;
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
</style>

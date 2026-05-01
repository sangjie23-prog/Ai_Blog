<template>
  <div class="article-edit-page">
    <div class="page-header">
      <h1 class="page-title">{{ isEdit ? '编辑文章' : '新建文章' }}</h1>
      <button class="back-btn" @click="goBack">返回列表</button>
    </div>

    <form @submit.prevent="handleSubmit" class="edit-form">
      <!-- 标题 -->
      <div class="form-group">
        <label for="title">标题</label>
        <input id="title" v-model="form.title" type="text" placeholder="请输入文章标题" required />
      </div>

      <!-- 摘要 -->
      <div class="form-group">
        <label for="summary">摘要</label>
        <textarea id="summary" v-model="form.summary" placeholder="请输入文章摘要（可选）" rows="3"></textarea>
      </div>

      <!-- 分类和标签 -->
      <div class="form-row">
        <div class="form-group">
          <label for="category">分类</label>
          <input id="category" v-model="form.category" type="text" placeholder="分类名称" />
        </div>
        <div class="form-group">
          <label for="tags">标签</label>
          <input id="tags" v-model="form.tags" type="text" placeholder="标签，用逗号分隔" />
        </div>
      </div>

      <!-- 内容编辑区 -->
      <div class="editor-container">
        <div class="editor-tabs">
          <button type="button" :class="['tab-btn', { active: activeTab === 'edit' }]" @click="activeTab = 'edit'">编辑</button>
          <button type="button" :class="['tab-btn', { active: activeTab === 'preview' }]" @click="activeTab = 'preview'">预览</button>
        </div>

        <!-- Markdown 编辑器 -->
        <textarea v-show="activeTab === 'edit'" v-model="form.content" class="markdown-editor" placeholder="在此输入 Markdown 内容..." rows="20"></textarea>

        <!-- Markdown 预览 -->
        <div v-show="activeTab === 'preview'" class="markdown-preview" v-html="previewHtml"></div>
      </div>

      <!-- 操作按钮 -->
      <div class="form-actions">
        <button type="submit" class="submit-btn" :disabled="loading">
          {{ loading ? '保存中...' : '保存草稿' }}
        </button>
        <button type="button" class="publish-btn" @click="handlePublish" :disabled="loading">
          {{ loading ? '发布中...' : '发布文章' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import MarkdownIt from 'markdown-it'
import { getAdminArticle, createAdminArticle, updateAdminArticle, publishArticle } from '../../api/admin/article'

const route = useRoute()
const router = useRouter()

const md = new MarkdownIt()
const isEdit = computed(() => !!route.params.id)
const articleId = computed(() => route.params.id)

const form = reactive({
  title: '',
  summary: '',
  content: '',
  category: '',
  tags: '',
  status: 0
})

const activeTab = ref('edit')
const loading = ref(false)

// Markdown 预览
const previewHtml = computed(() => {
  return md.render(form.content || '')
})

// 加载文章（编辑模式）
async function loadArticle() {
  if (!isEdit.value) return
  try {
    const res = await getAdminArticle(articleId.value)
    const article = res.data
    form.title = article.title || ''
    form.summary = article.summary || ''
    form.content = article.content || ''
    form.category = article.category || ''
    form.tags = article.tags || ''
  } catch (error) {
    alert('加载文章失败')
    router.push('/admin/articles')
  }
}

// 保存草稿
async function handleSubmit() {
  loading.value = true
  try {
    const data = {
      title: form.title,
      summary: form.summary,
      content: form.content,
      category: form.category,
      tags: form.tags,
      status: 0
    }

    if (isEdit.value) {
      await updateAdminArticle(articleId.value, data)
    } else {
      await createAdminArticle(data)
    }

    alert('保存成功')
    router.push('/admin/articles')
  } catch (error) {
    alert('保存失败')
  } finally {
    loading.value = false
  }
}

// 发布文章
async function handlePublish() {
  if (!form.title) {
    alert('请填写文章标题')
    return
  }

  loading.value = true
  try {
    const data = {
      title: form.title,
      summary: form.summary,
      content: form.content,
      category: form.category,
      tags: form.tags,
      status: 1
    }

    let id = articleId.value
    if (isEdit.value) {
      await updateAdminArticle(id, data)
    } else {
      const res = await createAdminArticle(data)
      id = res.data?.id
    }

    alert('发布成功')
    router.push('/admin/articles')
  } catch (error) {
    alert('发布失败')
  } finally {
    loading.value = false
  }
}

// 返回
function goBack() {
  router.push('/admin/articles')
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-edit-page {
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

.back-btn {
  padding: 8px 16px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background-color: var(--card-bg);
  color: var(--text-color);
  cursor: pointer;
  font-size: 14px;
}

.back-btn:hover {
  background-color: var(--hover-bg);
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
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
  background-color: var(--card-bg);
  color: var(--text-color);
  outline: none;
  transition: border-color 0.2s;
}

.form-group input:focus,
.form-group textarea:focus {
  border-color: var(--accent-color);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.editor-container {
  border: 1px solid var(--border-color);
  border-radius: 8px;
  overflow: hidden;
}

.editor-tabs {
  display: flex;
  border-bottom: 1px solid var(--border-color);
  background-color: var(--header-bg);
}

.tab-btn {
  padding: 10px 20px;
  border: none;
  background: none;
  font-size: 14px;
  font-weight: 500;
  color: var(--text-secondary);
  cursor: pointer;
}

.tab-btn.active {
  color: var(--accent-color);
  border-bottom: 2px solid var(--accent-color);
}

.markdown-editor {
  width: 100%;
  padding: 16px;
  border: none;
  font-size: 15px;
  font-family: 'Consolas', 'Monaco', monospace;
  line-height: 1.6;
  background-color: var(--card-bg);
  color: var(--text-color);
  resize: vertical;
  outline: none;
}

.markdown-preview {
  padding: 20px;
  background-color: var(--card-bg);
  min-height: 300px;
  line-height: 1.6;
}

.markdown-preview :deep(h1),
.markdown-preview :deep(h2),
.markdown-preview :deep(h3) {
  margin-top: 24px;
  margin-bottom: 12px;
}

.markdown-preview :deep(p) {
  margin-bottom: 12px;
}

.markdown-preview :deep(code) {
  background-color: var(--hover-bg);
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 14px;
}

.markdown-preview :deep(pre) {
  background-color: var(--hover-bg);
  padding: 16px;
  border-radius: 6px;
  overflow-x: auto;
}

.markdown-preview :deep(pre code) {
  background: none;
  padding: 0;
}

.markdown-preview :deep(blockquote) {
  border-left: 4px solid var(--accent-color);
  padding-left: 16px;
  margin: 16px 0;
  color: var(--text-secondary);
}

.markdown-preview :deep(img) {
  max-width: 100%;
}

.form-actions {
  display: flex;
  gap: 12px;
}

.submit-btn,
.publish-btn {
  padding: 12px 24px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn {
  background-color: var(--card-bg);
  color: var(--text-color);
  border: 1px solid var(--border-color);
}

.submit-btn:hover:not(:disabled) {
  background-color: var(--hover-bg);
}

.publish-btn {
  background-color: var(--accent-color);
  color: white;
}

.publish-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.submit-btn:disabled,
.publish-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }
}
</style>

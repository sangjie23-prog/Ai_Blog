<template>
  <div class="markdown-editor-wrapper">
    <!-- 工具栏 -->
    <div class="editor-toolbar">
      <div class="toolbar-group">
        <button type="button" class="toolbar-btn" @click="insertMarkdown('# ', '标题', 2)" title="标题 H1">
          <span>H1</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertMarkdown('## ', '标题', 2)" title="标题 H2">
          <span>H2</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertMarkdown('### ', '标题', 3)" title="标题 H3">
          <span>H3</span>
        </button>
      </div>

      <div class="toolbar-divider"></div>

      <div class="toolbar-group">
        <button type="button" class="toolbar-btn" @click="wrapSelection('**', '**')" title="粗体">
          <strong>B</strong>
        </button>
        <button type="button" class="toolbar-btn" @click="wrapSelection('*', '*')" title="斜体">
          <em>I</em>
        </button>
        <button type="button" class="toolbar-btn" @click="wrapSelection('~~', '~~')" title="删除线">
          <s>S</s>
        </button>
        <button type="button" class="toolbar-btn" @click="wrapSelection('`', '`')" title="行内代码">
          <code>&lt;/&gt;</code>
        </button>
      </div>

      <div class="toolbar-divider"></div>

      <div class="toolbar-group">
        <button type="button" class="toolbar-btn" @click="insertMarkdown('- ', '列表项')" title="无序列表">
          <span>• 列表</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertMarkdown('1. ', '列表项')" title="有序列表">
          <span>1. 列表</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertMarkdown('> ', '引用内容')" title="引用">
          <span>❝ 引用</span>
        </button>
      </div>

      <div class="toolbar-divider"></div>

      <div class="toolbar-group">
        <button type="button" class="toolbar-btn" @click="insertLink" title="链接">
          <span>🔗 链接</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertImage" title="图片">
          <span>🖼️ 图片</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertCodeBlock" title="代码块">
          <span>``` 代码</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertTable" title="表格">
          <span>▦ 表格</span>
        </button>
      </div>

      <div class="toolbar-divider"></div>

      <div class="toolbar-group">
        <button type="button" class="toolbar-btn" @click="insertHorizontalRule" title="分割线">
          <span>— 分割线</span>
        </button>
        <button type="button" class="toolbar-btn" @click="insertTodo" title="任务列表">
          <span>☑ 任务</span>
        </button>
      </div>

      <div class="toolbar-spacer"></div>

      <div class="toolbar-info">
        <span>字数: {{ wordCount }}</span>
      </div>
    </div>

    <!-- 编辑区域 -->
    <div class="editor-body" :class="`view-${viewMode}`">
      <!-- 编辑模式 -->
      <div v-show="viewMode === 'edit'" class="editor-pane">
        <textarea
          ref="editorRef"
          v-model="localContent"
          class="editor-textarea"
          placeholder="在此输入 Markdown 内容..."
          @input="handleInput"
          @keydown="handleKeydown"
        ></textarea>
      </div>

      <!-- 分屏模式 -->
      <template v-if="viewMode === 'split'">
        <div class="editor-pane">
          <textarea
            ref="editorRef"
            v-model="localContent"
            class="editor-textarea"
            placeholder="在此输入 Markdown 内容..."
            @input="handleInput"
            @keydown="handleKeydown"
          ></textarea>
        </div>
        <div class="preview-pane">
          <div class="markdown-preview" v-html="renderedHtml"></div>
        </div>
      </template>

      <!-- 预览模式 -->
      <div v-show="viewMode === 'preview'" class="preview-pane full-width">
        <div class="markdown-preview" v-html="renderedHtml"></div>
      </div>
    </div>

    <!-- 底部状态栏 -->
    <div class="editor-statusbar">
      <div class="statusbar-left">
        <span class="status-item" :class="{ active: autoSaveEnabled }" @click="toggleAutoSave">
          {{ autoSaveEnabled ? '✓ 自动保存已开启' : '○ 自动保存已关闭' }}
        </span>
        <span v-if="lastSaved" class="status-item">
          上次保存: {{ lastSaved }}
        </span>
      </div>
      <div class="statusbar-right">
        <button type="button" class="view-btn" :class="{ active: viewMode === 'edit' }" @click="viewMode = 'edit'">编辑</button>
        <button type="button" class="view-btn" :class="{ active: viewMode === 'split' }" @click="viewMode = 'split'">分屏</button>
        <button type="button" class="view-btn" :class="{ active: viewMode === 'preview' }" @click="viewMode = 'preview'">预览</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted, nextTick } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github-dark.css'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  autoSaveKey: {
    type: String,
    default: 'markdown-editor-autosave'
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

const editorRef = ref(null)
const localContent = ref(props.modelValue)
const viewMode = ref('split')
const autoSaveEnabled = ref(true)
const lastSaved = ref('')
let autoSaveTimer = null

// Markdown 渲染器
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  breaks: true,
  highlight: function (str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return '<pre class="hljs"><code>' +
          hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
          '</code></pre>'
      } catch (__) { }
    }
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>'
  }
})

// 渲染后的 HTML
const renderedHtml = computed(() => {
  return md.render(localContent.value || '')
})

// 字数统计
const wordCount = computed(() => {
  const text = localContent.value || ''
  const chineseCount = (text.match(/[\u4e00-\u9fa5]/g) || []).length
  const englishWords = (text.match(/[a-zA-Z]+/g) || []).length
  return chineseCount + englishWords
})

// 监听外部值变化
watch(() => props.modelValue, (newVal) => {
  if (newVal !== localContent.value) {
    localContent.value = newVal
  }
})

// 输入处理
function handleInput() {
  emit('update:modelValue', localContent.value)
  emit('change', localContent.value)

  if (autoSaveEnabled.value) {
    clearTimeout(autoSaveTimer)
    autoSaveTimer = setTimeout(() => {
      saveToLocalStorage()
    }, 1000)
  }
}

// 键盘快捷键
function handleKeydown(e) {
  if (e.ctrlKey || e.metaKey) {
    switch (e.key.toLowerCase()) {
      case 'b':
        e.preventDefault()
        wrapSelection('**', '**')
        break
      case 'i':
        e.preventDefault()
        wrapSelection('*', '*')
        break
      case 'k':
        e.preventDefault()
        insertLink()
        break
      case 's':
        e.preventDefault()
        saveToLocalStorage()
        break
    }
  }

  // Tab 键插入空格
  if (e.key === 'Tab') {
    e.preventDefault()
    insertText('  ')
  }
}

// 在光标位置插入文本
function insertText(text) {
  const textarea = editorRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const before = localContent.value.substring(0, start)
  const after = localContent.value.substring(end)

  localContent.value = before + text + after
  emit('update:modelValue', localContent.value)

  nextTick(() => {
    textarea.focus()
    textarea.selectionStart = textarea.selectionEnd = start + text.length
  })
}

// 在光标位置插入 Markdown（带占位符）
function insertMarkdown(prefix, placeholder, selectLength = 0) {
  const textarea = editorRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const before = localContent.value.substring(0, start)
  const selected = localContent.value.substring(start, end)
  const after = localContent.value.substring(end)

  const insertText = selected ? prefix + selected : prefix + placeholder
  localContent.value = before + insertText + '\n' + after
  emit('update:modelValue', localContent.value)

  nextTick(() => {
    textarea.focus()
    const newStart = start + prefix.length
    const newEnd = selected ? start + prefix.length + selected.length : newStart + placeholder.length
    textarea.selectionStart = newStart
    textarea.selectionEnd = newEnd
  })
}

// 包裹选中文本
function wrapSelection(before, after) {
  const textarea = editorRef.value
  if (!textarea) return

  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  const selected = localContent.value.substring(start, end)
  const beforeText = localContent.value.substring(0, start)
  const afterText = localContent.value.substring(end)

  const wrapBefore = selected.startsWith(before) ? '' : before
  const wrapAfter = selected.endsWith(after) ? '' : after

  const newText = wrapBefore + selected + wrapAfter
  localContent.value = beforeText + newText + afterText
  emit('update:modelValue', localContent.value)

  nextTick(() => {
    textarea.focus()
    textarea.selectionStart = start + wrapBefore.length
    textarea.selectionEnd = start + wrapBefore.length + selected.length
  })
}

// 插入链接
function insertLink() {
  const url = prompt('请输入链接地址:', 'https://')
  if (!url) return
  const text = prompt('请输入链接文本:', '链接文本') || '链接文本'
  insertText(`[${text}](${url})`)
}

// 插入图片
function insertImage() {
  const url = prompt('请输入图片地址:', 'https://')
  if (!url) return
  const alt = prompt('请输入图片描述:', '图片描述') || '图片'
  insertText(`![${alt}](${url})`)
}

// 插入代码块
function insertCodeBlock() {
  const lang = prompt('请输入代码语言（如 javascript, python, css）:', 'javascript') || 'javascript'
  insertMarkdown('```' + lang + '\n', '在此输入代码', 0)
  insertText('\n```\n')
}

// 插入表格
function insertTable() {
  const table = `| 列1 | 列2 | 列3 |
| --- | --- | --- |
| 内容 | 内容 | 内容 |
| 内容 | 内容 | 内容 |`
  insertText('\n' + table + '\n')
}

// 插入分割线
function insertHorizontalRule() {
  insertText('\n---\n')
}

// 插入任务列表
function insertTodo() {
  insertMarkdown('- [ ] ', '任务内容')
}

// 自动保存到 localStorage
function saveToLocalStorage() {
  if (props.autoSaveKey) {
    localStorage.setItem(props.autoSaveKey, localContent.value)
    const now = new Date()
    lastSaved.value = now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  }
}

// 从 localStorage 加载
function loadFromLocalStorage() {
  if (props.autoSaveKey) {
    const saved = localStorage.getItem(props.autoSaveKey)
    if (saved && !localContent.value) {
      localContent.value = saved
      emit('update:modelValue', saved)
    }
  }
}

// 切换自动保存
function toggleAutoSave() {
  autoSaveEnabled.value = !autoSaveEnabled.value
  if (autoSaveEnabled.value) {
    saveToLocalStorage()
  }
}

onMounted(() => {
  loadFromLocalStorage()
})

onUnmounted(() => {
  clearTimeout(autoSaveTimer)
})
</script>

<style scoped>
.markdown-editor-wrapper {
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  overflow: hidden;
  background-color: var(--card-bg);
}

/* 工具栏 */
.editor-toolbar {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background-color: var(--bg-secondary);
  border-bottom: 1px solid var(--border-color);
  flex-wrap: wrap;
  gap: 4px;
}

.toolbar-group {
  display: flex;
  gap: 2px;
}

.toolbar-divider {
  width: 1px;
  height: 24px;
  background-color: var(--border-color);
  margin: 0 8px;
}

.toolbar-spacer {
  flex: 1;
}

.toolbar-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 6px 10px;
  border: none;
  background: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 13px;
  color: var(--text-secondary);
  transition: all var(--transition-fast);
  white-space: nowrap;
}

.toolbar-btn:hover {
  background-color: var(--hover-bg);
  color: var(--text-color);
}

.toolbar-btn:active {
  background-color: var(--accent-light);
  color: var(--accent-color);
}

.toolbar-btn code {
  font-size: 12px;
  font-family: var(--font-mono);
}

.toolbar-info {
  font-size: 12px;
  color: var(--text-tertiary);
  padding: 0 8px;
}

/* 编辑主体 */
.editor-body {
  display: flex;
  min-height: 400px;
}

.editor-body.view-split {
  display: grid;
  grid-template-columns: 1fr 1fr;
}

.editor-pane {
  display: flex;
  flex-direction: column;
  border-right: 1px solid var(--border-color);
}

.editor-body.view-split .editor-pane {
  border-right: 1px solid var(--border-color);
}

.editor-body.view-preview .editor-pane {
  display: none;
}

.preview-pane {
  overflow-y: auto;
  background-color: var(--card-bg);
}

.preview-pane.full-width {
  width: 100%;
}

/* 编辑文本域 */
.editor-textarea {
  width: 100%;
  height: 100%;
  min-height: 400px;
  padding: 16px;
  border: none;
  font-size: 15px;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  line-height: 1.7;
  background-color: var(--card-bg);
  color: var(--text-color);
  resize: none;
  outline: none;
  tab-size: 2;
}

.editor-textarea::placeholder {
  color: var(--text-tertiary);
}

/* Markdown 预览 */
.markdown-preview {
  padding: 20px;
  font-size: 15px;
  line-height: 1.8;
  color: var(--text-color);
}

.markdown-preview :deep(h1) {
  font-size: 1.75rem;
  margin: 24px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border-color);
  font-weight: 700;
}

.markdown-preview :deep(h2) {
  font-size: 1.5rem;
  margin: 20px 0 12px;
  font-weight: 600;
}

.markdown-preview :deep(h3) {
  font-size: 1.25rem;
  margin: 16px 0 8px;
  font-weight: 600;
}

.markdown-preview :deep(h4) {
  font-size: 1.125rem;
  margin: 12px 0 8px;
  font-weight: 600;
}

.markdown-preview :deep(p) {
  margin-bottom: 12px;
}

.markdown-preview :deep(a) {
  color: var(--accent-color);
  text-decoration: underline;
  text-underline-offset: 2px;
}

.markdown-preview :deep(a:hover) {
  color: var(--accent-hover);
}

.markdown-preview :deep(strong) {
  font-weight: 700;
  color: var(--text-color);
}

.markdown-preview :deep(em) {
  font-style: italic;
}

.markdown-preview :deep(code) {
  background-color: var(--code-bg);
  padding: 2px 6px;
  border-radius: var(--radius-sm);
  font-size: 0.9em;
  font-family: var(--font-mono);
  color: var(--code-text);
}

.markdown-preview :deep(pre) {
  background-color: var(--code-bg);
  padding: 16px;
  border-radius: var(--radius-md);
  overflow-x: auto;
  margin-bottom: 16px;
  border: 1px solid var(--border-color);
}

.markdown-preview :deep(pre code) {
  background: none;
  padding: 0;
  color: inherit;
  font-size: 14px;
}

.markdown-preview :deep(.hljs) {
  background: none !important;
  padding: 0 !important;
}

.markdown-preview :deep(blockquote) {
  border-left: 4px solid var(--accent-color);
  padding: 12px 16px;
  margin: 16px 0;
  color: var(--text-secondary);
  background-color: var(--bg-secondary);
  border-radius: 0 var(--radius-md) var(--radius-md) 0;
}

.markdown-preview :deep(img) {
  max-width: 100%;
  border-radius: var(--radius-md);
  margin: 16px 0;
}

.markdown-preview :deep(ul),
.markdown-preview :deep(ol) {
  padding-left: 24px;
  margin-bottom: 12px;
}

.markdown-preview :deep(li) {
  margin-bottom: 6px;
}

.markdown-preview :deep(li input[type="checkbox"]) {
  margin-right: 8px;
}

.markdown-preview :deep(hr) {
  border: none;
  height: 1px;
  background-color: var(--border-color);
  margin: 24px 0;
}

.markdown-preview :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 16px;
}

.markdown-preview :deep(th),
.markdown-preview :deep(td) {
  border: 1px solid var(--border-color);
  padding: 8px 12px;
  text-align: left;
}

.markdown-preview :deep(th) {
  background-color: var(--bg-secondary);
  font-weight: 600;
}

.markdown-preview :deep(tr:nth-child(even)) {
  background-color: var(--bg-secondary);
}

/* 状态栏 */
.editor-statusbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 12px;
  background-color: var(--bg-secondary);
  border-top: 1px solid var(--border-color);
  font-size: 12px;
}

.statusbar-left {
  display: flex;
  gap: 16px;
}

.status-item {
  color: var(--text-tertiary);
  cursor: default;
}

.status-item.active {
  color: var(--success-color);
  cursor: pointer;
}

.statusbar-right {
  display: flex;
  gap: 4px;
}

.view-btn {
  padding: 4px 12px;
  border: none;
  background: none;
  border-radius: var(--radius-sm);
  cursor: pointer;
  font-size: 12px;
  color: var(--text-tertiary);
  transition: all var(--transition-fast);
}

.view-btn:hover {
  color: var(--text-color);
  background-color: var(--hover-bg);
}

.view-btn.active {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

/* 护眼模式适配 */
.eye-care-theme .editor-textarea {
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  -webkit-font-smoothing: antialiased;
}

.eye-care-theme .markdown-preview :deep(code) {
  color: #f0a0a0 !important;
}

.eye-care-theme .markdown-preview :deep(pre code) {
  color: #f0a0a0 !important;
}

/* 响应式 */
@media (max-width: 768px) {
  .editor-body.view-split {
    grid-template-columns: 1fr;
    grid-template-rows: 1fr 1fr;
  }

  .editor-pane {
    border-right: none;
    border-bottom: 1px solid var(--border-color);
  }

  .toolbar-divider {
    display: none;
  }

  .toolbar-btn {
    padding: 6px 8px;
    font-size: 12px;
  }

  .toolbar-btn span {
    font-size: 12px;
  }
}
</style>

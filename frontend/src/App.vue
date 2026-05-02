<template>
  <div id="app" :class="{ 'dark-mode': isDarkMode }">
    <Header />
    <main class="main-content">
      <router-view />
    </main>
    <Footer />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useThemeStore } from './stores/theme'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

const themeStore = useThemeStore()
const isDarkMode = computed(() => themeStore.isDark)

onMounted(() => {
  document.documentElement.classList.toggle('dark', isDarkMode.value)
})
</script>

<style>
/* 全局样式 - Ghost风格设计原则 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  /* Ghost 风格配色 - 浅色模式 */
  --bg-color: #ffffff;
  --bg-secondary: #f6f6f6;
  --bg-tertiary: #eef0f2;
  --text-color: #15171a;
  --text-secondary: #5a6b78;
  --text-tertiary: #7c8a96;
  --border-color: #e5eff5;
  --border-light: #f0f0f0;
  --accent-color: #3eb0ef;
  --accent-hover: #3498db;
  --accent-light: rgba(62, 176, 239, 0.1);
  --card-bg: #ffffff;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.12);
  --header-bg: rgba(255, 255, 255, 0.98);
  --header-blur: blur(12px);
  --footer-bg: #15171a;
  --footer-text: #ffffff;
  --code-bg: #f5f7f9;
  --code-text: #d63384;
  --hover-bg: #f5f7f9;
  --success-color: #28a745;
  --warning-color: #e67e22;
  --error-color: #dc3545;
  --info-color: #17a2b8;
  --input-bg: #ffffff;
  --input-border: #d1d5db;
  --input-focus-border: #3eb0ef;
  --btn-primary-bg: #3eb0ef;
  --btn-primary-text: #ffffff;
  --btn-secondary-bg: #f5f7f9;
  --btn-secondary-text: #15171a;
  --tag-bg: #eef0f2;
  --tag-text: #5a6b78;
  --overlay-bg: rgba(0, 0, 0, 0.5);
  --scrollbar-thumb: #c1c1c1;
  --scrollbar-track: #f1f1f1;
  
  /* 提示框配色 - 浅色模式 */
  --alert-success-bg: #d4edda;
  --alert-success-text: #155724;
  --alert-success-border: #c3e6cb;
  --alert-warning-bg: #fff3cd;
  --alert-warning-text: #856404;
  --alert-warning-border: #ffeeba;
  --alert-error-bg: #f8d7da;
  --alert-error-text: #721c24;
  --alert-error-border: #f5c6cb;
  --alert-info-bg: #d1ecf1;
  --alert-info-text: #0c5460;
  --alert-info-border: #bee5eb;
  
  /* 排版 */
  --font-sans: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Helvetica Neue', sans-serif;
  --font-serif: Georgia, 'Times New Roman', serif;
  --font-mono: 'SF Mono', 'Fira Code', 'Fira Mono', 'Roboto Mono', monospace;
  
  /* 间距 */
  --space-xs: 4px;
  --space-sm: 8px;
  --space-md: 16px;
  --space-lg: 24px;
  --space-xl: 32px;
  --space-2xl: 48px;
  --space-3xl: 64px;
  
  /* 圆角 */
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  --radius-full: 9999px;
  
  /* 过渡 */
  --transition-fast: 150ms ease;
  --transition-base: 250ms ease;
  --transition-slow: 350ms ease;
  
  /* 容器宽度 */
  --container-sm: 640px;
  --container-md: 768px;
  --container-lg: 1024px;
  --container-xl: 1200px;
  --container-2xl: 1400px;
}

.dark-mode {
  /* Ghost 风格配色 - 深色模式 */
  --bg-color: #0d1117;
  --bg-secondary: #161b22;
  --bg-tertiary: #21262d;
  /* 优化文字对比度，确保符合 WCAG AA 级标准 */
  --text-color: #f0f6fc;
  --text-secondary: #b0bac4;
  --text-tertiary: #8b949e;
  --border-color: #30363d;
  --border-light: #21262d;
  --accent-color: #58a6ff;
  --accent-hover: #79b8ff;
  --accent-light: rgba(88, 166, 255, 0.15);
  --card-bg: #161b22;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.4);
  --header-bg: rgba(13, 17, 23, 0.98);
  --footer-bg: #010409;
  --footer-text: #f0f6fc;
  --code-bg: #1c2128;
  --code-text: #ff7b72;
  --hover-bg: #21262d;
  --success-color: #3fb950;
  --warning-color: #d29922;
  --error-color: #f85149;
  --info-color: #58a6ff;
  --input-bg: #0d1117;
  --input-border: #30363d;
  --input-focus-border: #58a6ff;
  --btn-primary-bg: #58a6ff;
  --btn-primary-text: #0d1117;
  --btn-secondary-bg: #21262d;
  --btn-secondary-text: #f0f6fc;
  --tag-bg: #21262d;
  --tag-text: #b0bac4;
  --overlay-bg: rgba(0, 0, 0, 0.75);
  --scrollbar-thumb: #484f58;
  --scrollbar-track: #161b22;
  
  /* 提示框配色 - 深色模式（优化对比度） */
  --alert-success-bg: #1a3a2a;
  --alert-success-text: #7ee8a0;
  --alert-success-border: #2d5a3f;
  --alert-warning-bg: #3d2e1a;
  --alert-warning-text: #f0c878;
  --alert-warning-border: #5a4528;
  --alert-error-bg: #3d1a1a;
  --alert-error-text: #f88a8a;
  --alert-error-border: #5a2828;
  --alert-info-bg: #1a2d3d;
  --alert-info-text: #78c8f0;
  --alert-info-border: #28455a;
}

html {
  scroll-behavior: smooth;
  -webkit-text-size-adjust: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-rendering: optimizeLegibility;
}

body {
  font-family: var(--font-sans);
  background-color: var(--bg-color);
  color: var(--text-color);
  line-height: 1.7;
  font-size: 16px;
  transition: background-color var(--transition-base), color var(--transition-base);
  /* 确保背景正确显示 */
  background-image: none;
  background-repeat: no-repeat;
  background-attachment: fixed;
  /* 优化文字渲染 */
  text-rendering: optimizeLegibility;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 深色模式下文字增强 */
.dark-mode body {
  /* 添加微妙的渐变背景，提升视觉层次 */
  background-image: radial-gradient(ellipse at top, rgba(88, 166, 255, 0.03) 0%, transparent 50%);
  /* 确保深色模式下文字清晰 */
  text-shadow: 0 0 1px rgba(240, 246, 252, 0.1);
}

/* 深色模式下所有文字清晰度增强 */
.dark-mode h1,
.dark-mode h2,
.dark-mode h3,
.dark-mode h4,
.dark-mode h5,
.dark-mode h6 {
  color: var(--text-color);
  font-weight: 700;
}

.dark-mode p,
.dark-mode span,
.dark-mode div,
.dark-mode li,
.dark-mode td,
.dark-mode th {
  color: var(--text-color);
}

.dark-mode .text-secondary,
.dark-mode [class*="text-secondary"] {
  color: var(--text-secondary) !important;
}

/* 滚动条样式 */
::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

::-webkit-scrollbar-track {
  background: var(--scrollbar-track);
}

::-webkit-scrollbar-thumb {
  background: var(--scrollbar-thumb);
  border-radius: var(--radius-full);
}

::-webkit-scrollbar-thumb:hover {
  background: var(--text-secondary);
}

/* 主内容区域 */
.main-content {
  max-width: var(--container-xl);
  margin: 0 auto;
  padding: var(--space-2xl) var(--space-lg);
  min-height: calc(100vh - 200px);
}

/* 链接样式 */
a {
  color: var(--accent-color);
  text-decoration: none;
  transition: color var(--transition-fast);
}

a:hover {
  color: var(--accent-hover);
  text-decoration: none;
}

/* 按钮基础样式 */
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-sm);
  padding: var(--space-sm) var(--space-lg);
  font-size: 14px;
  font-weight: 500;
  line-height: 1.5;
  border-radius: var(--radius-md);
  border: 1px solid transparent;
  cursor: pointer;
  transition: all var(--transition-fast);
  white-space: nowrap;
  user-select: none;
}

.btn:focus-visible {
  outline: 2px solid var(--accent-color);
  outline-offset: 2px;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-primary {
  background-color: var(--btn-primary-bg);
  color: var(--btn-primary-text);
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--accent-hover);
}

.btn-secondary {
  background-color: var(--btn-secondary-bg);
  color: var(--btn-secondary-text);
  border-color: var(--border-color);
}

.btn-secondary:hover:not(:disabled) {
  background-color: var(--hover-bg);
}

/* 输入框样式 */
.input {
  width: 100%;
  padding: var(--space-sm) var(--space-md);
  font-size: 14px;
  line-height: 1.5;
  color: var(--text-color);
  background-color: var(--input-bg);
  border: 1px solid var(--input-border);
  border-radius: var(--radius-md);
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
}

.input:focus {
  outline: none;
  border-color: var(--input-focus-border);
  box-shadow: 0 0 0 3px var(--accent-light);
}

.input::placeholder {
  color: var(--text-tertiary);
}

/* 卡片样式 */
.card {
  background-color: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--space-lg);
  transition: all var(--transition-base);
}

.card:hover {
  box-shadow: var(--card-shadow-hover);
  border-color: var(--accent-color);
}

/* 提示框样式 - 统一美观设计 */
.alert {
  padding: var(--space-md) var(--space-lg);
  border-radius: var(--radius-md);
  border: 1px solid;
  margin-bottom: var(--space-md);
  font-size: 0.9375rem;
  line-height: 1.6;
  font-weight: 500;
  display: flex;
  align-items: flex-start;
  gap: var(--space-sm);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  animation: slideDown var(--transition-base) ease-out;
}

.alert-icon {
  flex-shrink: 0;
  font-size: 1.125rem;
  line-height: 1.5;
}

.alert-content {
  flex: 1;
}

.alert-success {
  background-color: var(--alert-success-bg);
  color: var(--alert-success-text);
  border-color: var(--alert-success-border);
}

.alert-warning {
  background-color: var(--alert-warning-bg);
  color: var(--alert-warning-text);
  border-color: var(--alert-warning-border);
}

.alert-error {
  background-color: var(--alert-error-bg);
  color: var(--alert-error-text);
  border-color: var(--alert-error-border);
}

.alert-info {
  background-color: var(--alert-info-bg);
  color: var(--alert-info-text);
  border-color: var(--alert-info-border);
}

/* 深色模式下提示框优化 */
.dark-mode .alert {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

/* 标签样式 */
.tag {
  display: inline-block;
  padding: var(--space-xs) var(--space-sm);
  font-size: 12px;
  font-weight: 500;
  color: var(--tag-text);
  background-color: var(--tag-bg);
  border-radius: var(--radius-sm);
  transition: all var(--transition-fast);
}

.tag:hover {
  color: var(--accent-color);
  background-color: var(--accent-light);
}

/* 代码块样式 */
code {
  font-family: var(--font-mono);
  font-size: 0.9em;
  padding: 2px 6px;
  background-color: var(--code-bg);
  color: var(--code-text);
  border-radius: var(--radius-sm);
}

pre {
  background-color: var(--code-bg);
  padding: var(--space-lg);
  border-radius: var(--radius-md);
  overflow-x: auto;
  line-height: 1.6;
}

pre code {
  padding: 0;
  background: none;
  color: inherit;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes scaleIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.animate-fade-in {
  animation: fadeIn var(--transition-base) ease-out;
}

.animate-slide-up {
  animation: slideUp var(--transition-base) ease-out;
}

.animate-slide-down {
  animation: slideDown var(--transition-base) ease-out;
}

.animate-scale-in {
  animation: scaleIn var(--transition-base) ease-out;
}

/* 响应式布局 - 桌面端 (>= 1200px) */
@media (min-width: 1200px) {
  .main-content {
    padding: var(--space-3xl) var(--space-xl);
  }
}

/* 响应式布局 - 平板端 (768px - 1199px) */
@media (max-width: 1199px) and (min-width: 768px) {
  .main-content {
    padding: var(--space-xl) var(--space-lg);
  }
}

/* 响应式布局 - 移动端 (480px - 767px) */
@media (max-width: 767px) and (min-width: 480px) {
  .main-content {
    padding: var(--space-lg) var(--space-md);
  }
  
  h1 { font-size: 1.75rem; }
  h2 { font-size: 1.5rem; }
  h3 { font-size: 1.25rem; }
}

/* 响应式布局 - 小移动端 (< 480px) */
@media (max-width: 479px) {
  .main-content {
    padding: var(--space-md) var(--space-sm);
  }
  
  h1 { font-size: 1.5rem; }
  h2 { font-size: 1.25rem; }
  h3 { font-size: 1.125rem; }
  
  .btn {
    padding: var(--space-sm) var(--space-md);
    font-size: 13px;
  }
}

/* 无障碍 - 减少动画 */
@media (prefers-reduced-motion: reduce) {
  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
    scroll-behavior: auto !important;
  }
}

/* 打印样式 */
@media print {
  .header,
  .footer,
  .theme-toggle,
  .btn {
    display: none !important;
  }
  
  body {
    background: white;
    color: black;
  }
  
  a {
    color: black;
    text-decoration: underline;
  }
}
</style>

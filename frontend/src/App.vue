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
  --text-secondary: #738a94;
  --text-tertiary: #9ca3af;
  --border-color: #e5eff5;
  --border-light: #f0f0f0;
  --accent-color: #3eb0ef;
  --accent-hover: #3498db;
  --accent-light: rgba(62, 176, 239, 0.1);
  --card-bg: #ffffff;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.08);
  --header-bg: rgba(255, 255, 255, 0.95);
  --header-blur: blur(12px);
  --footer-bg: #15171a;
  --footer-text: #ffffff;
  --code-bg: #f5f7f9;
  --code-text: #d63384;
  --hover-bg: #f5f7f9;
  --success-color: #2ecc71;
  --warning-color: #f39c12;
  --error-color: #e74c3c;
  --input-bg: #ffffff;
  --input-border: #d1d5db;
  --input-focus-border: #3eb0ef;
  --btn-primary-bg: #3eb0ef;
  --btn-primary-text: #ffffff;
  --btn-secondary-bg: #f5f7f9;
  --btn-secondary-text: #15171a;
  --tag-bg: #eef0f2;
  --tag-text: #738a94;
  --overlay-bg: rgba(0, 0, 0, 0.5);
  --scrollbar-thumb: #c1c1c1;
  --scrollbar-track: #f1f1f1;
  
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
  --text-color: #e6edf3;
  --text-secondary: #8b949e;
  --text-tertiary: #6e7681;
  --border-color: #30363d;
  --border-light: #21262d;
  --accent-color: #58a6ff;
  --accent-hover: #79b8ff;
  --accent-light: rgba(88, 166, 255, 0.15);
  --card-bg: #161b22;
  --card-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  --card-shadow-hover: 0 8px 24px rgba(0, 0, 0, 0.3);
  --header-bg: rgba(13, 17, 23, 0.95);
  --footer-bg: #010409;
  --footer-text: #e6edf3;
  --code-bg: #1c2128;
  --code-text: #ff7b72;
  --hover-bg: #21262d;
  --success-color: #3fb950;
  --warning-color: #d29922;
  --error-color: #f85149;
  --input-bg: #0d1117;
  --input-border: #30363d;
  --input-focus-border: #58a6ff;
  --btn-primary-bg: #58a6ff;
  --btn-primary-text: #0d1117;
  --btn-secondary-bg: #21262d;
  --btn-secondary-text: #e6edf3;
  --tag-bg: #21262d;
  --tag-text: #8b949e;
  --overlay-bg: rgba(0, 0, 0, 0.7);
  --scrollbar-thumb: #484f58;
  --scrollbar-track: #161b22;
}

html {
  scroll-behavior: smooth;
  -webkit-text-size-adjust: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

body {
  font-family: var(--font-sans);
  background-color: var(--bg-color);
  color: var(--text-color);
  line-height: 1.7;
  font-size: 16px;
  transition: background-color var(--transition-base), color var(--transition-base);
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

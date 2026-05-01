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
import { computed } from 'vue'
import { useThemeStore } from './stores/theme'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'

const themeStore = useThemeStore()
const isDarkMode = computed(() => themeStore.isDark)
</script>

<style>
/* 全局样式 - Ghost风格 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  --bg-color: #ffffff;
  --text-color: #15171a;
  --text-secondary: #738a94;
  --border-color: #e5eff5;
  --accent-color: #3eb0ef;
  --card-bg: #ffffff;
  --header-bg: #ffffff;
  --footer-bg: #15171a;
  --footer-text: #ffffff;
  --code-bg: #f5f5f5;
  --hover-bg: #f5f5f5;
}

.dark-mode {
  --bg-color: #1a1a2e;
  --text-color: #e0e0e0;
  --text-secondary: #8899a6;
  --border-color: #2a2a4a;
  --accent-color: #3eb0ef;
  --card-bg: #16213e;
  --header-bg: #16213e;
  --footer-bg: #0f0f23;
  --footer-text: #e0e0e0;
  --code-bg: #2a2a4a;
  --hover-bg: #2a2a4a;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  background-color: var(--bg-color);
  color: var(--text-color);
  line-height: 1.6;
  transition: background-color 0.3s, color 0.3s;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  min-height: calc(100vh - 200px);
}

a {
  color: var(--accent-color);
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .main-content {
    padding: 20px 15px;
  }
}
</style>

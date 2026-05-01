import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  // 深色模式状态
  const isDark = ref(localStorage.getItem('darkMode') === 'true')

  // 切换主题
  function toggle() {
    isDark.value = !isDark.value
    localStorage.setItem('darkMode', isDark.value)
  }

  return { isDark, toggle }
})

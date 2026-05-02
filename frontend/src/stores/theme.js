import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  // 护眼深色主题状态
  const isEyeCare = ref(localStorage.getItem('eyeCareTheme') === 'true')

  // 切换护眼主题
  function toggle() {
    isEyeCare.value = !isEyeCare.value
    localStorage.setItem('eyeCareTheme', isEyeCare.value)
  }

  return { isEyeCare, toggle }
})
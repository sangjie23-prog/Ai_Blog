import { ref, readonly } from 'vue'

const toasts = ref([])
let nextId = 0

export function useToast() {
  function showToast(message, type = 'info', duration = 3000) {
    const id = nextId++
    toasts.value.push({ id, message, type })

    if (duration > 0) {
      setTimeout(() => removeToast(id), duration)
    }

    return id
  }

  function removeToast(id) {
    const index = toasts.value.findIndex(t => t.id === id)
    if (index > -1) {
      toasts.value.splice(index, 1)
    }
  }

  function success(message, duration) {
    return showToast(message, 'success', duration)
  }

  function error(message, duration) {
    return showToast(message, 'error', duration)
  }

  function warning(message, duration) {
    return showToast(message, 'warning', duration)
  }

  function info(message, duration) {
    return showToast(message, 'info', duration)
  }

  return {
    toasts: readonly(toasts),
    showToast,
    removeToast,
    success,
    error,
    warning,
    info
  }
}

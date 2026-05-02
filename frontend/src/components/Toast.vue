<template>
  <Teleport to="body">
    <div class="toast-container">
      <TransitionGroup name="toast">
        <div
          v-for="toast in toasts"
          :key="toast.id"
          :class="['toast', `toast-${toast.type}`]"
          role="alert"
        >
          <span class="toast-icon">{{ typeIcons[toast.type] }}</span>
          <span class="toast-message">{{ toast.message }}</span>
          <button class="toast-close" @click="removeToast(toast.id)" aria-label="关闭">
            <svg viewBox="0 0 24 24" width="14" height="14">
              <path fill="currentColor" d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
            </svg>
          </button>
        </div>
      </TransitionGroup>
    </div>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue'

const toasts = ref([])
let nextId = 0

const typeIcons = {
  success: '✓',
  error: '✕',
  warning: '⚠',
  info: 'ℹ'
}

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

defineExpose({ showToast, success, error, warning, info, removeToast })
</script>

<style scoped>
.toast-container {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 10000;
  display: flex;
  flex-direction: column;
  gap: 10px;
  pointer-events: none;
}

.toast {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 280px;
  max-width: 420px;
  padding: 14px 18px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 500;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15), 0 2px 6px rgba(0, 0, 0, 0.1);
  pointer-events: auto;
  backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  animation: toastSlideIn 0.35s cubic-bezier(0.21, 1.02, 0.73, 1);
}

.toast-icon {
  flex-shrink: 0;
  width: 22px;
  height: 22px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  border-radius: 50%;
}

.toast-message {
  flex: 1;
  line-height: 1.4;
}

.toast-close {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  border: none;
  background: transparent;
  cursor: pointer;
  opacity: 0.5;
  transition: opacity 0.2s;
  padding: 0;
  color: inherit;
}

.toast-close:hover {
  opacity: 1;
}

/* 成功 */
.toast-success {
  background: rgba(46, 204, 113, 0.12);
  border: 1px solid rgba(46, 204, 113, 0.3);
  color: #2ecc71;
}

.toast-success .toast-icon {
  background: rgba(46, 204, 113, 0.2);
  color: #2ecc71;
}

/* 错误 */
.toast-error {
  background: rgba(231, 76, 60, 0.12);
  border: 1px solid rgba(231, 76, 60, 0.3);
  color: #e74c3c;
}

.toast-error .toast-icon {
  background: rgba(231, 76, 60, 0.2);
  color: #e74c3c;
}

/* 警告 */
.toast-warning {
  background: rgba(241, 196, 15, 0.12);
  border: 1px solid rgba(241, 196, 15, 0.3);
  color: #f1c40f;
}

.toast-warning .toast-icon {
  background: rgba(241, 196, 15, 0.2);
  color: #f1c40f;
}

/* 信息 */
.toast-info {
  background: rgba(52, 152, 219, 0.12);
  border: 1px solid rgba(52, 152, 219, 0.3);
  color: #3498db;
}

.toast-info .toast-icon {
  background: rgba(52, 152, 219, 0.2);
  color: #3498db;
}

/* 动画 */
.toast-enter-active {
  animation: toastSlideIn 0.35s cubic-bezier(0.21, 1.02, 0.73, 1);
}

.toast-leave-active {
  animation: toastSlideOut 0.3s cubic-bezier(0.55, 0.06, 0.68, 0.19);
}

@keyframes toastSlideIn {
  from {
    opacity: 0;
    transform: translateX(100%) scale(0.9);
  }
  to {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
}

@keyframes toastSlideOut {
  from {
    opacity: 1;
    transform: translateX(0) scale(1);
  }
  to {
    opacity: 0;
    transform: translateX(100%) scale(0.9);
  }
}

/* 响应式 */
@media (max-width: 479px) {
  .toast-container {
    top: 10px;
    right: 10px;
    left: 10px;
  }

  .toast {
    min-width: auto;
    max-width: 100%;
    padding: 12px 14px;
    font-size: 13px;
  }
}
</style>

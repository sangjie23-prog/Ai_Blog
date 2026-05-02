<template>
  <Teleport to="body">
    <Transition name="viewer-fade">
      <div 
        v-if="visible" 
        class="image-viewer-overlay" 
        @click.self="closeViewer"
        @keydown="handleKeydown"
        tabindex="0"
      >
        <div class="image-viewer-container">
          <!-- 工具栏 -->
          <div class="viewer-toolbar">
            <div class="toolbar-left">
              <span class="image-info">{{ imageAlt || '图片查看' }}</span>
            </div>
            <div class="toolbar-center">
              <div class="zoom-controls">
                <button class="toolbar-btn" @click="zoomOut" title="缩小">
                  <svg viewBox="0 0 24 24" width="20" height="20">
                    <path fill="currentColor" d="M19 13H5v-2h14v2z"/>
                  </svg>
                </button>
                <span class="zoom-level">{{ Math.round(scale * 100) }}%</span>
                <button class="toolbar-btn" @click="zoomIn" title="放大">
                  <svg viewBox="0 0 24 24" width="20" height="20">
                    <path fill="currentColor" d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/>
                  </svg>
                </button>
                <button class="toolbar-btn" @click="resetZoom" title="重置缩放">
                  <svg viewBox="0 0 24 24" width="20" height="20">
                    <path fill="currentColor" d="M12 5V1L7 6l5 5V7c3.31 0 6 2.69 6 6s-2.69 6-6 6-6-2.69-6-6H4c0 4.42 3.58 8 8 8s8-3.58 8-8-3.58-8-8-8z"/>
                  </svg>
                </button>
              </div>
            </div>
            <div class="toolbar-right">
              <button class="toolbar-btn" @click="rotateLeft" title="向左旋转">
                <svg viewBox="0 0 24 24" width="20" height="20">
                  <path fill="currentColor" d="M7.11 8.53L5.7 7.11C4.8 8.27 4.24 9.61 4.07 11h2.02c.14-.87.49-1.72 1.02-2.47zM6.09 13H4.07c.17 1.39.72 2.73 1.62 3.89l1.41-1.42c-.52-.75-.87-1.59-1.01-2.47zm1.01 5.32c1.16.9 2.51 1.44 3.9 1.61V17.9c-.87-.15-1.71-.49-2.46-1.03L7.1 18.32zM13 4.07V1L8.45 5.55 13 10V6.09c2.84.48 5 2.94 5 5.91s-2.16 5.43-5 5.91v2.02c3.95-.49 7-3.85 7-7.93s-3.05-7.44-7-7.93z"/>
                </svg>
              </button>
              <button class="toolbar-btn" @click="rotateRight" title="向右旋转">
                <svg viewBox="0 0 24 24" width="20" height="20">
                  <path fill="currentColor" d="M15.55 5.55L11 1v3.07C7.06 4.56 4 7.92 4 12s3.05 7.44 7 7.93v-2.02c-2.84-.48-5-2.94-5-5.91s2.16-5.43 5-5.91V10l4.55-4.45zM19.93 11c-.17-1.39-.72-2.73-1.62-3.89l-1.42 1.42c.54.75.88 1.6 1.02 2.47h2.02zM13 17.9v2.02c1.39-.17 2.74-.71 3.9-1.61l-1.44-1.44c-.75.54-1.59.89-2.46 1.03zm3.89-2.42l1.42 1.41c.9-1.16 1.45-2.5 1.62-3.89h-2.02c-.14.87-.48 1.72-1.02 2.48z"/>
                </svg>
              </button>
              <button class="toolbar-btn close-btn" @click="closeViewer" title="关闭 (ESC)">
                <svg viewBox="0 0 24 24" width="20" height="20">
                  <path fill="currentColor" d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"/>
                </svg>
              </button>
            </div>
          </div>

          <!-- 图片显示区域 -->
          <div class="viewer-image-wrapper" @wheel.prevent="handleWheel">
            <div 
              class="viewer-image-container"
              :style="imageStyle"
            >
              <img 
                :src="imageUrl" 
                :alt="imageAlt"
                class="viewer-image"
                @load="handleImageLoad"
                @error="handleImageError"
              />
            </div>
          </div>

          <!-- 底部提示 -->
          <div class="viewer-hint">
            <span>滚轮缩放 · 拖拽移动 · ESC 关闭</span>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  imageUrl: {
    type: String,
    default: ''
  },
  imageAlt: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue', 'close'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 缩放和旋转状态
const scale = ref(1)
const rotation = ref(0)
const translateX = ref(0)
const translateY = ref(0)
const isDragging = ref(false)
const dragStartX = ref(0)
const dragStartY = ref(0)
const imageLoaded = ref(false)

// 缩放控制
const minScale = 0.1
const maxScale = 5
const scaleStep = 0.2

function zoomIn() {
  scale.value = Math.min(scale.value + scaleStep, maxScale)
}

function zoomOut() {
  scale.value = Math.max(scale.value - scaleStep, minScale)
}

function resetZoom() {
  scale.value = 1
  rotation.value = 0
  translateX.value = 0
  translateY.value = 0
}

// 旋转控制
function rotateLeft() {
  rotation.value -= 90
}

function rotateRight() {
  rotation.value += 90
}

// 滚轮缩放
function handleWheel(event) {
  const delta = event.deltaY > 0 ? -scaleStep : scaleStep
  scale.value = Math.max(minScale, Math.min(maxScale, scale.value + delta))
}

// 图片样式计算
const imageStyle = computed(() => ({
  transform: `translate(${translateX.value}px, ${translateY.value}px) scale(${scale.value}) rotate(${rotation.value}deg)`,
  transition: isDragging.value ? 'none' : 'transform 0.3s ease'
}))

// 拖拽功能
function handleMouseDown(event) {
  if (event.button !== 0) return
  isDragging.value = true
  dragStartX.value = event.clientX - translateX.value
  dragStartY.value = event.clientY - translateY.value
  document.body.style.cursor = 'grabbing'
}

function handleMouseMove(event) {
  if (!isDragging.value) return
  translateX.value = event.clientX - dragStartX.value
  translateY.value = event.clientY - dragStartY.value
}

function handleMouseUp() {
  isDragging.value = false
  document.body.style.cursor = ''
}

// 键盘事件
function handleKeydown(event) {
  switch (event.key) {
    case 'Escape':
      closeViewer()
      break
    case '+':
    case '=':
      zoomIn()
      break
    case '-':
      zoomOut()
      break
    case '0':
      resetZoom()
      break
    case 'ArrowLeft':
      rotateLeft()
      break
    case 'ArrowRight':
      rotateRight()
      break
  }
}

// 图片加载
function handleImageLoad() {
  imageLoaded.value = true
}

function handleImageError() {
  console.error('图片加载失败:', props.imageUrl)
}

// 关闭查看器
function closeViewer() {
  emit('close')
  emit('update:modelValue', false)
  resetZoom()
}

// 生命周期
onMounted(() => {
  document.addEventListener('mousedown', handleMouseDown)
  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('mousedown', handleMouseDown)
  document.removeEventListener('mousemove', handleMouseMove)
  document.removeEventListener('mouseup', handleMouseUp)
  document.removeEventListener('keydown', handleKeydown)
  document.body.style.cursor = ''
})
</script>

<style scoped>
.image-viewer-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.9);
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  outline: none;
}

.image-viewer-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* 工具栏样式 */
.viewer-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  z-index: 10;
}

.toolbar-left {
  flex: 1;
}

.image-info {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  font-weight: 500;
}

.toolbar-center {
  flex: 2;
  display: flex;
  justify-content: center;
}

.zoom-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.zoom-level {
  color: rgba(255, 255, 255, 0.9);
  font-size: 13px;
  font-weight: 600;
  min-width: 50px;
  text-align: center;
  font-family: monospace;
}

.toolbar-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
  gap: 4px;
}

.toolbar-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.2s ease;
}

.toolbar-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border-color: rgba(255, 255, 255, 0.3);
}

.toolbar-btn:active {
  transform: scale(0.95);
}

.close-btn:hover {
  background: rgba(255, 71, 87, 0.3);
  border-color: rgba(255, 71, 87, 0.5);
  color: #ff4757;
}

/* 图片显示区域 */
.viewer-image-wrapper {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  cursor: grab;
  position: relative;
}

.viewer-image-wrapper:active {
  cursor: grabbing;
}

.viewer-image-container {
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: 90%;
  max-height: 90%;
}

.viewer-image {
  max-width: 100%;
  max-height: 80vh;
  object-fit: contain;
  user-select: none;
  -webkit-user-drag: none;
  pointer-events: none;
}

/* 底部提示 */
.viewer-hint {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  padding: 8px 16px;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  pointer-events: none;
}

/* 过渡动画 */
.viewer-fade-enter-active,
.viewer-fade-leave-active {
  transition: opacity 0.3s ease;
}

.viewer-fade-enter-from,
.viewer-fade-leave-to {
  opacity: 0;
}

.viewer-fade-enter-active .viewer-image-container,
.viewer-fade-leave-active .viewer-image-container {
  transition: transform 0.3s ease;
}

.viewer-fade-enter-from .viewer-image-container,
.viewer-fade-leave-to .viewer-image-container {
  transform: scale(0.8);
}

/* 响应式设计 */
@media (max-width: 767px) {
  .viewer-toolbar {
    padding: 8px 12px;
  }
  
  .image-info {
    font-size: 12px;
  }
  
  .toolbar-btn {
    width: 32px;
    height: 32px;
  }
  
  .zoom-level {
    font-size: 12px;
    min-width: 40px;
  }
  
  .viewer-hint {
    font-size: 11px;
    padding: 6px 12px;
  }
}

@media (max-width: 479px) {
  .viewer-toolbar {
    padding: 6px 8px;
  }
  
  .toolbar-left {
    display: none;
  }
  
  .toolbar-center {
    flex: 1;
  }
  
  .toolbar-right {
    flex: 0;
  }
  
  .zoom-controls {
    gap: 4px;
  }
  
  .toolbar-btn {
    width: 28px;
    height: 28px;
  }
  
  .viewer-image-container {
    max-width: 95%;
    max-height: 95%;
  }
}
</style>

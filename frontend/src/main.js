import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

// 引入代码高亮样式
import 'highlight.js/styles/github.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')

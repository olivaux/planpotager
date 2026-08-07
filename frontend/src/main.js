import { createApp } from 'vue'
import VueKonva from 'vue-konva'
import './style.css'
import App from './App.vue'
import router from './router/index.js'

createApp(App).use(router).use(VueKonva).mount('#app')

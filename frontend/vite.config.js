import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Backend Spring Boot (server.port dans application.properties)
const BACKEND_ORIGIN = 'http://localhost:8080'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': BACKEND_ORIGIN,
      '/oauth2': BACKEND_ORIGIN,
      '/login': BACKEND_ORIGIN,
    },
  },
})

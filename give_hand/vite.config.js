// import { defineConfig } from 'vite'
// import vue from '@vitejs/plugin-vue'

// export default defineConfig({
//   plugins: [vue()],
//   server: {
//     port: 5173,
//     proxy: {
//       // 本地开发时把 /api 代理到你的后端
//       '/api': {
//         target: 'https://behind-5.onrender.com:8080',
//         changeOrigin: true
//       }
//     }
//   }
// })

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'https://backend-8-ach8.onrender.com',
        changeOrigin: true,
        secure: true,
      }
    }
  }
})
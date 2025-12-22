<template>
  <div class="setting-page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <h2 class="title">设置</h2>
    </header>

    <div class="section">
      <div class="setting-item">
        <span>消息通知</span>
        <label class="switch">
          <input
            type="checkbox"
            v-model="notifications"
            @change="updateSetting"
          />
          <span class="slider"></span>
        </label>
      </div>
    </div>

    <!-- 可扩展其他设置项 -->
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { updateSettingsApi } from '../api/settings'

const notifications = ref(false)

// 获取当前设置（可选：如果你有 GET /settings 接口）
// 如果没有，可以跳过，只支持修改

async function updateSetting() {
  try {
    await updateSettingsApi(notifications.value)
  } catch (error) {
    console.error('更新设置失败', error)
    // 回滚状态
    notifications.value = !notifications.value
  }
}

// 如果后端支持 GET /api/settings，可以在这里加载初始值
onMounted(async () => {
  // const res = await axios.get('/api/settings')
  // notifications.value = res.data.notifications
})
</script>

<style scoped>
.setting-page {
  min-height: 100vh;
  background: #f5f7fa;
  padding-top: 50px;
}

.top {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #fff;
  box-shadow: 0 1px 5px rgba(0,0,0,0.1);
  z-index: 100;
}
.back {
  border: none;
  background: transparent;
  font-size: 18px;
  margin-right: 12px;
  color: #333;
}
.title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.section {
  padding: 16px;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  padding: 14px 16px;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

/* 开关样式 */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 26px;
}
.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}
.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .3s;
  border-radius: 34px;
}
.slider:before {
  position: absolute;
  content: "";
  height: 22px;
  width: 22px;
  left: 2px;
  bottom: 2px;
  background-color: white;
  transition: .3s;
  border-radius: 50%;
}
input:checked + .slider {
  background-color: #2d8cf0;
}
input:checked + .slider:before {
  transform: translateX(24px);
}
</style>
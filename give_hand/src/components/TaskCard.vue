<!-- TaskCard.vue -->
<template>
  <div class="task-card" @click="$emit('click')">
    <!-- 显示任务主图（不是用户头像！） -->
    <img 
      v-if="task.url" 
      :src="task.url" 
      alt="任务图片"
      class="task-image"
    />
    <div v-else class="task-placeholder">
      {{ task.title?.slice(0, 2) || '无' }}
    </div>

    <div class="task-info">
      <h3 class="title">{{ task.title }}</h3>
      <p class="price">¥{{ task.price }}</p>
      <p class="username">{{ task.username }}</p>
    </div>
  </div>
</template>

<script setup>
defineProps({
  task: {
    type: Object,
    required: true
  }
})

// 触发点击事件（父组件用 @click）
defineEmits(['click'])
</script>

<style scoped>
.task-card {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.task-image {
  width: 100%;
  aspect-ratio: 3 / 4; /* 竖图比例 */
  object-fit: cover;
}

.task-placeholder {
  width: 100%;
  aspect-ratio: 3 / 4;
  background: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #999;
}

.task-info {
  padding: 8px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 14px;
  font-weight: 600;
  margin: 0 0 6px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.price {
  font-size: 16px;
  font-weight: bold;
  color: #e74c3c;
  margin: 0 0 4px;
}

.username {
  font-size: 12px;
  color: #666;
  margin: 0;
}
</style>
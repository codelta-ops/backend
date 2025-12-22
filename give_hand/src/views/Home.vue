<template>
  <div class="page">
    <header class="header">
      <input
        class="search"
        v-model="keyword"
        placeholder="搜索帖子、用户或金额"
        @keyup.enter="doSearch"
      />
    </header>

    <main class="main" ref="mainRef">
      <!-- 下拉刷新提示 -->
      <div v-if="refreshing" class="refresh-indicator">
        <div class="refresh-spinner"></div>
        <span>刷新中...</span>
      </div>

      <!-- 帖子展示网格 -->
      <div class="grid">
        <TaskCard
          v-for="item in tasks"
          :key="item.id"
          :task="item"
          @click="goDetail(item.id)"
          class="task-card"
        />
      </div>

      <!-- 加载更多按钮 -->
      <div class="load-more" v-if="hasMore">
        <button @click="loadMore" :disabled="loading">
          {{ loading ? '加载中...' : '加载更多' }}
        </button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getTasksApi } from '../api/tasks'
import TaskCard from '../components/TaskCard.vue'

const router = useRouter()
const keyword = ref('')
const tasks = ref([])
const page = ref(1)       // 当前正在显示的最后一页（初始为1）
const size = 8
const total = ref(0)
const loading = ref(false)
const refreshing = ref(false)
const startY = ref(0)
const pullDistance = ref(0)
const maxPullDistance = 100
const mainRef = ref(null)

// 是否还能加载更多：当前总条数 < 后端总条数
const hasMore = computed(() => {
  return tasks.value.length > 0 && tasks.value.length < total.value
})

// 获取任务列表
async function fetchTasks(isRefresh = false) {
  if (loading.value) return

  // 如果是刷新，重置 page 为 1
  if (isRefresh) {
    page.value = 1
  }

  const params = {
    page: page.value,
    size: size,
    t: Date.now()
  }

  loading.value = true
  refreshing.value = isRefresh

  try {
    const data = await getTasksApi(params)

    if (isRefresh) {
      tasks.value = data.results || []
    } else {
      // 追加新数据
      tasks.value = [...tasks.value, ...(data.results || [])]
    }

    total.value = data.total || 0
  } catch (error) {
    console.error('获取任务失败:', error)
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

// 下拉刷新：重新加载第一页
async function refresh() {
  if (refreshing.value || loading.value) return
  pullDistance.value = 0
  await fetchTasks(true)
}

// 加载更多：加载下一页
function loadMore() {
  if (!hasMore.value || loading.value) return
  page.value += 1 // 👈 先递增 page，再请求
  fetchTasks(false)
}

function goDetail(id) {
  router.push(`/orders/${id}`)
}

function doSearch() {
  if (keyword.value.trim()) {
    router.push({ name: 'SearchResult', query: { keyword: keyword.value } })
  }
}

// 触摸事件处理
function onTouchStart(e) {
  if (refreshing.value || loading.value) return
  startY.value = e.touches[0].clientY
}

function onTouchMove(e) {
  if (refreshing.value || loading.value) return

  const el = mainRef.value
  if (!el || el.scrollTop > 0) return

  const currentY = e.touches[0].clientY
  const distance = currentY - startY.value

  if (distance > 0) {
    e.preventDefault()
    pullDistance.value = Math.min(distance * 0.5, maxPullDistance)

    if (pullDistance.value >= 60 && !refreshing.value) {
      refresh()
    }
  }
}

function onTouchEnd() {
  pullDistance.value = 0
}

onMounted(() => {
  fetchTasks(true) // 初始加载第一页

  const el = mainRef.value
  if (el) {
    el.addEventListener('touchstart', onTouchStart, { passive: false })
    el.addEventListener('touchmove', onTouchMove, { passive: false })
    el.addEventListener('touchend', onTouchEnd)

    onUnmounted(() => {
      el.removeEventListener('touchstart', onTouchStart)
      el.removeEventListener('touchmove', onTouchMove)
      el.removeEventListener('touchend', onTouchEnd)
    })
  }
})
</script>

<style scoped>
/* 样式保持不变 */
.page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  padding-bottom: 60px;
  overflow-x: hidden;
}

.header {
  padding: 16px;
  background-color: #f5f5f5;
  z-index: 10;
}

.search {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.main {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 16px;
  position: relative;
}

.refresh-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 12px;
  color: #666;
  font-size: 14px;
}

.refresh-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #ddd;
  border-top-color: #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 8px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.task-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: transform 0.2s ease;
  aspect-ratio: 9 / 12;
  padding: 8px;
  overflow: hidden;
}

.task-card :deep(*) {
  font-size: 0.8em;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.load-more {
  display: flex;
  justify-content: center;
  padding: 16px;
}

.load-more button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.load-more button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
</style>
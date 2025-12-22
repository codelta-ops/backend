<template>
  <div class="page">
    <!-- 搜索区域 -->
    <header class="header">
      <input
        v-model="keyword"
        class="search"
        placeholder="搜索任务标题或用户"
        @keyup.enter="handleSearch"
      />
    </header>

    <main class="main" ref="mainRef">
      <!-- 下拉刷新提示（可选，视觉弱化） -->
      <div v-if="refreshing" class="refresh-indicator">
        <div class="refresh-spinner"></div>
        <span>刷新中...</span>
      </div>

      <!-- 空状态 -->
      <div v-if="tasks.length === 0 && !loading" class="empty">
        没有找到相关结果<br />换个关键词试试吧
      </div>

      <!-- 任务卡片网格（两列） -->
      <div class="grid">
        <TaskCard
          v-for="item in tasks"
          :key="item.id"
          :task="item"
          @click="goDetail(item.id)"
        />
      </div>

      <!-- 加载更多按钮（样式匹配） -->
      <div class="load-more" v-if="hasMore">
        <button @click="loadMore" :disabled="loading" class="load-more-btn">
          {{ loading ? '加载中...' : '加载更多' }}
        </button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { searchTasksApi } from '../api/tasks'
import TaskCard from '../components/TaskCard.vue'

const route = useRoute()
const router = useRouter()

const keyword = ref(route.query.keyword || '')
const tasks = ref([])
const page = ref(1)
const size = 8
const total = ref(0)
const loading = ref(false)
const refreshing = ref(false)
const mainRef = ref(null)

const hasMore = computed(() => {
  return tasks.value.length < total.value
})

async function fetchSearchResults(isRefresh = false) {
  console.log('🚀 [1] 进入 fetchSearchResults, isRefresh:', isRefresh);
  console.log('🔍 [2] 当前 keyword.value:', keyword.value);

  if (isRefresh) page.value = 1;

  const kw = keyword.value.trim();
  console.log('📝 [3] 处理后的关键词 kw:', JSON.stringify(kw)); // 用 JSON.stringify 看是否是空字符串

  if (!kw) {
    console.log('⚠️ [4] 关键词为空，清空列表');
    tasks.value = [];
    total.value = 0;
    return;
  }

  loading.value = true;
  refreshing.value = isRefresh;

  try {
    const params = { keyword: kw, page: page.value, size: size };
    console.log('📡 [5] 准备调用 searchTasksApi，参数:', params);

    // 👇 关键：打印函数本身
    console.log('🔧 [6] searchTasksApi 函数:', searchTasksApi);

    const res = await searchTasksApi(params);
    console.log('✅ [7] 收到 API 响应:', res);

    const list = res.results || [];
    console.log('📋 [8] 解析出的列表长度:', list.length);

    if (isRefresh) {
      tasks.value = list;
    } else {
      tasks.value.push(...list);
    }
    total.value = res.data?.total || 0;
  } catch (error) {
    console.error('❌ [9] 搜索失败:', error);
    tasks.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
    refreshing.value = false;
    console.log('🔚 [10] fetchSearchResults 结束');
  }
}

function handleSearch() {
  const kw = keyword.value.trim()
  if (kw) {
    router.replace({ query: { keyword: kw } })
  }
}

watch(
  () => route.query.keyword,
  (newKeyword) => {
    const kw = (newKeyword || '').trim()
    keyword.value = kw

    if (kw) {
      page.value = 1
      tasks.value = []
      total.value = 0
      fetchSearchResults(true)
    } else {
      tasks.value = []
      total.value = 0
    }
  },
  { immediate: true }
)

function loadMore() {
  if (!hasMore.value || loading.value) return
  page.value++
  fetchSearchResults(false)
}

function goDetail(id) {
  router.push(`/orders/${id}`) 
}
</script>

<style scoped>
.page {
  min-height: 100%;
  background: #f0f4ff;
  padding-bottom: 70px;
}

.header {
  padding: 14px 16px 8px;
}

.search {
  width: 100%;
  height: 36px;
  border-radius: 999px;
  border: none;
  padding: 0 14px;
  font-size: 14px;
}

.main {
  padding: 8px 12px 80px;
  position: relative;
}

.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-gap: 10px;
}

.empty {
  text-align: center;
  color: #999;
  margin-top: 40px;
  line-height: 1.5;
}

/* 下拉刷新提示（简化） */
.refresh-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  color: #2d8cf0;
  font-size: 14px;
}
.refresh-spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #ddd;
  border-top-color: #2d8cf0;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 6px;
}
@keyframes spin {
  to { transform: rotate(360deg); }
}

/* 加载更多按钮 */
.load-more {
  text-align: center;
  margin-top: 16px;
}
.load-more-btn {
  background: #2d8cf0;
  color: white;
  border: none;
  border-radius: 999px;
  padding: 8px 24px;
  font-size: 14px;
  cursor: pointer;
}
.load-more-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
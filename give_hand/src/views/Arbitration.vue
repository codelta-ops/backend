<template>
  <div class="page">
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">仲裁记录</div>
    </header>
    <main class="main">
      <div v-if="arbitrations.length === 0" class="empty">
        你还没有仲裁记录<br />赶紧去申请吧！
      </div>
      <div class="grid" v-else>
        <div
          class="card"
          v-for="item in arbitrations"
          :key="item.id"
          @click="goArbitrationDetail(item)"
        >
          <div class="title">{{ item.title }}</div>
          <div class="status">{{ statusMap[item.status] }}</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getArbitrationListApi } from '../api/arbitration'

const router = useRouter()
const arbitrations = ref([])

// 状态映射表
const statusMap = {
  1: '已提交',
  2: '处理中',
  3: '已完成'
}

async function fetchArbitrationList() {
  const data = await getArbitrationListApi({ page: 1, size: 20 })
  arbitrations.value = data.results || []
}

function goArbitrationDetail(item) {
  router.push({ name: 'ArbitrationDetail', params: { arbitrationId: item.id } })
}

onMounted(fetchArbitrationList)
</script>

<style scoped>
.page {
  min-height: 100%;
  background: #f0f4ff;
}
.top {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  background: #5cb0ff;
  color: #fff;
}
.back {
  border: none;
  background: transparent;
  color: inherit;
  font-size: 18px;
  margin-right: 8px;
}
.main {
  padding: 12px 12px 70px;
}
.empty {
  text-align: center;
  padding-top: 80px;
  color: #999;
  font-size: 14px;
}
.grid {
  display: grid;
  grid-template-columns: 1fr; /* 单列布局，横向占满 */
  gap: 10px;
}
.card {
  background: #fff;
  border-radius: 16px;
  padding: 12px 14px;
  margin-bottom: 10px;
}
.title {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 8px;
}
.status {
  font-size: 12px;
  color: #999;
}
</style>
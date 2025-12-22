<template>
  <div class="page">
    <header class="header">
      <h2>消息列表</h2>
    </header>
    <main class="main">
      <div v-if="list.length === 0" style="padding: 20px; color: #999;">
        暂无消息
      </div>
      <div v-else>共 {{ list.length }} 条消息</div>
      <MessageItem v-for="(item, index) in list" :key="index" :message="item" @click="openChat(item)" />
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getMessageListApi } from '../api/messages'
import MessageItem from '../components/MessageItem.vue'

const router = useRouter()
const list = ref([])

async function fetchList() {
  const res = await getMessageListApi()
  list.value = res || [] // ✅ 正确赋值
}

function openChat(item) {
  if (!item.uid) return
  router.push(`/chat/${item.uid}`)
}

onMounted(fetchList)
</script>

<style scoped>
.page {
  min-height: 100%;
  background: #f0f4ff;
}
.header {
  padding: 14px 16px 8px;
}
.main {
  padding: 0 12px 70px;
}
</style>
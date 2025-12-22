<template>
  <div class="page">
    <!-- 头部 -->
    <header class="top">
      <button class="back" @click="$router.back()">←</button>
      <div class="title">{{ otherUser.username || '聊天' }}</div>
    </header>

    <!-- 聊天内容区 -->
    <main class="main" ref="scrollEl">
      <!-- 时间分隔线 -->
      <div v-for="(msg, index) in displayedMessages" :key="msg.msgId" class="msg-item">
        <!-- 时间显示（间隔>5分钟时显示） -->
        <div v-if="msg.showTime" class="time-divider">
          {{ formatTime(msg.time) }}
        </div>

        <!-- 消息行 -->
        <div class="msg-row" :class="{ mine: msg.isMine }">
          <!-- 对方头像 -->
          <div v-if="!msg.isMine" class="avatar">
            <img :src="otherUser.avatar || 'https://api.dicebear.com/7.x/initials/svg?seed=' + otherUser.username" alt="avatar" />
          </div>

          <!-- 气泡 -->
          <div class="bubble-container">
            <div class="bubble" :class="{ mine: msg.isMine }">
              {{ msg.content }}
            </div>
          </div>

          <!-- 自己头像 -->
          <div v-if="msg.isMine" class="avatar">
            <img :src="currentUserAvatar || 'https://api.dicebear.com/7.x/initials/svg?seed=Me'" alt="avatar" />
          </div>
        </div>
      </div>
    </main>

    <!-- 底部输入区 -->
    <footer class="footer">
      <input
        v-model="text"
        class="input"
        placeholder="输入消息..."
        @keyup.enter="send"
        @focus="onInputFocus"
      />
      <button class="send" @click="send">发送</button>
    </footer>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { getChatHistoryApi, sendMessageApi } from '../api/messages'

const route = useRoute()
const otherUser = reactive({})
const messages = ref([])
const displayedMessages = ref([])
const text = ref('')
const scrollEl = ref(null)
const currentUserAvatar = ref('')

// 获取当前用户信息（模拟）
function getCurrentUserInfo() {
  return {
    avatar: 'https://api.dicebear.com/7.x/initials/svg?seed=Me',
    username: '我'
  }
}

// 时间格式化
function formatTime(time) {
  if (!time) return ''
  const date = new Date(time)
  return `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`
}

// 处理消息，添加时间标记
function processMessages(msgs) {
  return msgs.map((msg, index) => {
    let showTime = false
    if (index === 0) {
      showTime = true
    } else {
      const currentTime = new Date(msg.time).getTime()
      const prevTime = new Date(msgs[index - 1].time).getTime()
      const diffMinutes = Math.abs(currentTime - prevTime) / (1000 * 60)
      if (diffMinutes > 5) {
        showTime = true
      }
    }
    return { ...msg, showTime }
  })
}

async function fetchChat() {
  try {
    const data = await getChatHistoryApi(route.params.userId)
    Object.assign(otherUser, data.otherUser || {})
    messages.value = data.messages || []
    displayedMessages.value = processMessages(messages.value)
    currentUserAvatar.value = getCurrentUserInfo().avatar
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('获取聊天失败:', error)
  }
}

function scrollToBottom() {
  if (scrollEl.value) {
    scrollEl.value.scrollTop = scrollEl.value.scrollHeight
  }
}

async function send() {
  if (!text.value.trim()) return
  const content = text.value.trim()
  text.value = ''
  try {
    const res = await sendMessageApi({
      receiverId: otherUser.userUid || route.params.userId,
      content
    })
    const newMsg = {
      msgId: res.msgId,
      content: res.content,
      time: res.time,
      isMine: true
    }
    messages.value.push(newMsg)
    displayedMessages.value = processMessages(messages.value)
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('发送失败:', error)
    text.value = content
  }
}

function onInputFocus() {
  setTimeout(() => {
    scrollToBottom()
  }, 100)
}

onMounted(fetchChat)
</script>

<style scoped>
.page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f4ff;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.top {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #4a90e2;
  color: white;
  position: sticky;
  top: 0;
  z-index: 10;
}

.back {
  border: none;
  background: transparent;
  color: inherit;
  font-size: 18px;
  margin-right: 8px;
  cursor: pointer;
}

.title {
  flex: 1;
  text-align: center;
  font-weight: 500;
  font-size: 16px;
}

.main {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
  background: #f0f4ff;
  position: relative;
}

.msg-item {
  margin-bottom: 12px;
}

.time-divider {
  text-align: center;
  font-size: 12px;
  color: #888;
  margin: 10px 0;
  position: relative;
}

.time-divider::before {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  top: 50%;
  height: 1px;
  background: #ddd;
  z-index: 0;
}

.time-divider span {
  background: #f0f4ff;
  padding: 0 8px;
  position: relative;
  z-index: 1;
}

.msg-row {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  max-width: 100%;
}

.msg-row.mine {
  justify-content: flex-end;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.bubble-container {
  display: flex;
  flex-direction: column;
  max-width: 70%;
  min-width: 40px;
}

.bubble {
  padding: 12px 16px;
  border-radius: 18px;
  background: #ffffff;
  color: #333;
  word-wrap: break-word;
  box-shadow: 0 1px 3px rgba(0,0,0,0.1);
  position: relative;
  max-width: 100%;
}

.bubble.mine {
  background: #4a90e2;
  color: white;
  border-bottom-right-radius: 4px;
}

.msg-row:not(.mine) .bubble {
  border-top-left-radius: 4px;
}

.footer {
  display: flex;
  padding: 12px;
  background: white;
  border-top: 1px solid #eee;
  position: sticky;
  bottom: 0;
  z-index: 10;
}

.input {
  flex: 1;
  height: 40px;
  border-radius: 20px;
  border: 1px solid #ddd;
  padding: 0 12px;
  font-size: 14px;
  outline: none;
  background: #f5f5f5;
}

.input:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.send {
  width: 60px;
  margin-left: 12px;
  height: 40px;
  border-radius: 20px;
  border: none;
  background: #4a90e2;
  color: white;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.2s;
}

.send:hover {
  background: #3a7bc8;
}

/* 响应式 */
@media (max-width: 768px) {
  .page {
    height: 100vh;
  }
  
  .main {
    padding: 12px;
  }
  
  .avatar {
    width: 32px;
    height: 32px;
  }
  
  .bubble {
    padding: 10px 14px;
    font-size: 14px;
  }
  
  .footer {
    padding: 10px;
  }
  
  .input {
    height: 36px;
    padding: 0 10px;
  }
  
  .send {
    width: 50px;
    height: 36px;
    font-size: 13px;
  }
}
</style>
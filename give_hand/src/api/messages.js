import http from './http'

export function getMessageListApi() {
  return http.get('/messages/list')
}

export function getChatHistoryApi(userId) {
  return http.get(`/messages/chat/${userId}`)
}

export function sendMessageApi(payload) {
  return http.post('/messages/send', payload)
}

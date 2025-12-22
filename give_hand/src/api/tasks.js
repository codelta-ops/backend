import http from './http'

export function getTasksApi(params) {
  return http.get('/tasks/list', { params })
}

export function searchTasksApi(params) {
  return http.get('/tasks/search', { params })
}

export function getTaskDetailApi(taskId) {
  return http.get(`/tasks/${taskId}`)
}

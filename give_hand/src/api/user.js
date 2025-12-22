import http from './http'

export function getUserInfoApi() {
  return http.get('/user/info')
}

export function updateUserInfoApi(payload) {
  return http.put('/user/update', payload)
}

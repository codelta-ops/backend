import http from './http'

export function loginApi(payload) {
  return http.post('/auth/login', payload)
}

export function registerApi(payload) {
  return http.post('/auth/register', payload)
}

export function logoutApi() {
  return http.post('/auth/logout')
}

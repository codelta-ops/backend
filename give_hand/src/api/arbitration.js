import http from './http'

export function getArbitrationListApi(params) {
  return http.get('/arbitration/list', { params })
}

export function applyArbitrationApi(payload) {
  return http.post('/arbitration/apply', payload)
}

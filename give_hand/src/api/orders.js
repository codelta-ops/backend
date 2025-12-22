import http from './http'

export function createOrderApi(payload) {
  return http.put('/orders/create', payload)
}

export function getOrderListApi(params) {
  return http.get('/orders/list', { params })
}

export function getOrderDetailApi(orderId) {
  return http.get(`/orders/${orderId}`)
}

export function acceptOrderApi(orderId) {
  return http.put(`/orders/${orderId}/accept`)
}

export function completeOrderApi(orderId) {
  return http.put(`/orders/${orderId}/complete`)
}

export function cancelOrderApi(orderId) {
  return http.post(`/orders/${orderId}/cancel`)
}

export function deleteOrderRecordApi(orderId) {
  return http.delete(`/orders/${orderId}/delete`)
}

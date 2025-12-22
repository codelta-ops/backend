import http from './http'

export function uploadImageApi(file) {
  const formData = new FormData()
  formData.append('file', file)
  return http.post('/file/upload/image', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

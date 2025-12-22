// src/api/settings.js
import http from './http'

export function updateSettingsApi(notifications) {
  return http.put('/settings/update', null, {
    params: {
      notifications: notifications
    }
  })
}
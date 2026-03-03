import axiosInstance from '../axiosInstance'
import type { NotificationResponse } from '../../types/notification'

export const notificationApi = {
  list: (page = 0, size = 20) =>
    axiosInstance.get<NotificationResponse>('/v1/notifications', { params: { page, size } }),

  readOne: (notificationId: number) =>
    axiosInstance.patch(`/v1/notifications/${notificationId}/read`),

  readAll: () =>
    axiosInstance.patch('/v1/notifications/read-all'),
}

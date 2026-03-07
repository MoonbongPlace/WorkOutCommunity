import axiosInstance from '../axiosInstance'
import type { NotificationResponse, NotificationUnreadCountResponse } from '../../types/notification'

export const notificationApi = {
  list: (page = 0, size = 20) =>
    axiosInstance.get<NotificationResponse>('/v1/notifications', { params: { page, size } }),

  unreadCount: () =>
    axiosInstance.get<NotificationUnreadCountResponse>('/v1/notifications/unread-count'),

  readOne: (notificationId: number) =>
    axiosInstance.patch(`/v1/notifications/${notificationId}/read`),

  readAll: () =>
    axiosInstance.patch('/v1/notifications/read-all'),
}

// GET /api/v1/notifications
export type NotificationType = 'COMMENT' | 'SYSTEM_NOTICE' | 'BROADCAST' | 'ADMIN_ACTION'

export interface NotificationItem {
  id: number
  postId: number | null
  type: NotificationType
  message: string
  linkUrl: string | null
  isRead: boolean
  createdAt: string
  readAt: string | null
}

export interface PageInfo {
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

export interface MyNotificationsResult {
  notifications: NotificationItem[]
  pageInfo: PageInfo
  unreadCount: number
}

export interface NotificationResponse {
  code: string
  message: string
  timestamp: string
  myNotificationsResult: MyNotificationsResult
}

// GET /api/v1/notifications/unread-count
export interface NotificationUnreadCountResponse {
  code: string
  message: string
  timestamp: string
  unReadCountResult: { unreadCount: number }
}

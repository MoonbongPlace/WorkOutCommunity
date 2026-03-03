import { useEffect, useState } from 'react'
import PageHeader from '../components/ui/PageHeader'
import StateBlock from '../components/ui/StateBlock'
import { notificationApi } from '../api/endpoints/notification'
import type { NotificationItem } from '../types/notification'

type Status = 'loading' | 'success' | 'empty' | 'error'

export default function NotificationsPage() {
  const [status, setStatus] = useState<Status>('loading')
  const [items,  setItems]  = useState<NotificationItem[]>([])

  async function load() {
    setStatus('loading')
    try {
      const { data } = await notificationApi.list()
      const list = data.myNotificationsResult.notifications
      setItems(list)
      setStatus(list.length === 0 ? 'empty' : 'success')
    } catch {
      setStatus('error')
    }
  }

  async function handleReadOne(id: number) {
    try {
      await notificationApi.readOne(id)
      setItems((prev) =>
        prev.map((item) => item.id === id ? { ...item, isRead: true } : item)
      )
    } catch {
      // 읽음 처리 실패는 조용히 무시
    }
  }

  async function handleReadAll() {
    try {
      await notificationApi.readAll()
      setItems((prev) => prev.map((item) => ({ ...item, isRead: true })))
    } catch {
      // 조용히 무시
    }
  }

  useEffect(() => { void load() }, [])

  const unreadCount = items.filter((i) => !i.isRead).length

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <PageHeader title="알림" />
        {status === 'success' && unreadCount > 0 && (
          <button
            className="btn-secondary text-sm py-1"
            onClick={() => void handleReadAll()}
          >
            전체 읽음
          </button>
        )}
      </div>

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {status === 'empty'   && <StateBlock type="empty" message="알림이 없습니다." />}
      {status === 'success' && (
        <ul className="flex flex-col gap-2">
          {items.map((item) => (
            <li key={item.id}>
              <div
                className={`bg-white border rounded-lg shadow-sm p-5 transition-colors ${
                  item.isRead
                    ? 'border-[#E8E7D1] opacity-60'
                    : 'border-[#A6A66A] cursor-pointer'
                }`}
                onClick={!item.isRead ? () => void handleReadOne(item.id) : undefined}
              >
                <div className="flex items-start justify-between gap-4">
                  <p className="text-sm text-gray-800">{item.message}</p>
                  <span className="text-xs text-gray-400 shrink-0">
                    {item.createdAt.slice(0, 10)}
                  </span>
                </div>
                {!item.isRead && (
                  <span className="tag mt-2">새 알림</span>
                )}
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

import { useEffect, useRef, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { notificationApi } from '../../api/endpoints/notification'
import type { NotificationItem } from '../../types/notification'

type Status = 'idle' | 'loading' | 'success' | 'empty' | 'error'

export default function NotificationWidget() {
  const [open, setOpen] = useState(false)
  const [status, setStatus] = useState<Status>('idle')
  const [items, setItems] = useState<NotificationItem[]>([])
  const [unreadCount, setUnreadCount] = useState(0)
  const panelRef = useRef<HTMLDivElement>(null)
  const navigate = useNavigate()
  const location = useLocation()

  async function fetchUnreadCount() {
    try {
      const { data } = await notificationApi.unreadCount()
      setUnreadCount(data.unReadCountResult.unreadCount)
    } catch {
      // 조용히 무시
    }
  }

  async function load() {
    setStatus('loading')
    try {
      const { data } = await notificationApi.list()
      const result = data.myNotificationsResult
      setItems(result.notifications)
      setUnreadCount(result.unreadCount)
      setStatus(result.notifications.length === 0 ? 'empty' : 'success')
    } catch {
      setStatus('error')
    }
  }

  async function handleReadOne(item: NotificationItem) {
    if (!item.isRead) {
      try {
        await notificationApi.readOne(item.id)
        setItems((prev) =>
          prev.map((n) => (n.id === item.id ? { ...n, isRead: true } : n))
        )
        setUnreadCount((prev) => Math.max(0, prev - 1))
      } catch {
        // 조용히 무시
      }
    }
    if (item.linkUrl) {
      setOpen(false)
      navigate(item.linkUrl)
    }
  }

  async function handleReadAll() {
    try {
      await notificationApi.readAll()
      setItems((prev) => prev.map((n) => ({ ...n, isRead: true })))
      setUnreadCount(0)
    } catch {
      // 조용히 무시
    }
  }

  // 마운트 및 페이지 이동 시 unread count 로드
  useEffect(() => {
    void fetchUnreadCount()
  }, [location.pathname])

  // 열릴 때 데이터 로드
  useEffect(() => {
    if (open) void load()
  }, [open])

  // 외부 클릭 시 닫기
  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (panelRef.current && !panelRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    if (open) document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [open])

  return (
    <div className="relative" ref={panelRef}>
      {/* 벨 아이콘 버튼 */}
      <button
        onClick={() => setOpen((v) => !v)}
        className="relative p-1.5 rounded hover:bg-[#E8E7D1] transition-colors"
        aria-label="알림"
      >
        <svg
          className="w-5 h-5 text-gray-600"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M15 17h5l-1.405-1.405A2.032 2.032 0 0118 14.158V11a6.002 6.002 0 00-4-5.659V5a2 2 0 10-4 0v.341C7.67 6.165 6 8.388 6 11v3.159c0 .538-.214 1.055-.595 1.436L4 17h5m6 0v1a3 3 0 11-6 0v-1m6 0H9"
          />
        </svg>
        {unreadCount > 0 && (
          <span className="absolute -top-0.5 -right-0.5 min-w-[16px] h-4 px-0.5 rounded-full bg-red-500 text-white text-[10px] font-bold flex items-center justify-center leading-none">
            {unreadCount > 99 ? '99+' : unreadCount}
          </span>
        )}
      </button>

      {/* 드롭다운 패널 */}
      {open && (
        <div className="absolute left-0 top-full mt-2 w-80 bg-white border border-[#E8E7D1] rounded-xl shadow-lg z-50 flex flex-col overflow-hidden">
          {/* 패널 헤더 */}
          <div className="flex items-center justify-between px-4 py-3 border-b border-[#E8E7D1]">
            <span className="text-sm font-semibold text-gray-800">알림</span>
            {status === 'success' && unreadCount > 0 && (
              <button
                className="text-xs text-[#7A7F3A] hover:underline font-medium"
                onClick={() => void handleReadAll()}
              >
                전체 읽음
              </button>
            )}
          </div>

          {/* 알림 목록 */}
          <div className="max-h-80 overflow-y-auto">
            {status === 'loading' && (
              <p className="text-sm text-gray-400 text-center py-8">불러오는 중...</p>
            )}
            {status === 'error' && (
              <p className="text-sm text-gray-400 text-center py-8">
                불러오기 실패.{' '}
                <button
                  className="text-[#7A7F3A] underline"
                  onClick={() => void load()}
                >
                  재시도
                </button>
              </p>
            )}
            {status === 'empty' && (
              <p className="text-sm text-gray-400 text-center py-8">알림이 없습니다.</p>
            )}
            {status === 'success' && (
              <ul className="divide-y divide-[#F0EFD8]">
                {items.map((item) => (
                  <li key={item.id}>
                    <div
                      className={`px-4 py-3 transition-colors ${
                        item.isRead
                          ? 'opacity-50'
                          : item.linkUrl
                          ? 'cursor-pointer hover:bg-[#F8F8F0]'
                          : 'cursor-pointer hover:bg-[#F8F8F0]'
                      }`}
                      onClick={() => void handleReadOne(item)}
                    >
                      <div className="flex items-start gap-2">
                        {!item.isRead && (
                          <span className="mt-1.5 w-2 h-2 rounded-full bg-[#7A7F3A] shrink-0" />
                        )}
                        {item.isRead && <span className="mt-1.5 w-2 h-2 shrink-0" />}
                        <div className="flex-1 min-w-0">
                          <p className="text-sm text-gray-800 leading-snug">{item.message}</p>
                          <p className="text-xs text-gray-400 mt-0.5">{item.createdAt.slice(0, 10)}</p>
                        </div>
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </div>
      )}
    </div>
  )
}

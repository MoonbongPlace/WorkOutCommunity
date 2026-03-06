import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { memberApi } from '../api/endpoints/member'
import { authApi } from '../api/endpoints/auth'
import { useAuth } from '../context/AuthContext'
import type { DetailMemberResult } from '../types/member'
import StateBlock from '../components/ui/StateBlock'
import ProfileHeader from '../components/mypage/ProfileHeader'
import MyPageTabs from '../components/mypage/MyPageTabs'
import PostList from '../components/mypage/PostList'
import WorkoutLogList from '../components/mypage/WorkoutLogList'

type Status    = 'loading' | 'success' | 'error'
type ActiveTab = 'posts' | 'workout'

export default function MePage() {
  const navigate      = useNavigate()
  const { logout }    = useAuth()
  const [status, setStatus]         = useState<Status>('loading')
  const [data,   setData]           = useState<DetailMemberResult | null>(null)
  const [activeTab, setActiveTab]   = useState<ActiveTab>('posts')
  const [loggingOut, setLoggingOut] = useState(false)

  async function load() {
    setStatus('loading')
    try {
      const { data: res } = await memberApi.me()
      setData(res.detailMember)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  async function handleLogout() {
    setLoggingOut(true)
    try {
      await authApi.logout()
    } finally {
      logout()
      navigate('/login')
    }
  }

  useEffect(() => { void load() }, [])

  return (
    <div className="max-w-2xl mx-auto pb-10">
      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}

      {status === 'success' && data && (
        <>
          {/* 프로필 영역 */}
          <div className="bg-white border border-[#E8E7D1] rounded-2xl shadow-sm mb-6">
            <ProfileHeader member={data} onUpdated={() => void load()} />
          </div>

          {/* 탭 + 콘텐츠 */}
          <div className="bg-white border border-[#E8E7D1] rounded-2xl shadow-sm mb-6">
            <MyPageTabs activeTab={activeTab} onTabChange={setActiveTab} />
            <div className="p-4">
              {activeTab === 'posts'   && <PostList />}
              {activeTab === 'workout' && <WorkoutLogList />}
            </div>
          </div>

          {/* 로그아웃 */}
          <button
            className="btn-secondary w-full"
            onClick={() => void handleLogout()}
            disabled={loggingOut}
          >
            {loggingOut ? '로그아웃 중...' : '로그아웃'}
          </button>
        </>
      )}
    </div>
  )
}

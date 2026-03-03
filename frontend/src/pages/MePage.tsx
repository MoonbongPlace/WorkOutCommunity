import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/ui/PageHeader'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { memberApi } from '../api/endpoints/member'
import { authApi } from '../api/endpoints/auth'
import { useAuth } from '../context/AuthContext'
import type { DetailMemberResult } from '../types/member'

type Status = 'loading' | 'success' | 'error'

export default function MePage() {
  const navigate      = useNavigate()
  const { logout }    = useAuth()
  const [status, setStatus] = useState<Status>('loading')
  const [data,   setData]   = useState<DetailMemberResult | null>(null)
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
    <div>
      <PageHeader title="내 정보" />

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {status === 'success' && data && (
        <div className="flex flex-col gap-4 max-w-md">
          <Card>
            <dl className="flex flex-col gap-3 text-sm">
              <Row label="이메일"    value={data.email} />
              <Row label="닉네임"    value={data.memberName} />
              {data.name  && <Row label="이름"   value={data.name} />}
              {data.age   && <Row label="나이"   value={String(data.age)} />}
              {data.sex   && <Row label="성별"   value={data.sex} />}
              <Row label="권한"      value={data.role} />
              <Row label="상태"      value={data.status} />
            </dl>
          </Card>

          <button
            className="btn-secondary w-full"
            onClick={() => void handleLogout()}
            disabled={loggingOut}
          >
            {loggingOut ? '로그아웃 중...' : '로그아웃'}
          </button>
        </div>
      )}
    </div>
  )
}

function Row({ label, value }: { label: string; value: string }) {
  return (
    <div className="flex gap-4 items-center border-b border-[#E8E7D1] pb-2 last:border-0 last:pb-0">
      <dt className="w-20 shrink-0 text-gray-500">{label}</dt>
      <dd className="font-medium text-gray-800">{value}</dd>
    </div>
  )
}

import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { workoutLogApi } from '../../api/endpoints/workoutLog'
import type { WorkoutLogSummary } from '../../types/workoutLog'
import StateBlock from '../ui/StateBlock'

type Status = 'loading' | 'success' | 'error'

export default function WorkoutLogList() {
  const navigate = useNavigate()
  const [status, setStatus] = useState<Status>('loading')
  const [logs,   setLogs]   = useState<WorkoutLogSummary[]>([])

  async function load() {
    setStatus('loading')
    try {
      const { data: res } = await workoutLogApi.list()
      const sorted = [...res.workOutLogListResult.workOutLogList].sort(
        (a, b) => new Date(b.logDate).getTime() - new Date(a.logDate).getTime()
      )
      setLogs(sorted)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => { void load() }, [])

  if (status === 'loading') return <StateBlock type="loading" />
  if (status === 'error')   return <StateBlock type="error" onRetry={() => void load()} />

  if (logs.length === 0) {
    return (
      <StateBlock type="empty" message="운동 기록이 없습니다." />
    )
  }

  return (
    <div className="flex flex-col gap-3 pt-2">
      {logs.map((log) => (
        <div
          key={log.id}
          onClick={() => navigate(`/workout-logs/${log.id}`)}
          className="flex items-center justify-between bg-white border border-[#E8E7D1] rounded-lg p-4 cursor-pointer hover:shadow-md hover:border-[#A6A66A] transition-all"
        >
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-[#F0F0E0] flex items-center justify-center text-[#7A7F3A] text-lg">
              🏋️
            </div>
            <div>
              <p className="font-semibold text-gray-800">
                {new Date(log.logDate).toLocaleDateString('ko-KR', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </p>
              <p className="text-xs text-gray-400 mt-0.5">
                기록일: {new Date(log.createdAt).toLocaleDateString('ko-KR')}
              </p>
            </div>
          </div>
          <span className="text-gray-300 text-lg">›</span>
        </div>
      ))}
    </div>
  )
}

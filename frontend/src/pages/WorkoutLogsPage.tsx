import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/ui/PageHeader'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { workoutLogApi } from '../api/endpoints/workoutLog'
import type { WorkoutLogSummary } from '../types/workoutLog'

type Status = 'loading' | 'success' | 'empty' | 'error'

export default function WorkoutLogsPage() {
  const navigate = useNavigate()
  const [status, setStatus] = useState<Status>('loading')
  const [logs,   setLogs]   = useState<WorkoutLogSummary[]>([])

  async function load() {
    setStatus('loading')
    try {
      const { data } = await workoutLogApi.list()
      const items = data.workOutLogListResult.workOutLogList
      setLogs(items)
      setStatus(items.length === 0 ? 'empty' : 'success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => { void load() }, [])

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <PageHeader title="운동일지" description="나의 운동 기록" />
        <button
          className="btn-primary text-sm py-1.5"
          onClick={() => navigate('/workout-logs/new')}
        >
          + 새 일지
        </button>
      </div>

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {status === 'empty'   && <StateBlock type="empty" message="운동일지가 없습니다." />}
      {status === 'success' && (
        <ul className="flex flex-col gap-3">
          {logs.map((log) => (
            <li key={log.id}>
              <button
                className="w-full text-left"
                onClick={() => navigate(`/workout-logs/${log.id}`)}
              >
                <Card className="hover:border-[#A6A66A] transition-colors cursor-pointer">
                  <h2 className="font-semibold text-gray-800 mb-1">{log.logDate}</h2>
                  <div className="text-xs text-gray-500">
                    기록일: {log.createdAt.slice(0, 10)}
                  </div>
                </Card>
              </button>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

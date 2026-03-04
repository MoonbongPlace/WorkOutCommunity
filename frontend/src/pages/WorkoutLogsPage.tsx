import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/ui/PageHeader'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { workoutLogApi } from '../api/endpoints/workoutLog'
import WorkoutLogCalendar from '../components/workout/WorkoutLogCalendar'

type Status = 'loading' | 'success' | 'empty' | 'error'

export default function WorkoutLogsPage() {
  const navigate = useNavigate()
  const [status, setStatus] = useState<Status>('loading')
  const [logMap, setLogMap] = useState<Map<string, number>>(new Map())

  async function load() {
    setStatus('loading')
    try {
      const { data } = await workoutLogApi.list()
      const items = data.workOutLogListResult.workOutLogList
      setLogMap(new Map(items.map(l => [l.logDate, l.id])))
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
      {(status === 'success' || status === 'empty') && (
        <Card>
          <WorkoutLogCalendar logMap={logMap} />
        </Card>
      )}
    </div>
  )
}

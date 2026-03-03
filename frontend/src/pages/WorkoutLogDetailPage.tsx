import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { workoutLogApi } from '../api/endpoints/workoutLog'
import type { WorkOutLogDetailResult } from '../types/workoutLog'

export default function WorkoutLogDetailPage() {
  const { logId } = useParams<{ logId: string }>()
  const navigate  = useNavigate()
  const [status, setStatus] = useState<'loading' | 'success' | 'error'>('loading')
  const [log,    setLog]    = useState<WorkOutLogDetailResult | null>(null)

  async function load() {
    if (!logId) return
    setStatus('loading')
    try {
      const { data } = await workoutLogApi.detail(Number(logId))
      setLog(data.workOutLogDetailResult)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  async function handleDelete() {
    if (!logId || !confirm('운동일지를 삭제하시겠습니까?')) return
    try {
      await workoutLogApi.remove(Number(logId))
      navigate('/workout-logs')
    } catch {
      alert('삭제에 실패했습니다.')
    }
  }

  useEffect(() => { void load() }, [logId])

  return (
    <div>
      <button
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-[#7A7F3A] mb-4 transition-colors"
        onClick={() => navigate(-1)}
      >
        ← 목록으로
      </button>

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {status === 'success' && log && (
        <div className="flex flex-col gap-4">
          <Card>
            <div className="flex items-start justify-between mb-4">
              <div>
                <h1 className="text-xl font-bold text-[#7A7F3A]">{log.logDate}</h1>
                <p className="text-xs text-gray-400 mt-0.5">기록일: {log.createdAt.slice(0, 10)}</p>
              </div>
              <button
                className="text-xs text-red-400 hover:text-red-600 transition-colors"
                onClick={() => void handleDelete()}
              >
                삭제
              </button>
            </div>

            {log.list.length === 0 ? (
              <p className="text-sm text-gray-400">운동 항목이 없습니다.</p>
            ) : (
              <ul className="flex flex-col gap-3">
                {log.list.map((item) => (
                  <li
                    key={item.id}
                    className="p-3 bg-[#E8E7D1] rounded-lg"
                  >
                    <div className="flex items-center justify-between mb-1">
                      <span className="font-semibold text-sm text-gray-800">{item.exerciseName}</span>
                      <span className="text-xs text-gray-500">순서 {item.orderSeq}</span>
                    </div>
                    <div className="flex flex-wrap gap-2 text-xs text-gray-600">
                      <span>{item.plannedSets}세트</span>
                      <span>{item.plannedReps}</span>
                      {item.plannedRpe     && <span>RPE {item.plannedRpe}</span>}
                      {item.plannedRestSec && <span>휴식 {item.plannedRestSec}초</span>}
                    </div>
                    {item.notes && (
                      <p className="text-xs text-gray-500 mt-1">{item.notes}</p>
                    )}
                  </li>
                ))}
              </ul>
            )}
          </Card>
        </div>
      )}
    </div>
  )
}

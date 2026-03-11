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
            {/* 헤더 */}
            <div className="flex items-start justify-between mb-5">
              <div>
                <p className="text-xs font-bold text-[#7A7F3A] uppercase tracking-wider mb-1">운동일지</p>
                <h1 className="text-xl font-bold text-gray-900">{log.logDate}</h1>
                <p className="text-xs text-gray-400 mt-0.5">기록일: {log.createdAt.slice(0, 10)}</p>
              </div>
              <button
                className="text-xs text-red-400 hover:text-red-600 transition-colors px-2 py-1 rounded hover:bg-red-50"
                onClick={() => void handleDelete()}
              >
                삭제
              </button>
            </div>

            {/* 종목 목록 */}
            {log.list.length === 0 ? (
              <p className="text-sm text-gray-400">운동 항목이 없습니다.</p>
            ) : (
              <ul className="flex flex-col gap-2">
                {log.list.map((item) => (
                  <li
                    key={item.id}
                    className="flex items-center justify-between bg-[#F7F7F0] border border-[#E8E7D1] rounded-lg px-4 py-3"
                  >
                    <div className="flex-1 min-w-0 mr-3">
                      <div className="flex items-center gap-2 mb-1">
                        <span className="font-semibold text-sm text-gray-800 truncate">{item.exerciseName}</span>
                        <span className="text-[11px] text-gray-400 flex-shrink-0">순서 {item.orderSeq}</span>
                      </div>
                      {item.notes && (
                        <p className="text-xs text-gray-500">{item.notes}</p>
                      )}
                    </div>

                    {/* 오른쪽 뱃지 그룹 */}
                    <div className="flex items-center gap-1.5 flex-shrink-0 flex-wrap justify-end">
                      {/* 세트×반복 - 랜딩 페이지와 동일한 조합 형식 */}
                      <span className="text-xs text-gray-500">
                        {item.plannedSets}×{item.plannedReps}
                      </span>
                      {item.plannedRpe != null && (
                        <span className="text-xs font-bold bg-[#E8E7D1] text-[#7A7F3A] px-1.5 py-0.5 rounded">
                          RPE {item.plannedRpe}
                        </span>
                      )}
                      {item.plannedRestSec != null && (
                        <span className="text-xs text-gray-500 bg-white border border-gray-200 px-2 py-0.5 rounded">
                          휴식 {item.plannedRestSec}초
                        </span>
                      )}
                    </div>
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

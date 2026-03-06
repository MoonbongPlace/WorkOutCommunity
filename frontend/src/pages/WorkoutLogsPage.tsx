import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import PageHeader from '../components/ui/PageHeader'
import Card from '../components/ui/Card'
import StateBlock from '../components/ui/StateBlock'
import { workoutLogApi } from '../api/endpoints/workoutLog'
import WorkoutLogCalendar from '../components/workout/WorkoutLogCalendar'
import WorkoutLogNewModal from '../components/workout/WorkoutLogNewModal'
import type { WorkoutLogItem } from '../types/workoutLog'

type Status = 'loading' | 'success' | 'empty' | 'error'

export default function WorkoutLogsPage() {
  const navigate = useNavigate()
  const [status, setStatus] = useState<Status>('loading')
  const [logMap, setLogMap] = useState<Map<string, number>>(new Map())
  const [selectedDate, setSelectedDate] = useState<string | null>(null)
  const [selectedItems, setSelectedItems] = useState<WorkoutLogItem[]>([])
  const [showModal, setShowModal] = useState(false)

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

  async function handleDateSelect(dateKey: string) {
    setSelectedDate(dateKey)
    const logId = logMap.get(dateKey)
    if (logId === undefined) {
      setSelectedItems([])
      return
    }
    try {
      const { data } = await workoutLogApi.detail(logId)
      setSelectedItems(data.workOutLogDetailResult.list)
    } catch {
      setSelectedItems([])
    }
  }

  useEffect(() => { void load() }, [])

  return (
    <div>
      <div className="flex items-center justify-between mb-4">
        <PageHeader title="운동일지" description="나의 운동 기록" />
        <button
          className="btn-primary text-sm py-1.5"
          onClick={() => setShowModal(true)}
        >
          + 새 일지
        </button>
      </div>

      {showModal && (
        <WorkoutLogNewModal
          onClose={() => setShowModal(false)}
          onSuccess={() => { setShowModal(false); void load() }}
          logMap={logMap}
        />
      )}

      {status === 'loading' && <StateBlock type="loading" />}
      {status === 'error'   && <StateBlock type="error" onRetry={() => void load()} />}
      {(status === 'success' || status === 'empty') && (
        <Card>
          <WorkoutLogCalendar
            logMap={logMap}
            selectedDate={selectedDate}
            onDateSelect={handleDateSelect}
          />

          {selectedDate && (() => {
            const label = new Date(selectedDate).toLocaleDateString('ko-KR', { month: 'long', day: 'numeric' })
            return (
              <div className="mt-4 pt-4 border-t border-[#E8E7D1]">
                {selectedItems.length > 0 ? (
                  <div
                    role="button"
                    tabIndex={0}
                    onClick={() => navigate(`/workout-logs/${selectedItems[0].logId}`)}
                    onKeyDown={e => e.key === 'Enter' && navigate(`/workout-logs/${selectedItems[0].logId}`)}
                    className="rounded-xl border border-[#E8E7D1] px-4 py-3 cursor-pointer hover:border-[#A6A66A] hover:bg-[#FAFAF6] transition-all duration-150 group"
                  >
                    <div className="flex items-center justify-between mb-2.5">
                      <span className="text-sm font-semibold tracking-wider text-[#A6A66A] uppercase">
                        {label} 운동 기록
                      </span>
                      <span className="text-[#C8C8A0] group-hover:text-[#7A7F3A] transition-colors text-base leading-none">›</span>
                    </div>
                    <ul className="space-y-2.5">
                      {selectedItems.map(item => (
                        <li key={item.id} className="flex items-center gap-2.5">
                          <span className="w-2 h-2 rounded-full bg-[#A6A66A] flex-shrink-0 group-hover:bg-[#7A7F3A] transition-colors" />
                          <span className="text-base font-medium text-gray-700 group-hover:text-[#4a4f22] transition-colors">
                            {item.exerciseName}
                          </span>
                        </li>
                      ))}
                    </ul>
                  </div>
                ) : (
                  <p className="text-sm text-gray-400 text-center py-2">
                    운동 일지가 존재하지 않습니다.
                  </p>
                )}
              </div>
            )
          })()}
        </Card>
      )}
    </div>
  )
}

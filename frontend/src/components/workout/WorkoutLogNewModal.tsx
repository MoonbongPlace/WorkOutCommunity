import { useState, useEffect } from 'react'
import axiosInstance from '../../api/axiosInstance'
import { exerciseApi } from '../../api/endpoints/exercise'
import ExercisePicker from './ExercisePicker'
import StateBlock from '../ui/StateBlock'
import type { ExerciseSummary } from '../../types/exercise'

interface ExerciseItem {
  exerciseId: string
  exerciseName: string
  plannedSets: string
  plannedReps: string
  plannedRpe: string
  plannedRestSec: string
  notes: string
}

const emptyItem = (): ExerciseItem => ({
  exerciseId: '',
  exerciseName: '',
  plannedSets: '3',
  plannedReps: '10',
  plannedRpe: '',
  plannedRestSec: '',
  notes: '',
})

// plannedRestSec(총 초) ↔ 분·초 변환 헬퍼
function toMinSec(totalSec: string): [number, number] {
  const n = totalSec ? Number(totalSec) : 0
  return [Math.floor(n / 60), n % 60]
}

function fromMinSec(min: number, sec: number): string {
  const total = min * 60 + sec
  return total === 0 ? '' : String(total)
}

interface Props {
  onClose: () => void
  onSuccess: () => void
  logMap: Map<string, number>
}

export default function WorkoutLogNewModal({ onClose, onSuccess, logMap }: Props) {
  const today = new Date().toISOString().slice(0, 10)

  const [logDate,   setLogDate]   = useState(today)
  const [items,     setItems]     = useState<ExerciseItem[]>([emptyItem()])
  const [saving,    setSaving]    = useState(false)
  const [error,     setError]     = useState<string | null>(null)
  const [exercises, setExercises] = useState<ExerciseSummary[]>([])
  const [exLoading, setExLoading] = useState(true)

  useEffect(() => {
    document.body.style.overflow = 'hidden'
    return () => { document.body.style.overflow = '' }
  }, [])

  useEffect(() => {
    exerciseApi.list()
      .then(({ data }) => setExercises(data.exercises))
      .catch(() => {})
      .finally(() => setExLoading(false))
  }, [])

  function updateItem(idx: number, patch: Partial<ExerciseItem>) {
    setItems(prev => prev.map((item, i) => i === idx ? { ...item, ...patch } : item))
  }

  function addItem() {
    setItems(prev => [...prev, emptyItem()])
  }

  function removeItem(idx: number) {
    setItems(prev => prev.filter((_, i) => i !== idx))
  }

  async function handleSubmit() {
    setError(null)
    for (const item of items) {
      if (!item.exerciseId || !item.plannedSets || !item.plannedReps) {
        setError('모든 운동 항목의 운동, 세트 수, 반복 수는 필수입니다.')
        return
      }
    }
    setSaving(true)
    try {
      await axiosInstance.post('/v1/workout-logs', {
        logDate,
        items: items.map(item => ({
          exerciseId:     Number(item.exerciseId),
          plannedSets:    Number(item.plannedSets),
          plannedReps:    item.plannedReps,
          plannedRpe:     item.plannedRpe     ? Number(item.plannedRpe)     : null,
          plannedRestSec: item.plannedRestSec ? Number(item.plannedRestSec) : null,
          notes:          item.notes || null,
        })),
      })
      onSuccess()
    } catch {
      setError('운동일지 생성에 실패했습니다. 다시 시도해주세요.')
    } finally {
      setSaving(false)
    }
  }

  const selectBase = 'w-full border border-gray-200 rounded-lg px-3 py-2.5 text-lg font-medium text-gray-700 bg-white focus:outline-none focus:ring-2 focus:ring-[#A6A66A] focus:border-transparent transition appearance-none cursor-pointer'

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      {/* 백드롭 */}
      <div
        className="absolute inset-0 bg-black/40 backdrop-blur-sm"
        onClick={onClose}
      />

      {/* 패널 — 42rem × 1.2 = 50rem */}
      <div className="relative bg-white rounded-t-2xl sm:rounded-2xl w-full sm:max-w-[50rem] max-h-[90vh] flex flex-col shadow-xl">

        {/* 헤더 */}
        <div className="relative flex items-center justify-center px-6 py-5 border-b border-[#E8E7D1]">
          <h2 className="text-2xl font-bold text-gray-800 tracking-tight">운동일지 작성</h2>
          <button
            className="absolute right-6 w-10 h-10 flex items-center justify-center rounded-full text-gray-400 hover:bg-[#E8E7D1] hover:text-[#7A7F3A] transition-colors text-2xl leading-none"
            onClick={onClose}
            aria-label="닫기"
          >
            ×
          </button>
        </div>

        {/* 폼 영역 */}
        <div className="flex-1 overflow-y-auto scrollbar-hide px-6 py-6 flex flex-col gap-6">

          {/* 날짜 */}
          <div>
            <label className="block text-lg font-semibold text-gray-500 mb-2">운동 날짜</label>
            <input
              type="date"
              className="input-base text-lg"
              value={logDate}
              onChange={e => setLogDate(e.target.value)}
            />
            {logMap.has(logDate) && (
              <p className="mt-2 text-base text-amber-600 font-medium">
                해당 날짜에 운동일지가 존재합니다. 운동일지는 하루에 1개 생성 가능합니다.
              </p>
            )}
          </div>

          {/* 운동 목록 */}
          {exLoading ? (
            <StateBlock type="loading" />
          ) : (
            items.map((item, idx) => {
              const [restMin, restSec] = toMinSec(item.plannedRestSec)
              return (
                <div key={idx} className="rounded-xl border border-[#E8E7D1] px-5 py-6 flex flex-col gap-5">

                  {/* 운동 헤더 */}
                  <div className="flex items-center justify-between">
                    <span className="text-xl font-semibold text-[#7A7F3A]">운동 {idx + 1}</span>
                    {items.length > 1 && (
                      <button
                        className="text-lg text-red-400 hover:text-red-600 transition-colors"
                        onClick={() => removeItem(idx)}
                      >
                        삭제
                      </button>
                    )}
                  </div>

                  {/* 운동 선택 */}
                  <div>
                    <label className="block text-lg font-semibold text-gray-500 mb-2">
                      운동 선택 <span className="text-red-400">*</span>
                    </label>
                    <ExercisePicker
                      exercises={exercises}
                      selectedId={item.exerciseId}
                      selectedName={item.exerciseName}
                      onSelect={(id, name) => updateItem(idx, { exerciseId: String(id), exerciseName: name })}
                    />
                  </div>

                  {/* 세트 수 / 반복 수 */}
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-lg font-semibold text-gray-500 mb-2">
                        세트 수 <span className="text-red-400">*</span>
                      </label>
                      <input
                        type="number"
                        min="1"
                        className="input-base text-lg"
                        value={item.plannedSets}
                        onChange={e => updateItem(idx, { plannedSets: e.target.value })}
                      />
                    </div>
                    <div>
                      <label className="block text-lg font-semibold text-gray-500 mb-2">
                        반복 수 <span className="text-red-400">*</span>
                      </label>
                      <input
                        type="text"
                        className="input-base text-lg"
                        placeholder="예: 10 또는 8-12"
                        value={item.plannedReps}
                        onChange={e => updateItem(idx, { plannedReps: e.target.value })}
                      />
                    </div>
                  </div>

                  {/* RPE */}
                  <div>
                    <label className="block text-lg font-semibold text-gray-500 mb-2">RPE</label>
                    <div className="grid grid-cols-10 gap-1.5">
                      {[1,2,3,4,5,6,7,8,9,10].map(n => (
                        <button
                          key={n}
                          type="button"
                          onClick={() => updateItem(idx, { plannedRpe: item.plannedRpe === String(n) ? '' : String(n) })}
                          className={`h-11 rounded-lg text-lg font-semibold transition-colors ${
                            item.plannedRpe === String(n)
                              ? 'bg-[#7A7F3A] text-white'
                              : 'bg-[#F5F5EC] text-gray-600 hover:bg-[#E8E7D1] hover:text-[#7A7F3A]'
                          }`}
                        >
                          {n}
                        </button>
                      ))}
                    </div>
                  </div>

                  {/* 휴식 시간 — 분:초 선택 */}
                  <div>
                    <label className="block text-lg font-semibold text-gray-500 mb-2">휴식 시간</label>
                    <div className="flex items-center gap-3">
                      {/* 분 선택 */}
                      <div className="flex-1">
                        <select
                          className={selectBase}
                          value={restMin}
                          onChange={e => updateItem(idx, { plannedRestSec: fromMinSec(Number(e.target.value), restSec) })}
                        >
                          {[0,1,2,3].map(m => (
                            <option key={m} value={m}>{m}분</option>
                          ))}
                        </select>
                      </div>
                      <span className="text-2xl font-light text-gray-400">:</span>
                      {/* 초 직접 입력 (0~59) */}
                      <div className="flex-1 relative">
                        <input
                          type="number"
                          min="0"
                          max="59"
                          placeholder="0"
                          className="input-base text-lg pr-10"
                          value={restSec === 0 && !item.plannedRestSec ? '' : restSec}
                          onChange={e => {
                            const v = e.target.value === '' ? 0 : Number(e.target.value)
                            updateItem(idx, { plannedRestSec: fromMinSec(restMin, Math.min(v, 59)) })
                          }}
                          onBlur={e => {
                            const v = Number(e.target.value)
                            if (v > 59) updateItem(idx, { plannedRestSec: fromMinSec(restMin, 59) })
                          }}
                        />
                        <span className="absolute right-3 top-1/2 -translate-y-1/2 text-base text-gray-400 pointer-events-none">초</span>
                      </div>
                    </div>
                  </div>

                  {/* 메모 */}
                  <div>
                    <label className="block text-lg font-semibold text-gray-500 mb-2">메모</label>
                    <input
                      type="text"
                      className="input-base text-lg"
                      placeholder="선택 사항"
                      value={item.notes}
                      onChange={e => updateItem(idx, { notes: e.target.value })}
                    />
                  </div>
                </div>
              )
            })
          )}

          {!exLoading && (
            <button className="btn-secondary w-full text-lg py-3" onClick={addItem}>
              + 운동 추가
            </button>
          )}
        </div>

        {/* 푸터 */}
        <div className="px-6 py-5 border-t border-[#E8E7D1] flex flex-col gap-2">
          {error && <p className="text-lg text-red-500 text-center">{error}</p>}
          <button
            className="btn-primary w-full text-lg py-3.5"
            onClick={() => void handleSubmit()}
            disabled={saving}
          >
            {saving ? '저장 중...' : '운동일지 저장'}
          </button>
        </div>
      </div>
    </div>
  )
}

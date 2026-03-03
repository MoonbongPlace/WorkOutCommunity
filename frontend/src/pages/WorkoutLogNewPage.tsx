import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import PageHeader from '../components/ui/PageHeader'
import axiosInstance from '../api/axiosInstance'

interface ExerciseItem {
  exerciseId: string
  plannedSets: string
  plannedReps: string
  plannedRpe: string
  plannedRestSec: string
  notes: string
}

const emptyItem = (): ExerciseItem => ({
  exerciseId: '',
  plannedSets: '3',
  plannedReps: '10',
  plannedRpe: '',
  plannedRestSec: '',
  notes: '',
})

export default function WorkoutLogNewPage() {
  const navigate = useNavigate()
  const today    = new Date().toISOString().slice(0, 10)

  const [logDate,  setLogDate]  = useState(today)
  const [items,    setItems]    = useState<ExerciseItem[]>([emptyItem()])
  const [saving,   setSaving]   = useState(false)
  const [error,    setError]    = useState<string | null>(null)

  function updateItem(idx: number, field: keyof ExerciseItem, value: string) {
    setItems((prev) =>
      prev.map((item, i) => i === idx ? { ...item, [field]: value } : item)
    )
  }

  function addItem() {
    setItems((prev) => [...prev, emptyItem()])
  }

  function removeItem(idx: number) {
    setItems((prev) => prev.filter((_, i) => i !== idx))
  }

  async function handleSubmit() {
    setError(null)
    for (const item of items) {
      if (!item.exerciseId || !item.plannedSets || !item.plannedReps) {
        setError('모든 운동 항목의 운동 ID, 세트 수, 반복 수는 필수입니다.')
        return
      }
    }

    setSaving(true)
    try {
      const body = {
        logDate,
        items: items.map((item) => ({
          exerciseId:    Number(item.exerciseId),
          plannedSets:   Number(item.plannedSets),
          plannedReps:   item.plannedReps,
          plannedRpe:    item.plannedRpe    ? Number(item.plannedRpe)    : null,
          plannedRestSec:item.plannedRestSec? Number(item.plannedRestSec): null,
          notes:         item.notes || null,
        })),
      }
      await axiosInstance.post('/v1/workout-logs', body)
      navigate('/workout-logs')
    } catch {
      setError('운동일지 생성에 실패했습니다. 다시 시도해주세요.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <button
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-[#7A7F3A] mb-4 transition-colors"
        onClick={() => navigate(-1)}
      >
        ← 뒤로
      </button>

      <PageHeader title="운동일지 작성" />

      <div className="flex flex-col gap-4 max-w-xl">
        {/* 날짜 */}
        <Card>
          <label className="block text-xs font-medium text-gray-600 mb-1">운동 날짜</label>
          <input
            type="date"
            className="input-base"
            value={logDate}
            onChange={(e) => setLogDate(e.target.value)}
          />
        </Card>

        {/* 운동 목록 */}
        {items.map((item, idx) => (
          <Card key={idx}>
            <div className="flex items-center justify-between mb-3">
              <span className="text-sm font-semibold text-[#7A7F3A]">운동 {idx + 1}</span>
              {items.length > 1 && (
                <button
                  className="text-xs text-red-400 hover:text-red-600 transition-colors"
                  onClick={() => removeItem(idx)}
                >
                  삭제
                </button>
              )}
            </div>

            <div className="flex flex-col gap-3">
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">
                  운동 ID <span className="text-red-400">*</span>
                </label>
                <input
                  type="number"
                  className="input-base"
                  placeholder="예: 1"
                  value={item.exerciseId}
                  onChange={(e) => updateItem(idx, 'exerciseId', e.target.value)}
                />
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">
                    세트 수 <span className="text-red-400">*</span>
                  </label>
                  <input
                    type="number"
                    min="1"
                    className="input-base"
                    value={item.plannedSets}
                    onChange={(e) => updateItem(idx, 'plannedSets', e.target.value)}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">
                    반복 수 <span className="text-red-400">*</span>
                  </label>
                  <input
                    type="text"
                    className="input-base"
                    placeholder="예: 10 또는 8-12"
                    value={item.plannedReps}
                    onChange={(e) => updateItem(idx, 'plannedReps', e.target.value)}
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">RPE</label>
                  <input
                    type="number"
                    min="1"
                    max="10"
                    className="input-base"
                    placeholder="1–10"
                    value={item.plannedRpe}
                    onChange={(e) => updateItem(idx, 'plannedRpe', e.target.value)}
                  />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-600 mb-1">휴식 (초)</label>
                  <input
                    type="number"
                    min="0"
                    className="input-base"
                    placeholder="예: 90"
                    value={item.plannedRestSec}
                    onChange={(e) => updateItem(idx, 'plannedRestSec', e.target.value)}
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">메모</label>
                <input
                  type="text"
                  className="input-base"
                  placeholder="선택 사항"
                  value={item.notes}
                  onChange={(e) => updateItem(idx, 'notes', e.target.value)}
                />
              </div>
            </div>
          </Card>
        ))}

        <button className="btn-secondary w-full" onClick={addItem}>
          + 운동 추가
        </button>

        {error && <p className="text-xs text-red-500">{error}</p>}

        <button
          className="btn-primary w-full"
          onClick={() => void handleSubmit()}
          disabled={saving}
        >
          {saving ? '저장 중...' : '운동일지 저장'}
        </button>
      </div>
    </div>
  )
}

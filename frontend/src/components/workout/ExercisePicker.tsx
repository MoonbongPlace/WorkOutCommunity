import { useState, useMemo } from 'react'
import type { ExerciseSummary } from '../../types/exercise'

interface Props {
  exercises: ExerciseSummary[]
  selectedId: string
  selectedName: string
  onSelect: (id: number, name: string) => void
}

export default function ExercisePicker({ exercises, selectedId, selectedName, onSelect }: Props) {
  const bodyParts = useMemo(() => {
    const parts = new Set(exercises.flatMap((e) => e.bodyParts))
    return [...parts]
  }, [exercises])

  const [activeTab, setActiveTab] = useState(bodyParts[0] ?? '')
  const [showList, setShowList] = useState(!selectedId)

  const filtered = exercises.filter((e) => e.bodyParts.includes(activeTab))

  function handleTabClick(part: string) {
    setActiveTab(part)
    setShowList(true)
  }

  function handleSelect(exercise: ExerciseSummary) {
    onSelect(exercise.id, exercise.name)
    setShowList(false)
  }

  return (
    <div className="flex flex-col gap-2">
      {/* 선택된 운동 표시 */}
      {selectedName && !showList && (
        <div className="flex items-center justify-between px-3 py-2 bg-[#E8E7D1] rounded-lg">
          <span className="text-sm font-medium text-[#7A7F3A]">{selectedName}</span>
          <span className="text-xs text-gray-500">선택됨</span>
        </div>
      )}

      {/* 부위별 탭 */}
      <div className="flex flex-wrap gap-1">
        {bodyParts.map((part) => (
          <button
            key={part}
            type="button"
            className={`text-xs px-2.5 py-1 rounded-full border transition-colors ${
              activeTab === part && showList
                ? 'bg-[#7A7F3A] text-white border-[#7A7F3A]'
                : 'bg-white text-gray-600 border-gray-200 hover:border-[#A6A66A]'
            }`}
            onClick={() => handleTabClick(part)}
          >
            {part}
          </button>
        ))}
      </div>

      {/* 운동 리스트 */}
      {showList && (
        <div className="border border-gray-200 rounded-lg max-h-44 overflow-y-auto">
          {filtered.length === 0 ? (
            <p className="text-xs text-gray-400 px-3 py-2">운동이 없습니다.</p>
          ) : (
            filtered.map((exercise) => (
              <button
                key={exercise.id}
                type="button"
                className={`w-full text-left px-3 py-2 text-sm transition-colors border-b border-gray-100 last:border-b-0 ${
                  String(exercise.id) === selectedId
                    ? 'bg-[#E8E7D1] text-[#7A7F3A] font-medium'
                    : 'hover:bg-[#E8E7D1]'
                }`}
                onClick={() => handleSelect(exercise)}
              >
                {exercise.name}
              </button>
            ))
          )}
        </div>
      )}
    </div>
  )
}

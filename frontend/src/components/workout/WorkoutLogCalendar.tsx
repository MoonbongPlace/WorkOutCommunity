import Calendar from 'react-calendar'
import { useNavigate } from 'react-router-dom'

interface Props {
  logMap: Map<string, number>
}

export default function WorkoutLogCalendar({ logMap }: Props) {
  const navigate = useNavigate()

  function tileContent({ date }: { date: Date }) {
    const key = date.toLocaleDateString('sv') // "YYYY-MM-DD"
    if (!logMap.has(key)) return null
    return (
      <div className="flex justify-center mt-0.5">
        <span className="w-1.5 h-1.5 rounded-full bg-[#7A7F3A]" />
      </div>
    )
  }

  function handleClickDay(date: Date) {
    const key = date.toLocaleDateString('sv')
    const id = logMap.get(key)
    if (id !== undefined) navigate(`/workout-logs/${id}`)
  }

  return (
    <Calendar
      locale="ko-KR"
      tileContent={tileContent}
      onClickDay={handleClickDay}
      className="!border-none !w-full"
    />
  )
}

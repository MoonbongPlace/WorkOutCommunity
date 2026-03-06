import Calendar from 'react-calendar'

interface Props {
  logMap: Map<string, number>
  selectedDate: string | null
  onDateSelect: (dateKey: string) => void
}

export default function WorkoutLogCalendar({ logMap, selectedDate, onDateSelect }: Props) {
  function tileContent({ date, view }: { date: Date; view: string }) {
    if (view !== 'month') return null
    const key = date.toLocaleDateString('sv')
    const hasLog = logMap.has(key)
    return (
      <div className="flex justify-center items-center mt-0.5 h-2">
        {hasLog && <span className="w-1.5 h-1.5 rounded-full bg-[#7A7F3A] tile-dot" />}
      </div>
    )
  }

  function tileClassName({ date, view }: { date: Date; view: string }) {
    if (view !== 'month') return null
    return date.toLocaleDateString('sv') === selectedDate ? 'calendar-tile--selected' : null
  }

  return (
    <Calendar
      locale="ko-KR"
      minDetail="year"
      tileContent={tileContent}
      tileClassName={tileClassName}
      onClickDay={(date) => onDateSelect(date.toLocaleDateString('sv'))}
      className="!border-none !w-full"
    />
  )
}

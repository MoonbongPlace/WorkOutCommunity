type Tab = 'posts' | 'workout'

interface Props {
  activeTab: Tab
  onTabChange: (tab: Tab) => void
}

const TABS: { key: Tab; label: string }[] = [
  { key: 'posts',   label: '작성글' },
  { key: 'workout', label: '운동일지' },
]

export default function MyPageTabs({ activeTab, onTabChange }: Props) {
  return (
    <div className="flex border-b border-[#E8E7D1]">
      {TABS.map(({ key, label }) => (
        <button
          key={key}
          onClick={() => onTabChange(key)}
          className={[
            'flex-1 py-3 text-sm font-medium transition-colors',
            activeTab === key
              ? 'border-b-2 border-[#7A7F3A] text-[#7A7F3A]'
              : 'text-gray-400 hover:text-gray-600',
          ].join(' ')}
        >
          {label}
        </button>
      ))}
    </div>
  )
}

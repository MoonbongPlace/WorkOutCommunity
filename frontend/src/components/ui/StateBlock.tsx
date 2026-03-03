interface Props {
  type: 'loading' | 'empty' | 'error'
  message?: string
  onRetry?: () => void
}

const defaults: Record<Props['type'], string> = {
  loading: '불러오는 중...',
  empty:   '데이터가 없습니다.',
  error:   '오류가 발생했습니다.',
}

export default function StateBlock({ type, message, onRetry }: Props) {
  return (
    <div className="flex flex-col items-center justify-center py-20 gap-3 text-gray-500">
      {type === 'loading' && (
        <div className="w-8 h-8 border-4 border-[#A6A66A] border-t-transparent rounded-full animate-spin" />
      )}
      <p className="text-sm">{message ?? defaults[type]}</p>
      {type === 'error' && onRetry && (
        <button onClick={onRetry} className="btn-secondary text-sm">
          다시 시도
        </button>
      )}
    </div>
  )
}

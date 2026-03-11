const FEATURES = ['커뮤니티 피드', '운동일지', 'AI 코치', '실시간 알림', '카테고리', '좋아요 · 댓글']

export default function StatsStrip() {
  return (
    <section className="py-5 bg-gradient-to-r from-[#7A7F3A] to-[#A6A66A]">
      <div className="max-w-5xl mx-auto px-4 sm:px-6">
        <div className="flex flex-wrap items-center justify-center gap-x-6 gap-y-2">
          {FEATURES.map((feat, i) => (
            <div key={feat} className="flex items-center gap-6">
              <span className="text-sm font-medium text-white/90">{feat}</span>
              {i < FEATURES.length - 1 && (
                <span className="text-white/30 text-xs">·</span>
              )}
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

import { useInView } from '../../hooks/useInView'

// WorkoutLogItem 형식에 맞는 샘플 운동일지
const WORKOUT_LOG = [
  { exerciseName: '스쿼트', plannedSets: 4, plannedReps: '10', plannedRpe: 8 },
  { exerciseName: '루마니안 데드리프트', plannedSets: 3, plannedReps: '8', plannedRpe: 7 },
  { exerciseName: '레그 컬', plannedSets: 3, plannedReps: '12', plannedRpe: 6 },
]

const FEATURES = [
  {
    id: 'community',
    title: '커뮤니티 피드',
    desc: '운동 기록과 노하우를 공유하고, 댓글·좋아요로 서로를 응원하세요.',
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" />
      </svg>
    ),
    preview: (
      <div className="space-y-2.5">
        <div className="flex items-start gap-2.5">
          <div className="w-7 h-7 rounded-full bg-[#E8E7D1] flex items-center justify-center text-xs font-bold text-[#7A7F3A] flex-shrink-0">
            이
          </div>
          <div className="flex-1">
            <p className="text-xs font-bold text-neutral-800">오늘의 풀바디 루틴 공유 💪</p>
            <p className="text-xs text-neutral-500 mt-0.5 leading-relaxed">스쿼트 4×10, 벤치 4×8, 풀업 3×Max</p>
            <div className="flex items-center gap-3 mt-1.5">
              <span className="text-xs text-[#7A7F3A] font-semibold">♥ 31</span>
              <span className="text-xs text-neutral-400">💬 8</span>
            </div>
          </div>
        </div>
        <div className="flex items-start gap-2.5 opacity-60">
          <div className="w-7 h-7 rounded-full bg-[#D4D4A8] flex items-center justify-center text-xs font-bold text-[#7A7F3A] flex-shrink-0">
            박
          </div>
          <div className="flex-1">
            <p className="text-xs font-bold text-neutral-800">러닝 5km 완주 🏃</p>
            <p className="text-xs text-neutral-500 mt-0.5">평균 페이스 5'20"</p>
          </div>
        </div>
      </div>
    ),
  },
  {
    id: 'log',
    title: '운동일지',
    desc: '종목·세트·반복수·RPE를 구조화해 기록하고, 날짜별로 추이를 추적하세요.',
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 3v11.25A2.25 2.25 0 006 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0118 16.5h-2.25m-7.5 0h7.5m-7.5 0l-1 3m8.5-3l1 3m0 0l.5 1.5m-.5-1.5h-9.5m0 0l-.5 1.5M9 11.25v1.5M12 9v3.75m3-6v6" />
      </svg>
    ),
    preview: (
      <div className="space-y-1.5">
        <div className="flex justify-between items-center mb-2">
          <p className="text-xs font-bold text-neutral-700">2026.03.11 · 하체</p>
          <span className="text-xs text-[#7A7F3A] font-semibold">3종목</span>
        </div>
        {WORKOUT_LOG.map((item) => (
          <div key={item.exerciseName} className="flex items-center justify-between rounded-lg bg-white border border-neutral-100 px-2.5 py-1.5">
            <span className="text-xs font-medium text-neutral-700 truncate mr-2">{item.exerciseName}</span>
            <div className="flex items-center gap-1.5 flex-shrink-0">
              <span className="text-xs text-neutral-400">
                {item.plannedSets}×{item.plannedReps}
              </span>
              <span className="text-xs bg-[#E8E7D1] text-[#7A7F3A] px-1.5 py-0.5 rounded font-bold">
                RPE {item.plannedRpe}
              </span>
            </div>
          </div>
        ))}
      </div>
    ),
  },
  {
    id: 'notification',
    title: '실시간 알림',
    desc: '좋아요·댓글 등 모든 상호작용을 즉시 알림으로 받아보세요.',
    icon: (
      <svg className="w-6 h-6" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0" />
      </svg>
    ),
    preview: (
      <div className="space-y-2">
        {[
          { icon: '♥', text: '김민준님이 게시글에 좋아요를 눌렀습니다.', time: '방금 전', read: false },
          { icon: '💬', text: '이지은님이 댓글을 남겼습니다.', time: '3분 전', read: false },
          { icon: '♥', text: '박준호님이 게시글에 좋아요를 눌렀습니다.', time: '8분 전', read: true },
        ].map((n, i) => (
          <div
            key={i}
            className={`flex items-start gap-2.5 rounded-lg px-2.5 py-2 ${n.read ? 'opacity-50' : 'bg-white border border-neutral-100'}`}
          >
            <span className="text-sm flex-shrink-0">{n.icon}</span>
            <div className="flex-1 min-w-0">
              <p className="text-xs text-neutral-700 leading-relaxed">{n.text}</p>
              <p className="text-xs text-neutral-400 mt-0.5">{n.time}</p>
            </div>
            {!n.read && <div className="w-2 h-2 bg-[#7A7F3A] rounded-full flex-shrink-0 mt-1" />}
          </div>
        ))}
      </div>
    ),
  },
]

export default function FeatureGrid() {
  const { ref, inView } = useInView()

  return (
    <section id="features" className="py-20">
      <div ref={ref} className={`reveal ${inView ? 'visible' : ''} mb-12`}>
        <p className="text-xs font-bold text-[#7A7F3A] uppercase tracking-widest mb-3">주요 기능</p>
        <h2 className="text-2xl md:text-3xl font-bold text-neutral-900 mb-3">
          운동의 모든 것을 한 곳에서
        </h2>
        <p className="text-neutral-500 max-w-xl">
          기록부터 공유, AI 코칭까지. 운동을 더 즐겁고 체계적으로 만들어 드립니다.
        </p>
      </div>

      <div className="grid md:grid-cols-3 gap-5">
        {FEATURES.map(({ id, icon, title, desc, preview }, i) => (
          <div
            key={id}
            className={`group reveal reveal-d${i + 1} ${inView ? 'visible' : ''} rounded-2xl border border-neutral-200 bg-white shadow-sm hover:shadow-lg hover:border-[#A6A66A]/60 p-5 flex flex-col gap-4 transition-all duration-300 hover:-translate-y-1`}
          >
            <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-[#E8E7D1] to-[#D4D4A8] flex items-center justify-center text-[#7A7F3A] group-hover:scale-110 transition-transform duration-200">
              {icon}
            </div>
            <div>
              <h3 className="text-sm font-bold text-neutral-900">{title}</h3>
              <p className="text-sm text-neutral-500 mt-1.5 leading-relaxed">{desc}</p>
            </div>
            <div className="mt-auto rounded-xl bg-neutral-50 border border-neutral-100 px-3 py-3">
              {preview}
            </div>
          </div>
        ))}
      </div>
    </section>
  )
}

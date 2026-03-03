const FEATURES = [
  {
    title: '커뮤니티 피드',
    desc: '운동 기록과 경험을 공유하고 댓글로 소통하세요.',
    preview: '스쿼트 5×5 완료!\n오늘은 집중이 특히 잘 됐네요.',
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 01.865-.501 48.172 48.172 0 003.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0012 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018z" />
      </svg>
    ),
  },
  {
    title: '운동 로그',
    desc: '세트·반복·RPE를 구조화해서 기록하고 추이를 추적하세요.',
    preview: '데드리프트 3×8 @RPE 8\n스쿼트 4×10 @RPE 7',
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M3.75 3v11.25A2.25 2.25 0 006 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0118 16.5h-2.25m-7.5 0h7.5m-7.5 0l-1 3m8.5-3l1 3m0 0l.5 1.5m-.5-1.5h-9.5m0 0l-.5 1.5M9 11.25v1.5M12 9v3.75m3-6v6" />
      </svg>
    ),
  },
  {
    title: '실시간 알림',
    desc: '좋아요, 댓글 등 상호작용을 즉시 확인하세요.',
    preview: '김철수님이 좋아요를 눌렀습니다.\n이지은님이 댓글을 남겼습니다.',
    icon: (
      <svg className="w-5 h-5" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" d="M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0" />
      </svg>
    ),
  },
]

export default function FeatureGrid() {
  return (
    <section className="py-16">
      <h2 className="text-lg font-semibold text-neutral-900 mb-2">주요 기능</h2>
      <p className="text-sm text-neutral-600 mb-8">커뮤니티 중심으로 설계된 운동 플랫폼입니다.</p>
      <div className="grid md:grid-cols-3 gap-4">
        {FEATURES.map(({ icon, title, desc, preview }) => (
          <div key={title} className="rounded-2xl border border-neutral-200 bg-white shadow-sm p-5 flex flex-col gap-3">
            <div className="w-9 h-9 rounded-xl border border-neutral-200 flex items-center justify-center text-neutral-600">
              {icon}
            </div>
            <div>
              <h3 className="text-sm font-semibold text-neutral-900">{title}</h3>
              <p className="text-sm text-neutral-600 mt-1 leading-relaxed">{desc}</p>
            </div>
            <div className="mt-auto rounded-xl bg-neutral-50 border border-neutral-100 px-3 py-2">
              {preview.split('\n').map((line, i) => (
                <p key={i} className="text-xs text-neutral-500 leading-relaxed">{line}</p>
              ))}
            </div>
          </div>
        ))}
      </div>
    </section>
  )
}

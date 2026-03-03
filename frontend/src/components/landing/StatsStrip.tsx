const STATS = [
  { value: 'JWT', label: '인증', desc: 'Access + Refresh 토큰 기반 보안 인증 구조' },
  { value: 'PostgreSQL', label: '마이그레이션', desc: 'H2 → PostgreSQL 도메인 기반 스키마 재설계' },
  { value: 'REST API', label: '도메인 분리', desc: '기능별 컨트롤러 + DTO 계층 구조' },
]

export default function StatsStrip() {
  return (
    <section className="py-12">
      <div className="grid md:grid-cols-3 gap-4">
        {STATS.map(({ value, label, desc }) => (
          <div key={value} className="rounded-2xl border border-neutral-200 bg-white shadow-sm p-5">
            <p className="text-lg font-semibold text-neutral-900">{value}</p>
            <p className="text-xs font-medium text-[#7A7F3A] mt-0.5">{label}</p>
            <p className="text-xs text-neutral-500 mt-2 leading-relaxed">{desc}</p>
          </div>
        ))}
      </div>
    </section>
  )
}

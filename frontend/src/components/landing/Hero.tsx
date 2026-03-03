import { Link } from 'react-router-dom'
import Button from '../ui/Button'

export default function Hero() {
  return (
    <section className="py-16 md:py-24">
      <div className="flex flex-col md:flex-row items-center gap-12">
        {/* 텍스트 */}
        <div className="flex-1 space-y-6">
          <h1 className="text-3xl md:text-4xl font-semibold text-neutral-900 leading-snug">
            운동을 기록하고,<br />커뮤니티와 함께 성장하세요
          </h1>
          <p className="text-sm text-neutral-600 leading-relaxed max-w-sm">
            구조화된 운동일지와 실시간 커뮤니티 피드로<br />
            나의 루틴을 공유하고 동기를 얻어보세요.
          </p>
          <div className="flex gap-3">
            <Link to="/login"><Button variant="primary">시작하기</Button></Link>
            <a href="#demo"><Button variant="secondary">데모 보기</Button></a>
          </div>
        </div>

        {/* 대시보드 프리뷰 카드 */}
        <div className="flex-1 flex flex-col gap-3 w-full">
          <PreviewCard label="오늘의 운동" tag="하체"
            content="스쿼트 4×10 · 데드리프트 3×8 · RPE 8" />
          <PreviewCard label="커뮤니티 피드" tag="자유"
            content="오늘 PR 달성! 데드리프트 140kg 성공했습니다." />
          <PreviewCard label="알림" tag="알림"
            content="김철수님이 회원님의 게시물에 좋아요를 눌렀습니다." />
        </div>
      </div>
    </section>
  )
}

function PreviewCard({ label, content, tag }: { label: string; content: string; tag: string }) {
  return (
    <div className="rounded-2xl border border-neutral-200 bg-white shadow-sm p-4">
      <div className="flex items-center justify-between mb-2">
        <span className="text-xs font-semibold text-neutral-500 uppercase tracking-wide">{label}</span>
        <span className="text-xs bg-[#E8E7D1] text-neutral-700 px-2 py-0.5 rounded">{tag}</span>
      </div>
      <p className="text-sm text-neutral-800 leading-relaxed">{content}</p>
    </div>
  )
}

import { Link } from 'react-router-dom'
import Button from '../ui/Button'

export default function FinalCta() {
  return (
    <section className="py-16 border-t border-neutral-200">
      <div className="flex flex-col md:flex-row items-center justify-between gap-6">
        <div>
          <h2 className="text-lg font-semibold text-neutral-900">지금 바로 시작해보세요</h2>
          <p className="text-sm text-neutral-600 mt-1">커뮤니티에 합류하고 나의 운동을 기록하세요.</p>
        </div>
        <Link to="/login">
          <Button variant="primary">로그인 / 회원가입</Button>
        </Link>
      </div>

      <div className="mt-12 pt-6 border-t border-neutral-100 flex flex-col md:flex-row items-center justify-between gap-3">
        <p className="text-xs text-neutral-400">Community API Migration · Portfolio Project</p>
        <div className="flex items-center gap-4 text-xs text-neutral-400">
          <a href="https://github.com" className="hover:text-neutral-600 transition-colors">GitHub</a>
          <a href="/docs" className="hover:text-neutral-600 transition-colors">Docs</a>
        </div>
      </div>
    </section>
  )
}

import { useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { authApi } from '../api/endpoints/auth'

export default function FindUserIdPage() {
  const [form, setForm] = useState({ name: '', phoneNumber: '' })
  const [result, setResult] = useState<{ name: string; email: string } | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setResult(null)
    setLoading(true)
    try {
      const { data } = await authApi.findUserId(form)
      setResult(data.findUserIdResult)
    } catch {
      setError('일치하는 회원 정보를 찾을 수 없습니다.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#E8E7D1] flex items-center justify-center px-4">
      <div className="bg-white w-full max-w-sm rounded-xl shadow-md p-8">
        <h1 className="text-2xl font-bold text-[#7A7F3A] mb-2 text-center">아이디 찾기</h1>
        <p className="text-xs text-gray-500 text-center mb-6">
          가입 시 입력한 이름과 휴대전화 번호를 입력해주세요.
        </p>

        {result ? (
          <div className="flex flex-col gap-4">
            <div className="bg-[#E8E7D1] rounded-lg px-4 py-5 text-center">
              <p className="text-xs text-gray-500 mb-1">{result.name}님의 아이디(이메일)</p>
              <p className="text-base font-semibold text-[#7A7F3A]">{result.email}</p>
            </div>
            <Link to="/login" className="btn-primary w-full text-center block mt-2">
              로그인하러 가기
            </Link>
          </div>
        ) : (
          <form onSubmit={handleSubmit} className="flex flex-col gap-4">
            <div>
              <label className="block text-xs font-medium text-gray-600 mb-1">
                이름 <span className="text-red-400">*</span>
              </label>
              <input
                type="text"
                name="name"
                className="input-base"
                value={form.name}
                onChange={handleChange}
                required
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-gray-600 mb-1">
                휴대전화 <span className="text-red-400">*</span>
              </label>
              <input
                type="tel"
                name="phoneNumber"
                className="input-base"
                placeholder="010-0000-0000"
                value={form.phoneNumber}
                onChange={handleChange}
                required
              />
            </div>

            {error && <p className="text-xs text-red-500">{error}</p>}

            <button type="submit" className="btn-primary w-full mt-2" disabled={loading}>
              {loading ? '조회 중...' : '아이디 찾기'}
            </button>
          </form>
        )}

        <p className="text-center text-xs text-gray-500 mt-4">
          <Link to="/login" className="text-[#7A7F3A] font-medium hover:underline">
            로그인
          </Link>
          {' · '}
          <Link to="/signup" className="text-[#7A7F3A] font-medium hover:underline">
            회원가입
          </Link>
        </p>
      </div>
    </div>
  )
}

import { useState, type FormEvent } from 'react'
import { useNavigate, useLocation, Link } from 'react-router-dom'
import { authApi } from '../api/endpoints/auth'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const navigate  = useNavigate()
  const location  = useLocation()
  const { login } = useAuth()
  const [email,    setEmail]    = useState('')
  const [password, setPassword] = useState('')
  const [error,    setError]    = useState<string | null>(null)
  const [loading,  setLoading]  = useState(false)

  const signupSuccess = (location.state as { signupSuccess?: boolean } | null)?.signupSuccess

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      const { data } = await authApi.signin(email, password)
      await login(data.memberSigninResult.accessToken)
      navigate('/posts')
    } catch {
      setError('로그인에 실패했습니다. 이메일 또는 비밀번호를 확인해주세요.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#E8E7D1] flex items-center justify-center px-4">
      <div className="bg-white w-full max-w-sm rounded-xl shadow-md p-8">
        <h1 className="text-2xl font-bold text-[#7A7F3A] mb-6 text-center">Community</h1>

        {signupSuccess && (
          <p className="text-xs text-[#7A7F3A] bg-[#E8E7D1] rounded px-3 py-2 mb-4 text-center">
            회원가입이 완료되었습니다. 로그인해주세요.
          </p>
        )}

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">이메일</label>
            <input
              type="email"
              className="input-base"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">비밀번호</label>
            <input
              type="password"
              className="input-base"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}

          <button type="submit" className="btn-primary w-full mt-2" disabled={loading}>
            {loading ? '로그인 중...' : '로그인'}
          </button>
        </form>

        <p className="text-center text-xs text-gray-500 mt-4">
          계정이 없으신가요?{' '}
          <Link to="/signup" className="text-[#7A7F3A] font-medium hover:underline">
            회원가입
          </Link>
        </p>
      </div>
    </div>
  )
}

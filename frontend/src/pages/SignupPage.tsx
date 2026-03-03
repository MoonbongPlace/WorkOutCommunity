import { useState, type FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { authApi } from '../api/endpoints/auth'

export default function SignupPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({
    email:      '',
    memberName: '',
    password:   '',
    name:       '',
    age:        '',
    sex:        '',
  })
  const [error,   setError]   = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setLoading(true)
    try {
      await authApi.signup({
        email:      form.email,
        memberName: form.memberName,
        password:   form.password,
        name:       form.name,
        age:        form.age ? Number(form.age) : 0,
        sex:        form.sex,
      })
      navigate('/login', { state: { signupSuccess: true } })
    } catch {
      setError('회원가입에 실패했습니다. 입력 내용을 확인해주세요.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#E8E7D1] flex items-center justify-center px-4 py-12">
      <div className="bg-white w-full max-w-sm rounded-xl shadow-md p-8">
        <h1 className="text-2xl font-bold text-[#7A7F3A] mb-6 text-center">회원가입</h1>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <Field label="이메일" required>
            <input
              type="email"
              name="email"
              className="input-base"
              value={form.email}
              onChange={handleChange}
              required
            />
          </Field>

          <Field label="닉네임" required>
            <input
              type="text"
              name="memberName"
              className="input-base"
              value={form.memberName}
              onChange={handleChange}
              required
            />
          </Field>

          <Field label="비밀번호" required>
            <input
              type="password"
              name="password"
              className="input-base"
              value={form.password}
              onChange={handleChange}
              required
            />
          </Field>

          <Field label="이름" required>
            <input
              type="text"
              name="name"
              className="input-base"
              value={form.name}
              onChange={handleChange}
              required
            />
          </Field>

          <div className="grid grid-cols-2 gap-3">
            <Field label="나이">
              <input
                type="number"
                name="age"
                min="1"
                max="120"
                className="input-base"
                value={form.age}
                onChange={handleChange}
              />
            </Field>

            <Field label="성별">
              <select
                name="sex"
                className="input-base"
                value={form.sex}
                onChange={handleChange}
              >
                <option value="">선택</option>
                <option value="M">남성</option>
                <option value="F">여성</option>
              </select>
            </Field>
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}

          <button
            type="submit"
            className="btn-primary w-full mt-2"
            disabled={loading}
          >
            {loading ? '가입 중...' : '가입하기'}
          </button>
        </form>

        <p className="text-center text-xs text-gray-500 mt-4">
          이미 계정이 있으신가요?{' '}
          <Link to="/login" className="text-[#7A7F3A] font-medium hover:underline">
            로그인
          </Link>
        </p>
      </div>
    </div>
  )
}

function Field({ label, required, children }: { label: string; required?: boolean; children: React.ReactNode }) {
  return (
    <div>
      <label className="block text-xs font-medium text-gray-600 mb-1">
        {label}{required && <span className="text-red-400 ml-0.5">*</span>}
      </label>
      {children}
    </div>
  )
}

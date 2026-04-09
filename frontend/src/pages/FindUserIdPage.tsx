import { useState, useEffect, useRef, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { authApi } from '../api/endpoints/auth'

type Step = 'idle' | 'codeSent' | 'verified'

const TIMER_SECONDS = 180

export default function FindUserIdPage() {
  const [form, setForm] = useState({ name: '', phoneNumber: '' })
  const [code, setCode] = useState('')
  const [step, setStep] = useState<Step>('idle')
  const [verificationId, setVerificationId] = useState<number | null>(null)
  const [timer, setTimer] = useState(TIMER_SECONDS)
  const [result, setResult] = useState<{ name: string; email: string } | null>(null)
  const [error, setError] = useState<string | null>(null)
  const [codeError, setCodeError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)
  const [verifying, setVerifying] = useState(false)
  const timerRef = useRef<ReturnType<typeof setInterval> | null>(null)

  useEffect(() => {
    if (step !== 'codeSent') return
    setTimer(TIMER_SECONDS)
    timerRef.current = setInterval(() => {
      setTimer((t) => {
        if (t <= 1) {
          clearInterval(timerRef.current!)
          return 0
        }
        return t - 1
      })
    }, 1000)
    return () => clearInterval(timerRef.current!)
  }, [step, verificationId])

  function formatTimer(sec: number) {
    const m = String(Math.floor(sec / 60)).padStart(2, '0')
    const s = String(sec % 60).padStart(2, '0')
    return `${m}:${s}`
  }

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }))
  }

  async function handleSendCode(e: FormEvent) {
    e.preventDefault()
    setError(null)
    setCodeError(null)
    setLoading(true)
    try {
      const { data } = await authApi.verifyPhone({ phoneNumber: form.phoneNumber })
      setVerificationId(data.verifyResult.id)
      setCode('')
      setStep('codeSent')
    } catch {
      setError('인증번호 발송에 실패했습니다. 전화번호를 확인해주세요.')
    } finally {
      setLoading(false)
    }
  }

  async function handleVerifyCode(e: FormEvent) {
    e.preventDefault()
    if (!verificationId) return
    if (timer === 0) {
      setCodeError('인증 시간이 만료되었습니다. 인증번호를 다시 발급해주세요.')
      return
    }
    setCodeError(null)
    setVerifying(true)
    try {
      const { data } = await authApi.verifyPhoneResult({ id: verificationId, codeHash: code })
      if (data.message === '번호 인증 성공') {
        clearInterval(timerRef.current!)
        await handleFindUserId()
      } else {
        setCodeError('인증번호가 일치하지 않습니다.')
      }
    } catch {
      setCodeError('인증 확인에 실패했습니다. 다시 시도해주세요.')
    } finally {
      setVerifying(false)
    }
  }

  async function handleFindUserId() {
    setError(null)
    try {
      const { data } = await authApi.findUserId(form)
      setResult(data.findUserIdResult)
      setStep('verified')
    } catch {
      setError('일치하는 회원 정보를 찾을 수 없습니다.')
    }
  }

  if (result) {
    return (
      <div className="min-h-screen bg-[#E8E7D1] flex items-center justify-center px-4">
        <div className="bg-white w-full max-w-sm rounded-xl shadow-md p-8">
          <h1 className="text-2xl font-bold text-[#7A7F3A] mb-6 text-center">아이디 찾기</h1>
          <div className="flex flex-col gap-4">
            <div className="bg-[#E8E7D1] rounded-lg px-4 py-5 text-center">
              <p className="text-xs text-gray-500 mb-1">{result.name}님의 아이디(이메일)</p>
              <p className="text-base font-semibold text-[#7A7F3A]">{result.email}</p>
            </div>
            <Link to="/login" className="btn-primary w-full text-center block mt-2">
              로그인하러 가기
            </Link>
          </div>
          <p className="text-center text-xs text-gray-500 mt-4">
            <Link to="/login" className="text-[#7A7F3A] font-medium hover:underline">로그인</Link>
            {' · '}
            <Link to="/signup" className="text-[#7A7F3A] font-medium hover:underline">회원가입</Link>
          </p>
        </div>
      </div>
    )
  }

  return (
    <div className="min-h-screen bg-[#E8E7D1] flex items-center justify-center px-4">
      <div className="bg-white w-full max-w-sm rounded-xl shadow-md p-8">
        <h1 className="text-2xl font-bold text-[#7A7F3A] mb-2 text-center">아이디 찾기</h1>
        <p className="text-xs text-gray-500 text-center mb-6">
          가입 시 입력한 이름과 휴대전화 번호를 입력해주세요.
        </p>

        <form onSubmit={handleSendCode} className="flex flex-col gap-4">
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
              disabled={step === 'codeSent'}
              required
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              휴대전화 <span className="text-red-400">*</span>
            </label>
            <div className="flex gap-2">
              <input
                type="tel"
                name="phoneNumber"
                className="input-base flex-1"
                placeholder="010-0000-0000"
                value={form.phoneNumber}
                onChange={handleChange}
                disabled={step === 'codeSent'}
                required
              />
              <button
                type="submit"
                className="btn-primary whitespace-nowrap px-3 text-sm"
                disabled={loading || step === 'codeSent'}
              >
                {loading ? '발송 중...' : step === 'codeSent' ? '발송완료' : '인증번호 발급'}
              </button>
            </div>
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}
        </form>

        {step === 'codeSent' && (
          <form onSubmit={handleVerifyCode} className="flex flex-col gap-4 mt-4">
            <div>
              <label className="block text-xs font-medium text-gray-600 mb-1">
                인증번호 <span className="text-red-400">*</span>
              </label>
              <div className="flex gap-2">
                <div className="relative flex-1">
                  <input
                    type="text"
                    className="input-base w-full pr-14"
                    placeholder="인증번호 7자리"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                    maxLength={7}
                    required
                  />
                  <span className={`absolute right-3 top-1/2 -translate-y-1/2 text-xs font-medium ${timer === 0 ? 'text-red-500' : 'text-[#7A7F3A]'}`}>
                    {formatTimer(timer)}
                  </span>
                </div>
                <button
                  type="submit"
                  className="btn-primary whitespace-nowrap px-3 text-sm"
                  disabled={verifying || timer === 0}
                >
                  {verifying ? '확인 중...' : '인증 확인'}
                </button>
              </div>
              {codeError && <p className="text-xs text-red-500 mt-1">{codeError}</p>}
            </div>

            <button
              type="button"
              className="text-xs text-gray-400 hover:text-[#7A7F3A] underline text-center"
              onClick={() => {
                setStep('idle')
                setCode('')
                setCodeError(null)
                setError(null)
                clearInterval(timerRef.current!)
              }}
            >
              인증번호 재발급
            </button>
          </form>
        )}

        <p className="text-center text-xs text-gray-500 mt-6">
          <Link to="/login" className="text-[#7A7F3A] font-medium hover:underline">로그인</Link>
          {' · '}
          <Link to="/signup" className="text-[#7A7F3A] font-medium hover:underline">회원가입</Link>
        </p>
      </div>
    </div>
  )
}

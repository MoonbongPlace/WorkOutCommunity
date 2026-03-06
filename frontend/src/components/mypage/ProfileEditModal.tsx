import { useRef, useState } from 'react'
import { memberApi } from '../../api/endpoints/member'
import type { DetailMemberResult } from '../../types/member'

const DEFAULT_PROFILE = '/assets/default-profile.jpg'

interface Props {
  member: DetailMemberResult
  onClose: () => void
  onSaved: () => void
}

export default function ProfileEditModal({ member, onClose, onSaved }: Props) {
  const [memberName, setMemberName] = useState(member.memberName)
  const [name,       setName]       = useState(member.name ?? '')
  const [age,        setAge]        = useState<string>(member.age != null ? String(member.age) : '')
  const [sex,        setSex]        = useState(member.sex ?? '')
  const [preview,   setPreview]    = useState<string>(member.profileImage || DEFAULT_PROFILE)
  const [imageFile, setImageFile]  = useState<File | null>(null)
  const [saving,    setSaving]     = useState(false)
  const [error,     setError]      = useState<string | null>(null)

  const fileInputRef = useRef<HTMLInputElement>(null)

  function handleImageChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0]
    if (!file) return
    setImageFile(file)
    setPreview(URL.createObjectURL(file))
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(null)

    const data: Record<string, unknown> = {}
    if (memberName.trim()) data.memberName = memberName.trim()
    if (name.trim())       data.name       = name.trim()
    if (age !== '')        data.age        = Number(age)
    if (sex)               data.sex        = sex

    if (Object.keys(data).length === 0 && !imageFile) {
      setError('수정할 항목을 입력해 주세요.')
      return
    }

    const form = new FormData()
    form.append('data', JSON.stringify(data))
    if (imageFile) form.append('profileImage', imageFile)

    setSaving(true)
    try {
      await memberApi.update(form)
      onSaved()
    } catch {
      setError('수정에 실패했습니다. 다시 시도해 주세요.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/40"
      onClick={(e) => { if (e.target === e.currentTarget) onClose() }}
    >
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-md mx-4 overflow-hidden">
        {/* 헤더 */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-gray-100">
          <h2 className="text-base font-semibold text-gray-800">프로필 수정</h2>
          <button
            type="button"
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 text-xl leading-none"
          >
            ✕
          </button>
        </div>

        <form onSubmit={(e) => { void handleSubmit(e) }} className="px-6 py-5 space-y-4">
          {/* 프로필 이미지 */}
          <div className="flex flex-col items-center gap-2">
            <img
              src={preview}
              alt="프로필"
              className="w-20 h-20 rounded-full object-cover border-2 border-[#E8E7D1]"
              onError={() => setPreview(DEFAULT_PROFILE)}
            />
            <button
              type="button"
              onClick={() => fileInputRef.current?.click()}
              className="text-xs text-[#7A7F3A] underline"
            >
              사진 변경
            </button>
            <input
              ref={fileInputRef}
              type="file"
              accept="image/*"
              className="hidden"
              onChange={handleImageChange}
            />
          </div>

          {/* 닉네임 */}
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">닉네임</label>
            <input
              type="text"
              className="input-base"
              value={memberName}
              onChange={(e) => setMemberName(e.target.value)}
              placeholder="닉네임"
            />
          </div>

          {/* 이름 */}
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">이름</label>
            <input
              type="text"
              className="input-base"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="이름"
            />
          </div>

          {/* 나이 */}
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">나이</label>
            <input
              type="number"
              className="input-base"
              value={age}
              min={1}
              max={150}
              onChange={(e) => setAge(e.target.value)}
              placeholder="나이"
            />
          </div>

          {/* 성별 */}
          <div>
            <label className="block text-xs font-medium text-gray-500 mb-1">성별</label>
            <select
              className="input-base"
              value={sex}
              onChange={(e) => setSex(e.target.value)}
            >
              <option value="">선택 안 함</option>
              <option value="MALE">남성</option>
              <option value="FEMALE">여성</option>
            </select>
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}

          {/* 버튼 */}
          <div className="flex gap-2 pt-1">
            <button
              type="button"
              onClick={onClose}
              className="btn-secondary flex-1 text-sm"
              disabled={saving}
            >
              취소
            </button>
            <button
              type="submit"
              className="btn-primary flex-1 text-sm"
              disabled={saving}
            >
              {saving ? '저장 중...' : '저장'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

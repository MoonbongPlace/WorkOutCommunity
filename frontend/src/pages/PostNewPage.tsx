import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import PageHeader from '../components/ui/PageHeader'
import { postApi } from '../api/endpoints/post'
import { categoryApi } from '../api/endpoints/category'
import type { CategoryItem } from '../types/category'

export default function PostNewPage() {
  const navigate = useNavigate()
  const [title,      setTitle]      = useState('')
  const [content,    setContent]    = useState('')
  const [categoryId, setCategoryId] = useState<number | null>(null)
  const [categories, setCategories] = useState<CategoryItem[]>([])
  const [saving,     setSaving]     = useState(false)
  const [error,      setError]      = useState<string | null>(null)

  useEffect(() => {
    categoryApi.list()
      .then(({ data }) => setCategories(data.categoryListResult.categoryList))
      .catch(() => {/* 카테고리 로드 실패 시 빈 목록 유지 */})
  }, [])

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    if (!title.trim() || !content.trim()) {
      setError('제목과 내용을 모두 입력해주세요.')
      return
    }
    setSaving(true)
    setError(null)
    try {
      await postApi.create({
        title: title.trim(),
        content: content.trim(),
        ...(categoryId !== null && { categoryId }),
      })
      navigate('/posts')
    } catch {
      setError('게시글 생성에 실패했습니다. 다시 시도해주세요.')
    } finally {
      setSaving(false)
    }
  }

  return (
    <div>
      <button
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-[#7A7F3A] mb-4 transition-colors"
        onClick={() => navigate(-1)}
      >
        ← 뒤로
      </button>

      <PageHeader title="게시글 작성" />

      <form onSubmit={handleSubmit} className="max-w-2xl">
        <Card className="flex flex-col gap-4">
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              제목 <span className="text-red-400">*</span>
            </label>
            <input
              type="text"
              className="input-base"
              placeholder="제목을 입력하세요"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              maxLength={100}
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">카테고리</label>
            <select
              className="input-base"
              value={categoryId ?? ''}
              onChange={(e) => setCategoryId(e.target.value === '' ? null : Number(e.target.value))}
            >
              <option value="">카테고리 없음</option>
              {categories.map((cat) => (
                <option key={cat.id} value={cat.id}>{cat.name}</option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              내용 <span className="text-red-400">*</span>
            </label>
            <textarea
              className="input-base resize-none"
              placeholder="내용을 입력하세요"
              rows={10}
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}

          <div className="flex gap-3 justify-end">
            <button
              type="button"
              className="btn-secondary"
              onClick={() => navigate(-1)}
            >
              취소
            </button>
            <button
              type="submit"
              className="btn-primary"
              disabled={saving}
            >
              {saving ? '저장 중...' : '게시글 등록'}
            </button>
          </div>
        </Card>
      </form>
    </div>
  )
}

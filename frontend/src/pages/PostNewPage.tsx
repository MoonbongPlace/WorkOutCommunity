import { useEffect, useRef, useState, type FormEvent } from 'react'
import { useNavigate } from 'react-router-dom'
import Card from '../components/ui/Card'
import PageHeader from '../components/ui/PageHeader'
import { postApi } from '../api/endpoints/post'
import { categoryApi } from '../api/endpoints/category'
import type { CategoryItem } from '../types/category'

const MAX_IMAGES = 6

type ImageEntry = { file: File; preview: string }

export default function PostNewPage() {
  const navigate      = useNavigate()
  const textareaRef   = useRef<HTMLTextAreaElement>(null)
  const fileInputRef  = useRef<HTMLInputElement>(null)

  const [title,      setTitle]      = useState('')
  const [content,    setContent]    = useState('')
  const [categoryId, setCategoryId] = useState<number | null>(null)
  const [categories, setCategories] = useState<CategoryItem[]>([])
  const [images,     setImages]     = useState<ImageEntry[]>([])
  const [saving,     setSaving]     = useState(false)
  const [error,      setError]      = useState<string | null>(null)

  useEffect(() => {
    categoryApi.list()
      .then(({ data }) => setCategories(data.categoryListResult.categoryList))
      .catch(() => {})
  }, [])

  // 미리보기 URL 해제
  useEffect(() => {
    return () => images.forEach((img) => URL.revokeObjectURL(img.preview))
  }, [])

  function handleImageSelect(e: React.ChangeEvent<HTMLInputElement>) {
    const files     = Array.from(e.target.files ?? [])
    const remaining = MAX_IMAGES - images.length
    const toAdd     = files.slice(0, remaining).map((file) => ({
      file,
      preview: URL.createObjectURL(file),
    }))
    if (files.length > remaining) {
      setError(`이미지는 최대 ${MAX_IMAGES}장까지 첨부할 수 있습니다.`)
    }
    setImages((prev) => [...prev, ...toAdd])
    e.target.value = ''
    // 이미지 선택 후 textarea 포커스 복귀 → 이어서 글 작성 가능
    textareaRef.current?.focus()
  }

  function removeImage(index: number) {
    setImages((prev) => {
      URL.revokeObjectURL(prev[index].preview)
      return prev.filter((_, i) => i !== index)
    })
  }

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    if (!title.trim() || !content.trim()) {
      setError('제목과 내용을 모두 입력해주세요.')
      return
    }
    setSaving(true)
    setError(null)
    try {
      await postApi.create(
        {
          title: title.trim(),
          content: content.trim(),
          ...(categoryId !== null && { categoryId }),
        },
        images.map((img) => img.file),
      )
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
          {/* 제목 */}
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

          {/* 카테고리 */}
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

          {/* 내용 + 이미지 첨부 툴바 일체형 */}
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">
              내용 <span className="text-red-400">*</span>
            </label>
            <div className="border border-gray-200 rounded-lg overflow-hidden focus-within:ring-2 focus-within:ring-[#7A7F3A]/30 transition-shadow">
              <textarea
                ref={textareaRef}
                className="w-full px-3 pt-3 pb-2 text-sm text-gray-800 resize-none outline-none bg-white"
                placeholder="내용을 입력하세요"
                rows={10}
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />

              {/* 이미지 첨부 툴바 — textarea 하단에 붙어 있어 글쓰기 흐름 유지 */}
              <div className="flex items-center gap-2 px-3 py-2 border-t border-gray-100 bg-gray-50">
                <button
                  type="button"
                  disabled={images.length >= MAX_IMAGES}
                  onClick={() => {
                    setError(null)
                    fileInputRef.current?.click()
                  }}
                  className="flex items-center gap-1.5 text-xs text-gray-500 hover:text-[#7A7F3A] disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.8}>
                    <path strokeLinecap="round" strokeLinejoin="round" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  이미지 첨부
                </button>
                <span className="ml-auto text-xs text-gray-400">{images.length}/{MAX_IMAGES}</span>
              </div>
            </div>

            {/* 첨부 이미지 미리보기 */}
            {images.length > 0 && (
              <div className="flex flex-wrap gap-2 mt-2">
                {images.map((img, i) => (
                  <div key={i} className="relative group">
                    <img
                      src={img.preview}
                      alt={`첨부 이미지 ${i + 1}`}
                      className="w-20 h-20 object-cover rounded-lg border border-gray-200"
                    />
                    <button
                      type="button"
                      onClick={() => removeImage(i)}
                      className="absolute -top-1.5 -right-1.5 w-5 h-5 bg-gray-700 text-white text-xs rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity leading-none"
                      aria-label="이미지 제거"
                    >
                      ×
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>

          {error && <p className="text-xs text-red-500">{error}</p>}

          <div className="flex gap-3 justify-end">
            <button type="button" className="btn-secondary" onClick={() => navigate(-1)}>
              취소
            </button>
            <button type="submit" className="btn-primary" disabled={saving}>
              {saving ? '저장 중...' : '게시글 등록'}
            </button>
          </div>
        </Card>
      </form>

      {/* hidden file input */}
      <input
        ref={fileInputRef}
        type="file"
        accept="image/jpeg,image/png,image/webp"
        multiple
        className="hidden"
        onChange={handleImageSelect}
      />
    </div>
  )
}

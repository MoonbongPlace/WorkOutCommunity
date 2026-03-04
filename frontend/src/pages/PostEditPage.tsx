import { useEffect, useRef, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Card from '../components/ui/Card'
import PageHeader from '../components/ui/PageHeader'
import StateBlock from '../components/ui/StateBlock'
import { postApi } from '../api/endpoints/post'
import { categoryApi } from '../api/endpoints/category'
import type { CategoryItem } from '../types/category'

const MAX_IMAGES = 6

type NewImageEntry = { file: File; preview: string }

export default function PostEditPage() {
  const { postId }   = useParams<{ postId: string }>()
  const navigate     = useNavigate()
  const textareaRef  = useRef<HTMLTextAreaElement>(null)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const [loadStatus,     setLoadStatus]     = useState<'loading' | 'success' | 'error'>('loading')
  const [title,          setTitle]          = useState('')
  const [content,        setContent]        = useState('')
  const [categoryId,     setCategoryId]     = useState<number | null>(null)
  const [categories,     setCategories]     = useState<CategoryItem[]>([])
  // 기존 이미지 중 유지할 URL 목록
  const [keepImages,     setKeepImages]     = useState<string[]>([])
  // 새로 첨부할 이미지 목록
  const [newImages,      setNewImages]      = useState<NewImageEntry[]>([])
  const [saving,         setSaving]         = useState(false)
  const [error,          setError]          = useState<string | null>(null)

  const totalCount = keepImages.length + newImages.length

  useEffect(() => {
    async function load() {
      if (!postId) return
      try {
        const [postRes, catRes] = await Promise.all([
          postApi.detail(Number(postId)),
          categoryApi.list(),
        ])
        const p = postRes.data.post
        setTitle(p.title)
        setContent(p.content)
        setCategoryId(p.categoryId ?? null)
        setKeepImages(p.images ?? [])
        setCategories(catRes.data.categoryListResult.categoryList)
        setLoadStatus('success')
      } catch {
        setLoadStatus('error')
      }
    }
    void load()
  }, [postId])

  // 새 이미지 미리보기 URL 해제
  useEffect(() => {
    return () => newImages.forEach((img) => URL.revokeObjectURL(img.preview))
  }, [])

  function handleImageSelect(e: React.ChangeEvent<HTMLInputElement>) {
    const files     = Array.from(e.target.files ?? [])
    const remaining = MAX_IMAGES - totalCount
    const toAdd     = files.slice(0, remaining).map((file) => ({
      file,
      preview: URL.createObjectURL(file),
    }))
    if (files.length > remaining) {
      setError(`이미지는 최대 ${MAX_IMAGES}장까지 첨부할 수 있습니다.`)
    }
    setNewImages((prev) => [...prev, ...toAdd])
    e.target.value = ''
    textareaRef.current?.focus()
  }

  function removeKeepImage(url: string) {
    setKeepImages((prev) => prev.filter((u) => u !== url))
  }

  function removeNewImage(index: number) {
    setNewImages((prev) => {
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
      await postApi.update(
        Number(postId),
        {
          title: title.trim(),
          content: content.trim(),
          ...(categoryId !== null && { categoryId }),
          keepImages,
        },
        newImages.map((img) => img.file),
      )
      navigate(`/posts/${postId}`)
    } catch {
      setError('게시글 수정에 실패했습니다. 다시 시도해주세요.')
    } finally {
      setSaving(false)
    }
  }

  if (loadStatus === 'loading') return <StateBlock type="loading" />
  if (loadStatus === 'error')   return <StateBlock type="error" />

  return (
    <div>
      <button
        className="flex items-center gap-1 text-sm text-gray-500 hover:text-[#7A7F3A] mb-4 transition-colors"
        onClick={() => navigate(-1)}
      >
        ← 뒤로
      </button>

      <PageHeader title="게시글 수정" />

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
                rows={10}
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />

              {/* 이미지 첨부 툴바 */}
              <div className="flex items-center gap-2 px-3 py-2 border-t border-gray-100 bg-gray-50">
                <button
                  type="button"
                  disabled={totalCount >= MAX_IMAGES}
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
                <span className="ml-auto text-xs text-gray-400">{totalCount}/{MAX_IMAGES}</span>
              </div>
            </div>

            {/* 이미지 미리보기: 기존 이미지(유지 목록) + 새 이미지 */}
            {(keepImages.length > 0 || newImages.length > 0) && (
              <div className="flex flex-wrap gap-2 mt-2">
                {keepImages.map((url) => (
                  <div key={url} className="relative group">
                    <img
                      src={url}
                      alt="기존 이미지"
                      className="w-20 h-20 object-cover rounded-lg border border-gray-200"
                    />
                    <button
                      type="button"
                      onClick={() => removeKeepImage(url)}
                      className="absolute -top-1.5 -right-1.5 w-5 h-5 bg-gray-700 text-white text-xs rounded-full flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity leading-none"
                      aria-label="이미지 제거"
                    >
                      ×
                    </button>
                  </div>
                ))}
                {newImages.map((img, i) => (
                  <div key={`new-${i}`} className="relative group">
                    <img
                      src={img.preview}
                      alt={`새 이미지 ${i + 1}`}
                      className="w-20 h-20 object-cover rounded-lg border border-[#7A7F3A]/40"
                    />
                    {/* 새 이미지는 초록 테두리로 구분 */}
                    <span className="absolute bottom-0 left-0 right-0 text-center text-[10px] bg-[#7A7F3A]/70 text-white rounded-b-lg py-0.5">
                      새 이미지
                    </span>
                    <button
                      type="button"
                      onClick={() => removeNewImage(i)}
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
              {saving ? '저장 중...' : '수정 완료'}
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

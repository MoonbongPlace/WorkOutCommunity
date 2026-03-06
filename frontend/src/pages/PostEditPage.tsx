import { useEffect, useState, type FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import Card from '../components/ui/Card'
import PageHeader from '../components/ui/PageHeader'
import StateBlock from '../components/ui/StateBlock'
import BlockEditor, { type Block, type KeepImageBlock, type TextBlock, uid } from '../components/post/BlockEditor'
import { postApi } from '../api/endpoints/post'
import { categoryApi } from '../api/endpoints/category'
import type { CategoryItem } from '../types/category'

const MAX_IMAGES = 6

function parseToBlocks(content: string, images: string[]): Block[] {
  if (!images.length && !/\[\[image:\d+\]\]/.test(content)) {
    return [{ id: uid(), type: 'text', value: content }]
  }

  // 마커 없는 기존 게시글 (하위 호환)
  if (images.length > 0 && !/\[\[image:\d+\]\]/.test(content)) {
    const result: Block[] = [{ id: uid(), type: 'text', value: content }]
    images.forEach(url => {
      result.push({ id: uid(), type: 'image', source: 'keep', url } as KeepImageBlock)
      result.push({ id: uid(), type: 'text', value: '' } as TextBlock)
    })
    return result
  }

  const parts = content.split(/(\[\[image:\d+\]\])/)
  const result: Block[] = []
  for (const part of parts) {
    const match = part.match(/^\[\[image:(\d+)\]\]$/)
    if (match) {
      const idx = parseInt(match[1])
      if (idx < images.length) {
        result.push({ id: uid(), type: 'image', source: 'keep', url: images[idx] } as KeepImageBlock)
      }
    } else {
      const text = part.replace(/^\n/, '').replace(/\n$/, '')
      result.push({ id: uid(), type: 'text', value: text } as TextBlock)
    }
  }

  if (!result.length || result[0].type === 'image') result.unshift({ id: uid(), type: 'text', value: '' })
  if (result[result.length - 1].type === 'image') result.push({ id: uid(), type: 'text', value: '' })
  return result
}

function serializeBlocks(blocks: Block[]): { content: string; keepImages: string[]; newFiles: File[] } {
  const keepImages: string[] = []
  const newFiles: File[] = []

  // 먼저 수집해서 인덱스 오프셋 파악
  for (const b of blocks) {
    if (b.type !== 'image') continue
    if (b.source === 'keep') keepImages.push(b.url)
    else newFiles.push(b.file)
  }

  const keepCount = keepImages.length
  let keepIdx = 0
  let newIdx = 0
  const parts: string[] = []

  for (const b of blocks) {
    if (b.type === 'text') {
      if (b.value.trim()) parts.push(b.value)
    } else if (b.source === 'keep') {
      parts.push(`[[image:${keepIdx++}]]`)
    } else {
      parts.push(`[[image:${keepCount + newIdx++}]]`)
    }
  }

  return { content: parts.join('\n'), keepImages, newFiles }
}

export default function PostEditPage() {
  const { postId }   = useParams<{ postId: string }>()
  const navigate     = useNavigate()

  const [loadStatus, setLoadStatus] = useState<'loading' | 'success' | 'error'>('loading')
  const [title,      setTitle]      = useState('')
  const [categoryId, setCategoryId] = useState<number | null>(null)
  const [categories, setCategories] = useState<CategoryItem[]>([])
  const [blocks,     setBlocks]     = useState<Block[]>([])
  const [saving,     setSaving]     = useState(false)
  const [error,      setError]      = useState<string | null>(null)

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
        setCategoryId(p.categoryId ?? null)
        setCategories(catRes.data.categoryListResult.categoryList)
        setBlocks(parseToBlocks(p.content, p.images ?? []))
        setLoadStatus('success')
      } catch {
        setLoadStatus('error')
      }
    }
    void load()
  }, [postId])

  async function handleSubmit(e: FormEvent) {
    e.preventDefault()
    const { content, keepImages, newFiles } = serializeBlocks(blocks)
    if (!title.trim() || !content) {
      setError('제목과 내용을 모두 입력해주세요.')
      return
    }
    setSaving(true)
    setError(null)
    try {
      await postApi.update(
        Number(postId),
        { title: title.trim(), content, ...(categoryId !== null && { categoryId }), keepImages },
        newFiles,
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

          {/* 블록 에디터 */}
          <div>
            <label className="block text-xs font-medium text-gray-600 mb-1">내용</label>
            <BlockEditor
              blocks={blocks}
              onChange={setBlocks}
              disabled={saving}
              maxImages={MAX_IMAGES}
            />
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
    </div>
  )
}

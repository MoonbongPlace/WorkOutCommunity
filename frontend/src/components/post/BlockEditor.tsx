import { useEffect, useRef, useState } from 'react'

export type TextBlock     = { id: string; type: 'text'; value: string }
export type NewImageBlock = { id: string; type: 'image'; source: 'new'; file: File; preview: string }
export type KeepImageBlock = { id: string; type: 'image'; source: 'keep'; url: string }
export type ImageBlock    = NewImageBlock | KeepImageBlock
export type Block         = TextBlock | ImageBlock

export function uid(): string {
  return Math.random().toString(36).slice(2, 10)
}

export function initBlocks(): Block[] {
  return [{ id: uid(), type: 'text', value: '' }]
}

interface Props {
  blocks: Block[]
  onChange: (blocks: Block[]) => void
  disabled?: boolean
  maxImages?: number
}

export default function BlockEditor({ blocks, onChange, disabled = false, maxImages = 6 }: Props) {
  const fileInputRef    = useRef<HTMLInputElement>(null)
  const [insertAfterId, setInsertAfterId] = useState<string | null>(null)
  const [limitError,    setLimitError]    = useState(false)

  const imageCount = blocks.filter(b => b.type === 'image').length

  // preview URL 해제
  useEffect(() => {
    return () => {
      blocks.forEach(b => {
        if (b.type === 'image' && b.source === 'new') URL.revokeObjectURL(b.preview)
      })
    }
  }, [])

  function updateText(id: string, value: string) {
    onChange(blocks.map(b => b.id === id && b.type === 'text' ? { ...b, value } : b))
  }

  function openFilePicker(afterId: string) {
    if (imageCount >= maxImages) { setLimitError(true); return }
    setLimitError(false)
    setInsertAfterId(afterId)
    fileInputRef.current?.click()
  }

  function handleFileSelect(e: React.ChangeEvent<HTMLInputElement>) {
    const files = Array.from(e.target.files ?? [])
    e.target.value = ''
    if (!insertAfterId || files.length === 0) return
    const remaining = maxImages - imageCount
    const toAdd = files.slice(0, remaining)
    if (files.length > remaining) setLimitError(true)

    const idx = blocks.findIndex(b => b.id === insertAfterId)
    if (idx === -1) return
    const next = [...blocks]
    let at = idx + 1
    for (const file of toAdd) {
      const imgBlock: NewImageBlock = { id: uid(), type: 'image', source: 'new', file, preview: URL.createObjectURL(file) }
      const txtBlock: TextBlock     = { id: uid(), type: 'text', value: '' }
      next.splice(at++, 0, imgBlock)
      next.splice(at++, 0, txtBlock)
    }
    onChange(next)
    setInsertAfterId(null)
  }

  function removeImage(imageId: string) {
    const idx = blocks.findIndex(b => b.id === imageId)
    if (idx === -1) return
    const b = blocks[idx]
    if (b.type === 'image' && b.source === 'new') URL.revokeObjectURL(b.preview)
    const next = [...blocks]
    const prevB = idx > 0 ? next[idx - 1] : null
    const nextB = idx < next.length - 1 ? next[idx + 1] : null
    if (prevB?.type === 'text' && nextB?.type === 'text') {
      const merged = prevB.value + (prevB.value && nextB.value ? '\n' : '') + nextB.value
      next[idx - 1] = { ...prevB, value: merged }
      next.splice(idx, 2)
    } else {
      next.splice(idx, 1)
    }
    if (next.length === 0) next.push({ id: uid(), type: 'text', value: '' })
    onChange(next)
    setLimitError(false)
  }

  const imgIcon = (
    <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={1.8}>
      <path strokeLinecap="round" strokeLinejoin="round" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
    </svg>
  )

  return (
    <div>
      <div className="border border-gray-200 rounded-lg overflow-hidden focus-within:ring-2 focus-within:ring-[#7A7F3A]/30 transition-shadow">
        {blocks.map((block, idx) =>
          block.type === 'text' ? (
            <div key={block.id} className={idx > 0 ? 'border-t border-gray-100' : ''}>
              <AutoTextarea
                value={block.value}
                onChange={(v) => updateText(block.id, v)}
                placeholder={idx === 0 ? '내용을 입력하세요' : ''}
                disabled={disabled}
              />
              <div className="flex items-center px-3 py-2 border-t border-gray-100 bg-gray-50">
                <button
                  type="button"
                  disabled={imageCount >= maxImages || disabled}
                  onClick={() => openFilePicker(block.id)}
                  className="flex items-center gap-1.5 text-xs text-gray-500 hover:text-[#7A7F3A] disabled:opacity-40 disabled:cursor-not-allowed transition-colors"
                >
                  {imgIcon}
                  이미지 삽입
                </button>
                <span className="ml-auto text-xs text-gray-400">{imageCount}/{maxImages}</span>
              </div>
            </div>
          ) : (
            <div key={block.id} className="relative border-t border-gray-100">
              <img
                src={block.source === 'new' ? block.preview : block.url}
                alt="첨부 이미지"
                className="w-full max-h-96 object-contain bg-gray-50"
              />
              <button
                type="button"
                onClick={() => removeImage(block.id)}
                disabled={disabled}
                className="absolute top-2 right-2 w-7 h-7 bg-black/50 hover:bg-black/70 text-white rounded-full flex items-center justify-center text-sm transition-colors disabled:opacity-50"
                aria-label="이미지 제거"
              >
                ×
              </button>
            </div>
          )
        )}
      </div>

      {limitError && (
        <p className="mt-1 text-xs text-red-500">이미지는 최대 {maxImages}장까지 첨부할 수 있습니다.</p>
      )}

      <input
        ref={fileInputRef}
        type="file"
        accept="image/jpeg,image/png,image/webp"
        multiple
        className="hidden"
        onChange={handleFileSelect}
      />
    </div>
  )
}

function AutoTextarea({ value, onChange, placeholder, disabled }: {
  value: string
  onChange: (v: string) => void
  placeholder?: string
  disabled?: boolean
}) {
  const ref = useRef<HTMLTextAreaElement>(null)

  useEffect(() => {
    const el = ref.current
    if (!el) return
    el.style.height = 'auto'
    el.style.height = Math.max(80, el.scrollHeight) + 'px'
  }, [value])

  return (
    <textarea
      ref={ref}
      className="w-full px-3 py-3 text-sm text-gray-800 resize-none outline-none bg-white overflow-hidden"
      style={{ minHeight: '80px' }}
      placeholder={placeholder}
      value={value}
      onChange={(e) => onChange(e.target.value)}
      disabled={disabled}
    />
  )
}

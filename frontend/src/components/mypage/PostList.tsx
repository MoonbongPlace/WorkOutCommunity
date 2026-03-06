import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { postApi } from '../../api/endpoints/post'
import type { PostListItem } from '../../types/post'
import StateBlock from '../ui/StateBlock'

type Status = 'loading' | 'success' | 'error'

export default function PostList() {
  const navigate = useNavigate()
  const [status, setStatus] = useState<Status>('loading')
  const [posts,  setPosts]  = useState<PostListItem[]>([])

  async function load() {
    setStatus('loading')
    try {
      const { data: res } = await postApi.me()
      setPosts(res.postListResult.content)
      setStatus('success')
    } catch {
      setStatus('error')
    }
  }

  useEffect(() => { void load() }, [])

  if (status === 'loading') return <StateBlock type="loading" />
  if (status === 'error')   return <StateBlock type="error" onRetry={() => void load()} />

  if (posts.length === 0) {
    return (
      <StateBlock type="empty" message="작성한 게시글이 없습니다." />
    )
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full text-sm">
        <thead>
          <tr className="border-b border-[#E8E7D1] text-gray-500">
            <th className="py-3 text-left font-medium pl-2">제목</th>
            <th className="py-3 text-right font-medium pr-2 w-28">작성일</th>
          </tr>
        </thead>
        <tbody>
          {posts.map((post) => (
            <tr
              key={post.id}
              onClick={() => navigate(`/posts/${post.id}`)}
              className="border-b border-[#E8E7D1] last:border-0 cursor-pointer hover:bg-[#F9F9F2] transition-colors"
            >
              <td className="py-3 pl-2 font-medium text-gray-800 truncate max-w-xs">
                {post.title}
              </td>
              <td className="py-3 pr-2 text-right text-gray-400 whitespace-nowrap">
                {new Date(post.createdAt).toLocaleDateString('ko-KR')}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

import Badge from '../ui/Badge'

const FEED = [
  {
    id: 1, avatar: '김', nickname: '김철수', time: '방금 전',
    text: '오늘 데드리프트 PR 달성! 140kg 성공했습니다. 6개월 동안 꾸준히 한 결과네요.',
    tags: ['#하체', '#PR달성'], likes: 24, comments: 8, bookmarks: 3, liked: true,
  },
  {
    id: 2, avatar: '이', nickname: '이지은', time: '12분 전',
    text: '오늘 풀바디 루틴 완료. 스쿼트 4×10, 벤치 4×8, 풀업 3×Max. 총 1시간 15분.',
    tags: ['#풀바디', '#루틴'], likes: 17, comments: 4, bookmarks: 6, liked: false,
  },
  {
    id: 3, avatar: '박', nickname: '박준호', time: '35분 전',
    text: "러닝 5km 완주! 평균 페이스 5'20\". 꾸준히 하다 보니 숨이 훨씬 덜 차네요.",
    tags: ['#유산소', '#러닝'], likes: 9, comments: 2, bookmarks: 1, liked: false,
  },
  {
    id: 4, avatar: '최', nickname: '최민서', time: '1시간 전',
    text: '숄더프레스 10kg×3×10 클리어. 어깨가 점점 넓어지는 것 같아요.',
    tags: ['#상체', '#어깨'], likes: 31, comments: 11, bookmarks: 5, liked: false,
  },
]

export default function ActivityFeedPreview() {
  return (
    <section id="demo" className="py-16">
      <h2 className="text-lg font-semibold text-neutral-900 mb-2">커뮤니티 피드</h2>
      <p className="text-sm text-neutral-600 mb-8">회원들의 실시간 운동 기록과 소통을 확인하세요.</p>
      <div className="grid md:grid-cols-2 gap-4">
        {FEED.map((post) => <FeedCard key={post.id} post={post} />)}
      </div>
    </section>
  )
}

function FeedCard({ post }: { post: typeof FEED[0] }) {
  return (
    <div className="rounded-2xl border border-neutral-200 bg-white shadow-sm p-5 flex flex-col gap-3">
      <div className="flex items-center gap-3">
        <div className="w-9 h-9 rounded-full bg-[#E8E7D1] flex items-center justify-center text-sm font-semibold text-[#7A7F3A]">
          {post.avatar}
        </div>
        <div>
          <p className="text-sm font-medium text-neutral-900">{post.nickname}</p>
          <p className="text-xs text-neutral-400">{post.time}</p>
        </div>
      </div>

      <p className="text-sm text-neutral-700 leading-relaxed">{post.text}</p>

      <div className="flex flex-wrap gap-1.5">
        {post.tags.map((tag) => <Badge key={tag}>{tag}</Badge>)}
      </div>

      <div className="flex items-center gap-4 pt-1 border-t border-neutral-100">
        <button className={`flex items-center gap-1 text-xs transition-colors ${post.liked ? 'text-[#7A7F3A] font-medium' : 'text-neutral-400 hover:text-neutral-600'}`}>
          <HeartIcon filled={post.liked} />{post.likes}
        </button>
        <button className="flex items-center gap-1 text-xs text-neutral-400 hover:text-neutral-600 transition-colors">
          <CommentIcon />{post.comments}
        </button>
        <button className="flex items-center gap-1 text-xs text-neutral-400 hover:text-neutral-600 transition-colors ml-auto">
          <BookmarkIcon />{post.bookmarks}
        </button>
      </div>
    </div>
  )
}

function HeartIcon({ filled }: { filled: boolean }) {
  return (
    <svg className="w-4 h-4" fill={filled ? 'currentColor' : 'none'} stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" d="M21 8.25c0-2.485-2.099-4.5-4.688-4.5-1.935 0-3.597 1.126-4.312 2.733-.715-1.607-2.377-2.733-4.313-2.733C5.1 3.75 3 5.765 3 8.25c0 7.22 9 12 9 12s9-4.78 9-12z" />
    </svg>
  )
}

function CommentIcon() {
  return (
    <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" d="M12 20.25c4.97 0 9-3.694 9-8.25s-4.03-8.25-9-8.25S3 7.444 3 12c0 2.104.859 4.023 2.273 5.48.432.447.74 1.04.586 1.641a4.483 4.483 0 01-.923 1.785A5.969 5.969 0 006 21c1.282 0 2.47-.402 3.445-1.087.81.22 1.668.337 2.555.337z" />
    </svg>
  )
}

function BookmarkIcon() {
  return (
    <svg className="w-4 h-4" fill="none" stroke="currentColor" strokeWidth={1.5} viewBox="0 0 24 24">
      <path strokeLinecap="round" strokeLinejoin="round" d="M17.593 3.322c1.1.128 1.907 1.077 1.907 2.185V21L12 17.25 4.5 21V5.507c0-1.108.806-2.057 1.907-2.185a48.507 48.507 0 0111.186 0z" />
    </svg>
  )
}

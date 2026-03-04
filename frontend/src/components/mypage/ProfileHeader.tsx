import { useState } from 'react'
import type { DetailMemberResult } from '../../types/member'
import ProfileEditModal from './ProfileEditModal'

const DEFAULT_PROFILE = '/assets/default-profile.jpg'

interface Props {
  member: DetailMemberResult
  onUpdated: () => void
}

export default function ProfileHeader({ member, onUpdated }: Props) {
  const [imgSrc,     setImgSrc]     = useState(member.profileImage || DEFAULT_PROFILE)
  const [showModal,  setShowModal]  = useState(false)

  return (
    <>
      <div className="flex flex-col items-center gap-4 py-8">
        <img
          src={imgSrc}
          alt={member.memberName}
          className="w-24 h-24 rounded-full object-cover border-4 border-white shadow-md"
          onError={() => {
            if (imgSrc !== DEFAULT_PROFILE) setImgSrc(DEFAULT_PROFILE)
          }}
        />

        <h2 className="text-xl font-bold text-gray-800">{member.memberName}</h2>

        <button
          className="btn-secondary text-sm px-5 py-2"
          onClick={() => setShowModal(true)}
        >
          프로필 수정
        </button>
      </div>

      {showModal && (
        <ProfileEditModal
          member={member}
          onClose={() => setShowModal(false)}
          onSaved={() => {
            setShowModal(false)
            onUpdated()
          }}
        />
      )}
    </>
  )
}

import axiosInstance from '../axiosInstance'
import type { MemberInfoResponse } from '../../types/member'

export const memberApi = {
  me: () =>
    axiosInstance.get<MemberInfoResponse>('/v1/members/me'),

  update: (body: { memberName?: string; name?: string; age?: number; sex?: string }) =>
    axiosInstance.put('/v1/members/me', body),

  withdraw: () =>
    axiosInstance.delete('/v1/members/me'),
}

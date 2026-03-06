import axiosInstance from '../axiosInstance'
import type { MemberInfoResponse } from '../../types/member'

export const memberApi = {
  me: () =>
    axiosInstance.get<MemberInfoResponse>('/v1/members/me'),

  update: (form: FormData) =>
    axiosInstance.patch('/v1/members/me', form, {
      headers: { 'Content-Type': 'multipart/form-data' },
    }),

  withdraw: () =>
    axiosInstance.delete('/v1/members/me'),
}

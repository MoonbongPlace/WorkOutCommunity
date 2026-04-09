import axiosInstance from '../axiosInstance'
import type { SigninResponse, SignupResponse, LogoutResponse, FindUserIdResponse } from '../../types/auth'

export const authApi = {
  signin: (email: string, password: string) =>
    axiosInstance.post<SigninResponse>('/v1/auth/signin', { email, password }),

  signup: (body: {
    email:       string
    phoneNumber: string
    memberName:  string
    password:    string
    name:        string
    age:         number
    sex:         string
  }) => axiosInstance.post<SignupResponse>('/v1/auth/signup', body),

  findUserId: (body: { name: string; phoneNumber: string }) =>
    axiosInstance.post<FindUserIdResponse>('/v1/auth/user-id', body),

  logout: () =>
    axiosInstance.post<LogoutResponse>('/v1/auth/logout'),
}

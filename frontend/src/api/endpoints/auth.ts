import axiosInstance from '../axiosInstance'
import type { SigninResponse, SignupResponse, LogoutResponse } from '../../types/auth'

export const authApi = {
  signin: (email: string, password: string) =>
    axiosInstance.post<SigninResponse>('/v1/auth/signin', { email, password }),

  signup: (body: {
    email:      string
    memberName: string
    password:   string
    name:       string
    age:        number
    sex:        string
  }) => axiosInstance.post<SignupResponse>('/v1/auth/signup', body),

  logout: () =>
    axiosInstance.post<LogoutResponse>('/v1/auth/logout'),
}

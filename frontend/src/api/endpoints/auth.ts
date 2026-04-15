import axiosInstance from '../axiosInstance'
import type { SigninResponse, SignupResponse, LogoutResponse, FindUserIdResponse, VerifyPhoneResponse, VerifyPhoneResultResponse } from '../../types/auth'

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

  verifyPhone: (body: { phoneNumber: string }) =>
    axiosInstance.post<VerifyPhoneResponse>('/v1/auth/phone-verifications', body),

  verifyPhoneResult: (body: { phoneNumber: string; verificationCode: string }) =>
    axiosInstance.post<VerifyPhoneResultResponse>('/v1/auth/phone-verifications-result', body),

  logout: () =>
    axiosInstance.post<LogoutResponse>('/v1/auth/logout'),
}

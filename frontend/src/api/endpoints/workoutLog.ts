import axiosInstance from '../axiosInstance'
import type { WorkOutLogListResponse, WorkOutLogDetailResponse } from '../../types/workoutLog'

export const workoutLogApi = {
  list: () =>
    axiosInstance.get<WorkOutLogListResponse>('/v1/workout-logs/list'),

  detail: (workOutLogId: number) =>
    axiosInstance.get<WorkOutLogDetailResponse>(`/v1/workout-logs/${workOutLogId}`),

  remove: (workOutLogId: number) =>
    axiosInstance.delete(`/v1/workout-logs/${workOutLogId}`),
}

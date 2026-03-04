import axiosInstance from '../axiosInstance'
import type { ExerciseListResponse } from '../../types/exercise'

export const exerciseApi = {
  list: () => axiosInstance.get<ExerciseListResponse>('/v1/exercises'),
}

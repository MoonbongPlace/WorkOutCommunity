import axiosInstance from '../axiosInstance'
import type { CategoryListResponse } from '../../types/category'

export const categoryApi = {
  list: () =>
    axiosInstance.get<CategoryListResponse>('/v1/categories'),
}

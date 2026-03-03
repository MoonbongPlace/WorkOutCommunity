// GET /api/v1/categories
export interface CategoryItem {
  id: number
  name: string
}

export interface CategoryListResult {
  categoryList: CategoryItem[]
}

export interface CategoryListResponse {
  code: string
  message: string
  timestamp: string
  categoryListResult: CategoryListResult
}

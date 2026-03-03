// GET /api/v1/workout-logs/list
export interface WorkoutLogSummary {
  id: number
  logDate: string   // LocalDate → "YYYY-MM-DD"
  createdAt: string
}

export interface WorkOutLogListResult {
  workOutLogList: WorkoutLogSummary[]
}

export interface WorkOutLogListResponse {
  code: string
  message: string
  timestamp: string
  workOutLogListResult: WorkOutLogListResult
}

// GET /api/v1/workout-logs/{id}
export interface WorkoutLogItem {
  id: number
  logId: number
  exerciseId: number
  exerciseName: string
  orderSeq: number
  plannedSets: number
  plannedReps: string
  plannedRpe: number | null
  plannedRestSec: number | null
  notes: string | null
}

export interface WorkOutLogDetailResult {
  id: number
  memberId: number
  logDate: string
  createdAt: string
  list: WorkoutLogItem[]
}

export interface WorkOutLogDetailResponse {
  code: string
  message: string
  timestamp: string
  workOutLogDetailResult: WorkOutLogDetailResult
}

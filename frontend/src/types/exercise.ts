export interface ExerciseSummary {
  id: number
  name: string
  bodyParts: string[]
}

export interface ExerciseListResponse {
  code: string
  message: string
  timestamp: string
  exercises: ExerciseSummary[]
}

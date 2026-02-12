/**
 * Error response from API
 * Matches ErrorResponse from backend
 */
export interface ErrorResponse {
  errorCode: string;
  message: string;
  timestamp: string;
}

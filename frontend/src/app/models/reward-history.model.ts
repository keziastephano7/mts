/**
 * Reward history entry
 * Matches RewardHistoryDTO from backend
 */
export interface RewardHistory {
  id: number;
  transactionId: string;
  pointsGranted: number;
  grantedOn: string;
}

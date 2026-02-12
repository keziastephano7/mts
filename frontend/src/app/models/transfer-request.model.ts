/**
 * Transfer request data
 * Matches TransferRequest from backend
 */
export interface TransferRequest {
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  idempotencyKey: string;
}

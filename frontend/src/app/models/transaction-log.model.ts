/**
 * Transaction log entry
 * Matches TransactionLog from backend
 */
export interface TransactionLog {
  id: string;
  fromAccountId: number;
  toAccountId: number;
  amount: number;
  status: string;
  failureReason?: string;
  idempotencyKey: string;
  createdOn: string;
}

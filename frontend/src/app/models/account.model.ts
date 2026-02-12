/**
 * Represents a bank account
 * Matches AccountResponse from backend
 */
export interface Account {
  id: number;
  holderName: string;
  balance: number;
  status: string;
}

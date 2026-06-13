import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RewardBalance } from '../models/reward-balance.model';
import { RewardHistory } from '../models/reward-history.model';

/**
 * Reward Service
 * Handles reward points API calls
 */
@Injectable({
  providedIn: 'root'
})
export class RewardService {

  private apiUrl = 'http://localhost:8080/api/v1/rewards';

  constructor(private http: HttpClient) {}

  /**
   * Get reward points balance for an account
   * @param accountId Account ID
   * @returns Observable of RewardBalance
   */
  getRewardBalance(accountId: number): Observable<RewardBalance> {
    return this.http.get<RewardBalance>(`${this.apiUrl}/${accountId}/balance`);
  }

  /**
   * Get reward history for an account
   * @param accountId Account ID
   * @returns Observable of RewardHistory array
   */
  getRewardHistory(accountId: number): Observable<RewardHistory[]> {
    return this.http.get<RewardHistory[]>(`${this.apiUrl}/${accountId}/history`);
  }
}

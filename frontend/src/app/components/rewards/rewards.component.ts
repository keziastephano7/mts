import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';

import { AuthService } from '../../services/auth.service';
import { RewardService } from '../../services/reward.service';
import { RewardBalance } from '../../models/reward-balance.model';
import { RewardHistory } from '../../models/reward-history.model';

/**
 * Rewards Component
 * Shows reward points balance and reward history
 */
@Component({
  selector: 'app-rewards',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatToolbarModule,
    MatTableModule
  ],
  templateUrl: './rewards.component.html',
  styleUrl: './rewards.component.css'
})
export class RewardsComponent implements OnInit {

  rewardBalance: RewardBalance | null = null;
  rewardHistory: RewardHistory[] = [];
  loading: boolean = true;
  error: string = '';
  username: string = '';

  displayedColumns: string[] = ['transactionId', 'pointsGranted', 'grantedOn'];

  constructor(
    private authService: AuthService,
    private rewardService: RewardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get username
    this.username = this.authService.getUsername() || 'User';

    // Load reward data
    this.loadRewardData();
  }

  /**
   * Load reward balance and history
   */
  loadRewardData(): void {
    const accountId = this.authService.getCurrentAccountId();

    if (!accountId) {
      this.error = 'No account ID found';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.error = '';

    // Load reward balance
    this.rewardService.getRewardBalance(accountId).subscribe({
      next: (balance) => {
        this.rewardBalance = balance;
        // Load reward history
        this.loadRewardHistory(accountId);
      },
      error: (error) => {
        console.error('Failed to load reward balance', error);
        this.error = 'Failed to load reward balance';
        this.loading = false;
      }
    });
  }

  /**
   * Load reward history
   */
  private loadRewardHistory(accountId: number): void {
    this.rewardService.getRewardHistory(accountId).subscribe({
      next: (history) => {
        this.rewardHistory = history;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load reward history', error);
        this.error = 'Failed to load reward history';
        this.loading = false;
      }
    });
  }

  /**
   * Refresh reward data
   */
  refresh(): void {
    this.loadRewardData();
  }

  /**
   * Navigate to dashboard
   */
  goToDashboard(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Navigate to transfer
   */
  goToTransfer(): void {
    this.router.navigate(['/transfer']);
  }

  /**
   * Navigate to history
   */
  goToHistory(): void {
    this.router.navigate(['/history']);
  }

  /**
   * Logout
   */
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  /**
   * Format date for display
   */
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString('en-IN');
  }
}

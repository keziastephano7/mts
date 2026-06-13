import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { RewardService } from '../../services/reward.service';
import { Account } from '../../models/account.model';
import { RewardBalance } from '../../models/reward-balance.model';

/**
 * Profile Component
 * Displays user profile with account details, balance, and rewards
 */
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    MatProgressSpinnerModule,
    RouterLink
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  account: Account | null = null;
  rewardBalance: RewardBalance | null = null;
  loading: boolean = true;
  error: string = '';
  username: string = '';

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private rewardService: RewardService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get username
    this.username = this.authService.getUsername() || 'User';

    // Load profile data
    this.loadProfileData();
  }

  /**
   * Load account and reward data
   */
  loadProfileData(): void {
    const accountId = this.authService.getCurrentAccountId();

    if (!accountId) {
      this.error = 'No account ID found';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.error = '';

    // Load account details
    this.accountService.getAccount(accountId).subscribe({
      next: (account) => {
        this.account = account;
        // Load reward balance
        this.loadRewardBalance(accountId);
      },
      error: (error) => {
        console.error('Failed to load account', error);
        this.error = 'Failed to load account details';
        this.loading = false;
      }
    });
  }

  /**
   * Load reward balance
   */
  private loadRewardBalance(accountId: number): void {
    this.rewardService.getRewardBalance(accountId).subscribe({
      next: (balance) => {
        this.rewardBalance = balance;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load reward balance', error);
        // Don't fail the whole page if rewards fail
        this.loading = false;
      }
    });
  }

  /**
   * Go back to dashboard
   */
  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Refresh profile data
   */
  refresh(): void {
    this.loadProfileData();
  }
}

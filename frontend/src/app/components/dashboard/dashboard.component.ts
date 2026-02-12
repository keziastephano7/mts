import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatToolbarModule } from '@angular/material/toolbar';

import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { Account } from '../../models/account.model';

/**
 * Dashboard Component
 * Shows account overview and navigation
 */
@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatToolbarModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {

  account: Account | null = null;
  loading: boolean = true;
  error: string = '';
  username: string = '';

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get username
    this.username = this.authService.getUsername() || 'User';

    // Load account data
    this.loadAccount();
  }

  /**
   * Load account details
   */
  loadAccount(): void {
    const accountId = this.authService.getCurrentAccountId();

    if (!accountId) {
      this.error = 'No account ID found';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.accountService.getAccount(accountId).subscribe({
      next: (account) => {
        this.account = account;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load account', error);
        this.error = 'Failed to load account details';
        this.loading = false;
      }
    });
  }

  /**
   * Navigate to transfer page
   */
  goToTransfer(): void {
    this.router.navigate(['/transfer']);
  }

  /**
   * Navigate to history page
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
   * Refresh account data
   */
  refresh(): void {
    this.loadAccount();
  }
}

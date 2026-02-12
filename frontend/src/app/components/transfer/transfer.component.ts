import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { TransferService } from '../../services/transfer.service';
import { TransferRequest } from '../../models/transfer-request.model';
import { Account } from '../../models/account.model';

/**
 * Transfer Component
 * Handles money transfer between accounts
 */
@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.css'
})
export class TransferComponent implements OnInit {

  // Form fields
  toAccountId: number | null = null;
  amount: number | null = null;
  notes: string = '';

  // Account data
  fromAccount: Account | null = null;

  // Loading states
  loading: boolean = false;
  loadingAccount: boolean = true;

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private transferService: TransferService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadAccount();
  }

  /**
   * Load current account details
   */
  loadAccount(): void {
    const accountId = this.authService.getCurrentAccountId();

    if (!accountId) {
      this.showError('No account found');
      this.router.navigate(['/dashboard']);
      return;
    }

    this.accountService.getAccount(accountId).subscribe({
      next: (account) => {
        this.fromAccount = account;
        this.loadingAccount = false;
      },
      error: (error) => {
        console.error('Failed to load account', error);
        this.showError('Failed to load account details');
        this.loadingAccount = false;
      }
    });
  }

  /**
   * Handle transfer form submission
   */
  onTransfer(): void {
    // Validate inputs
    if (!this.validateForm()) {
      return;
    }

    if (!this.fromAccount) {
      this.showError('Account not loaded');
      return;
    }

    // Create transfer request
    const request: TransferRequest = {
      fromAccountId: this.fromAccount.id,
      toAccountId: this.toAccountId!,
      amount: this.amount!,
      idempotencyKey: this.transferService.generateIdempotencyKey()
    };

    this.loading = true;

    // Execute transfer
    this.transferService.transfer(request).subscribe({
      next: (response) => {
        console.log('Transfer successful', response);
        this.showSuccess(response.message);

        // Navigate to dashboard after 2 seconds
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 2000);
      },
      error: (error) => {
        console.error('Transfer failed', error);
        this.loading = false;

        // Display error message
        const errorMessage = error.error?.message || 'Transfer failed. Please try again.';
        this.showError(errorMessage);
      }
    });
  }

  /**
   * Validate form inputs
   */
  validateForm(): boolean {
    if (!this.toAccountId) {
      this.showError('Please enter destination account ID');
      return false;
    }

    if (!this.amount || this.amount <= 0) {
      this.showError('Please enter a valid amount');
      return false;
    }

    if (this.fromAccount && this.toAccountId === this.fromAccount.id) {
      this.showError('Cannot transfer to the same account');
      return false;
    }

    if (this.fromAccount && this.amount > this.fromAccount.balance) {
      this.showError('Insufficient balance');
      return false;
    }

    return true;
  }

  /**
   * Cancel and go back to dashboard
   */
  cancel(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Go back to dashboard
   */
  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Show success message
   */
  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['success-snackbar']
    });
  }

  /**
   * Show error message
   */
  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}

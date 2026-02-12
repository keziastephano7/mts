import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../services/auth.service';
import { AccountService } from '../../services/account.service';
import { TransactionLog } from '../../models/transaction-log.model';

/**
 * History Component
 * Displays transaction history
 */
@Component({
  selector: 'app-history',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatChipsModule,
    MatToolbarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css'
})
export class HistoryComponent implements OnInit {

  transactions: TransactionLog[] = [];
  loading: boolean = true;
  error: string = '';
  currentAccountId: number | null = null;

  // Table columns
  displayedColumns: string[] = ['date', 'type', 'account', 'amount', 'status'];

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentAccountId = this.authService.getCurrentAccountId();
    this.loadTransactions();
  }

  /**
   * Load transaction history
   */
  loadTransactions(): void {
    if (!this.currentAccountId) {
      this.error = 'No account ID found';
      this.loading = false;
      return;
    }

    this.loading = true;
    this.accountService.getTransactions(this.currentAccountId).subscribe({
      next: (transactions) => {
        this.transactions = transactions.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load transactions', error);
        this.error = 'Failed to load transaction history';
        this.loading = false;
      }
    });
  }

  /**
   * Determine transaction type (SENT or RECEIVED)
   */
  getTransactionType(transaction: TransactionLog): string {
    return transaction.fromAccountId === this.currentAccountId ? 'SENT' : 'RECEIVED';
  }

  /**
   * Get other account ID
   */
  getOtherAccountId(transaction: TransactionLog): number {
    return transaction.fromAccountId === this.currentAccountId
      ? transaction.toAccountId
      : transaction.fromAccountId;
  }

  /**
   * Format amount with sign
   */
  getFormattedAmount(transaction: TransactionLog): string {
    const amount = transaction.amount;
    const isSent = transaction.fromAccountId === this.currentAccountId;
    return isSent ? `-₹${amount}` : `+₹${amount}`;
  }

  /**
   * Get amount color class
   */
  getAmountClass(transaction: TransactionLog): string {
    return transaction.fromAccountId === this.currentAccountId ? 'debit' : 'credit';
  }

  /**
   * Format date
   */
  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Go back to dashboard
   */
  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Refresh transactions
   */
  refresh(): void {
    this.loadTransactions();
  }
}

import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

export interface PaymentResultData {
  success: boolean;
  message: string;
  transactionId?: string;
  amount?: number;
  rewardPointsEarned?: number;
}

@Component({
  selector: 'app-payment-result-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="dialog-container" [class.success]="data.success" [class.failure]="!data.success">
      <div class="icon-container">
        <mat-icon class="result-icon" [class.success-icon]="data.success" [class.error-icon]="!data.success">
          {{ data.success ? 'check_circle' : 'error' }}
        </mat-icon>
      </div>

      <h2 mat-dialog-title class="dialog-title">
        {{ data.success ? 'Transfer Successful' : 'Transfer Failed' }}
      </h2>

      <mat-dialog-content class="dialog-content">
        <p class="message">{{ data.message }}</p>
        
        <div *ngIf="data.success && data.transactionId" class="transaction-details">
          <div class="detail-row">
            <span class="label">Transaction ID:</span>
            <span class="value">{{ data.transactionId }}</span>
          </div>
          <div class="detail-row">
            <span class="label">Amount Transferred:</span>
            <span class="value">₹{{ data.amount | number:'1.2-2' }}</span>
          </div>
          <div *ngIf="data.rewardPointsEarned && data.rewardPointsEarned > 0" class="reward-row">
            <span class="label reward-label">
              <mat-icon class="reward-icon">card_giftcard</mat-icon>
              Reward Points Earned:
            </span>
            <span class="value reward-value">+{{ data.rewardPointsEarned }} points</span>
          </div>
        </div>
      </mat-dialog-content>

      <mat-dialog-actions align="end" class="dialog-actions">
        <button *ngIf="data.success && data.rewardPointsEarned && data.rewardPointsEarned > 0" mat-stroked-button (click)="onViewRewards()">
          <mat-icon>card_giftcard</mat-icon>
          View Rewards
        </button>
        <button mat-raised-button (click)="onClose()">
          {{ data.success ? 'Continue' : 'Try Again' }}
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-container {
      padding: 20px;
      text-align: center;
    }

    .icon-container {
      margin-bottom: 20px;
    }

    .result-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
    }

    .success-icon {
      color: #4caf50;
    }

    .error-icon {
      color: #f44336;
    }

    .dialog-title {
      margin: 15px 0;
      font-size: 24px;
      font-weight: 600;
    }

    .dialog-content {
      margin: 20px 0;
    }

    .message {
      font-size: 16px;
      color: #666;
      margin: 0 0 20px 0;
    }

    .transaction-details {
      background: #f5f5f5;
      padding: 15px;
      border-radius: 8px;
      margin: 20px 0;
    }

    .detail-row {
      display: flex;
      justify-content: space-between;
      padding: 8px 0;
      border-bottom: 1px solid #ddd;
    }

    .detail-row:last-child {
      border-bottom: none;
    }

    .label {
      font-weight: 500;
      color: #666;
    }

    .value {
      font-weight: 600;
      color: #333;
    }

    .dialog-actions {
      margin-top: 25px;
      display: flex;
      justify-content: center;
    }

    .dialog-actions button {
      min-width: 120px;
    }

    .reward-row {
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      padding: 12px;
      border-radius: 6px;
      margin-top: 12px;
    }

    .reward-label {
      display: flex;
      align-items: center;
      gap: 6px;
      color: white !important;
      font-weight: 600;
    }

    .reward-icon {
      font-size: 18px;
      width: 18px;
      height: 18px;
    }

    .reward-value {
      color: white !important;
      font-weight: bold;
      font-size: 16px;
    }
  `]
})
export class PaymentResultDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<PaymentResultDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PaymentResultData,
    private router: Router
  ) {}

  onClose(): void {
    this.dialogRef.close();
    if (this.data.success) {
      this.router.navigate(['/dashboard']);
    }
  }

  onViewRewards(): void {
    this.dialogRef.close();
    this.router.navigate(['/rewards']);
  }
}

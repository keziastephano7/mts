import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

export interface RewardGainedData {
  pointsEarned: number;
  totalBalance: number;
  amount: number;
}

@Component({
  selector: 'app-reward-gained-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButtonModule, MatIconModule],
  template: `
    <div class="dialog-container">
      <div class="celebration-icon">
        <mat-icon>celebration</mat-icon>
      </div>

      <h2 mat-dialog-title class="dialog-title">
        Congratulations! 🎉
      </h2>

      <mat-dialog-content class="dialog-content">
        <p class="subtitle">You earned reward points!</p>
        
        <div class="points-earned">
          <span class="points-value">+{{ data.pointsEarned }}</span>
          <span class="points-label">points</span>
        </div>

        <div class="reward-details">
          <div class="detail-item">
            <span class="label">Transfer Amount:</span>
            <span class="value">₹{{ data.amount | number:'1.2-2' }}</span>
          </div>
          <div class="detail-item">
            <span class="label">Total Reward Points:</span>
            <span class="value">{{ data.totalBalance }}</span>
          </div>
        </div>

        <p class="reward-info">
          You earned <strong>{{ data.pointsEarned }} point{{ data.pointsEarned !== 1 ? 's' : '' }}</strong> 
          for this transfer! Keep transferring to earn more rewards.
        </p>
      </mat-dialog-content>

      <mat-dialog-actions align="center" class="dialog-actions">
        <button mat-stroked-button (click)="onClose()">
          <mat-icon>close</mat-icon>
          Close
        </button>
        <button mat-raised-button color="primary" (click)="goToRewards()">
          <mat-icon>card_giftcard</mat-icon>
          View Rewards
        </button>
      </mat-dialog-actions>
    </div>
  `,
  styles: [`
    .dialog-container {
      padding: 30px 20px;
      text-align: center;
    }

    .celebration-icon {
      margin-bottom: 20px;
    }

    .celebration-icon mat-icon {
      font-size: 64px;
      width: 64px;
      height: 64px;
      color: #f093fb;
      animation: bounce 0.6s ease-in-out;
    }

    @keyframes bounce {
      0%, 100% { transform: translateY(0); }
      50% { transform: translateY(-20px); }
    }

    .dialog-title {
      margin: 15px 0;
      font-size: 28px;
      font-weight: 600;
      color: #333;
    }

    .dialog-content {
      margin: 20px 0;
    }

    .subtitle {
      font-size: 16px;
      color: #666;
      margin: 0 0 25px 0;
    }

    .points-earned {
      display: flex;
      align-items: baseline;
      justify-content: center;
      gap: 12px;
      margin: 25px 0;
      padding: 20px;
      background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      border-radius: 12px;
      color: white;
    }

    .points-value {
      font-size: 48px;
      font-weight: bold;
    }

    .points-label {
      font-size: 18px;
      opacity: 0.9;
    }

    .reward-details {
      background: #f5f5f5;
      padding: 15px;
      border-radius: 8px;
      margin: 20px 0;
    }

    .detail-item {
      display: flex;
      justify-content: space-between;
      padding: 10px 0;
      border-bottom: 1px solid #ddd;
    }

    .detail-item:last-child {
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

    .reward-info {
      font-size: 14px;
      color: #666;
      margin: 20px 0 0 0;
      line-height: 1.5;
    }

    .dialog-actions {
      margin-top: 30px;
      display: flex;
      gap: 12px;
      justify-content: center;
    }

    .dialog-actions button {
      min-width: 140px;
      height: 44px;
    }
  `]
})
export class RewardGainedDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<RewardGainedDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: RewardGainedData,
    private router: Router
  ) {}

  onClose(): void {
    this.dialogRef.close();
  }

  goToRewards(): void {
    this.dialogRef.close();
    this.router.navigate(['/rewards']);
  }
}

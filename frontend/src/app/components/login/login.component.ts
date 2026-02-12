import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthService } from '../../services/auth.service';

/**
 * Login Component
 * Handles user authentication
 */
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  // Form fields
  username: string = '';
  password: string = '';

  // Loading state
  loading: boolean = false;

  // Password visibility toggle
  hidePassword: boolean = true;

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    // If already logged in, redirect to dashboard
    if (this.authService.isAuthenticated()) {
      this.router.navigate(['/dashboard']);
    }
  }

  onLogin(): void {
    // Validate inputs
    if (!this.username || !this.password) {
      this.showError('Please enter both username and password');
      return;
    }

    this.loading = true;

    // Call auth service --- calls java backend
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        // Login successful
        console.log('Login successful', response);

        // Store account ID (assuming we get account 1 for simplicity)
        // In real app, backend would return the user's account ID
        this.authService.setCurrentAccountId(1);

        // Show success message
        this.showSuccess('Login successful!');

        // Navigate to dashboard
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        // Login failed
        console.error('Login failed', error);
        this.loading = false;

        if (error.status === 401) {
          this.showError('Invalid username or password');
        } else {
          this.showError('Login failed. Please try again.');
        }
      },
      complete: () => {
        this.loading = false;
      }
    });
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}

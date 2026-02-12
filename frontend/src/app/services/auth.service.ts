import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'  // Available throughout the app
})
export class AuthService {

  // API base URL - points to our Spring Boot backend
  private apiUrl = 'http://localhost:8080/api/v1';

  // BehaviorSubject to track login state
  // Other components can subscribe to this
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  // Observable that components can subscribe to
  public isLoggedIn$ = this.loggedIn.asObservable();

  constructor(private http: HttpClient) {}


  login(username: string, password: string): Observable<any> {
    // Create Basic Auth header
    const credentials = btoa(`${username}:${password}`);  // Base64 encode
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    // Test authentication by calling account endpoint
    // If successful, we know credentials are valid
    return this.http.get(`${this.apiUrl}/accounts/1`, { headers }).pipe(
      tap(() => {
        // Store credentials in localStorage
        localStorage.setItem('authToken', credentials);
        localStorage.setItem('username', username);

        // Update login state
        this.loggedIn.next(true);
      })
    );
  }

  logout(): void {
    // Clear stored credentials
    localStorage.removeItem('authToken');
    localStorage.removeItem('username');
    localStorage.removeItem('currentAccountId');

    // Update login state
    this.loggedIn.next(false);
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('authToken');
  }

  isAuthenticated(): boolean {
    return this.hasToken();
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }

  setCurrentAccountId(accountId: number): void {
    localStorage.setItem('currentAccountId', accountId.toString());
  }

  getCurrentAccountId(): number | null {
    const id = localStorage.getItem('currentAccountId');
    return id ? parseInt(id, 10) : null;
  }
}

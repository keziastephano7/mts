import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../models/account.model';
import { TransactionLog } from '../models/transaction-log.model';

@Injectable({
  providedIn: 'root'
})

export class AccountService {

  private apiUrl = 'http://localhost:8080/api/v1/accounts';

  constructor(private http: HttpClient) {}

  getAccount(accountId: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${accountId}`);
  }

  getBalance(accountId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${accountId}/balance`);
  }

  getTransactions(accountId: number): Observable<TransactionLog[]> {
    return this.http.get<TransactionLog[]>(`${this.apiUrl}/${accountId}/transactions`);
  }
}

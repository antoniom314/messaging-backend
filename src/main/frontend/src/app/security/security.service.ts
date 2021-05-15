import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Credentionals as Credentials } from '../login/credentials';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private getSignupUrl = 'http://user:pass@localhost:8080/api/signup';
  private getLoginUrl = 'http://user:pass@localhost:8080/api/login';
  private getUsersUrl = 'http://localhost:8080/api/get-users';
  private getRoleUserUrl = 'http://localhost:8080/api/get-role-user';
  private getRoleAdminUrl = 'http://localhost:8080/api/get-role-admin';


  constructor(private http: HttpClient) { }

  public register(credentials: Credentials): Observable<any> {

    const headers = new HttpHeaders({
            'Authorization': 'Basic ' + btoa(credentials.username + ':' + credentials.password),
            // 'Authorization': 'Basic ' + credentials.username + ':' + credentials.password,
            'Content-Type': 'application/json',
            //withCredentials: 'true',
            observe: 'response'
            //  'Accept': 'text/plain',
            // 'Content-Type': 'text/plain',
            //  'responseType': 'text'
            });
    return this.http.get<any>(this.getSignupUrl, {headers});
  }

  public authenticate(credentials: Credentials): Observable<any> {

    const headers = new HttpHeaders({
            'Authorization': 'Basic ' + btoa(credentials.username + ':' + credentials.password),
            'Content-Type': 'application/json',
            //withCredentials: 'true',
            observe: 'response'
            //  'Accept': 'text/plain',
            // 'Content-Type': 'text/plain',
            //  'responseType': 'text'
            });

    // headers.set('Authorization', 'Basic ' + btoa('user' + ':' + 'user')) ;
    // headers.set('Content-Type', 'application/json') ;

    return this.http.get<any>(this.getLoginUrl, {headers});
  }

  public getMessage(): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'withCredentials': 'true',
       observe: 'response', });

    return this.http.get<any>(this.getUsersUrl);
  }

  public getRoleUser(): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'withCredentials': 'true',
       observe: 'response', });

    return this.http.get<any>(this.getRoleUserUrl, {headers});
  }

  public getRoleAdmin(): Observable<any> {

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'withCredentials': 'true',
       observe: 'response', });

    return this.http.get<any>(this.getRoleAdminUrl, {headers});
  }
}


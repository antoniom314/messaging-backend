import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Credentials } from '../../components/login/credentials';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {

  private rootUrl = 'http://localhost:8080/api';
  private registerUrl = this.rootUrl + '/register';
  private authenticateUrl = this.rootUrl + '/authenticate';

  constructor(private http: HttpClient) { }

  public register(credentials: Credentials): Observable<any> {

    const headers = new HttpHeaders({
            Authentication: 'Basic ' + btoa(credentials.username + ':' + credentials.password),
            'Content-Type': 'application/json',
            observe: 'response',
            });

    return this.http.get<any>(this.registerUrl, {headers});
  }

  public authenticate(credentials: Credentials): Observable<any> {

    const headers = new HttpHeaders({
            Authentication: 'Basic ' + btoa(credentials.username + ':' + credentials.password),
           'Content-Type': 'application/json',
            observe: 'response',
            });

    return this.http.get<any>(this.authenticateUrl, {headers});
  }
}

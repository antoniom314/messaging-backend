import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private rootUrl = 'http://localhost:8080/api';
  private usersUrl = this.rootUrl + '/get-users';

  constructor(private http: HttpClient) { }

  public getUsers(): Observable<any> {

    const headers = new HttpHeaders({
           'Content-Type': 'application/json',
            observe: 'response'
            });

    return this.http.get<any>(this.usersUrl, {headers});
  }

}

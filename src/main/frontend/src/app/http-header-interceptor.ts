import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Properties } from './properties';


@Injectable()
export class HttpHeaderInterseptor implements HttpInterceptor {

  constructor(private router: Router) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    // Get JWT token from local storage
    const authToken = localStorage.getItem(Properties.STORAGE_NAME);

    if (authToken) {
      // Add JWT token to 'Authorization' request header
      request = request.clone({setHeaders: {Authorization: authToken}});

      // Navigate to Users component
      // this.router.navigate(['/users']);

    } else {
      // Navigate to LogIn component
      // this.router.navigate(['/login']);
    }

    return next.handle(request).pipe(

      tap(response => {

        if (response instanceof HttpResponse) {
          // Get JWT token from response
          const token = response.headers.get('Authorization');

          if (token) {
            // Store JWT token in local storage
            localStorage.setItem(Properties.STORAGE_NAME, token);
          }
        }
      })
    );
  }
}

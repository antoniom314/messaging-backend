import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";
import { Properties } from "./properties";


@Injectable()
export class HttpHeaderInterseptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    console.log('HttpInterceptor');

    console.log('request');
    console.log(request.headers);

    // console.log(request.headers.getAll('Content-Type'));
    const token = localStorage.getItem(Properties.STORAGE_NAME);
    console.log('token= ' + token);

    // Add JWT token to request header
    if (token) {
      request = request.clone({
        setHeaders: {Authorization: 'Bearer ' + token}
      });
    }

    return next.handle(request).pipe(
      tap(response => {

        console.log('response');

        if (response instanceof HttpResponse) {

          const authorization = response.headers.get('Authorization');

          console.log(authorization);
          console.log(response.headers);

          if (authorization) {
            localStorage.setItem(Properties.STORAGE_NAME, authorization);
          }
        }
      })
    );
  }
}

import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, NavigationExtras, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Properties } from './properties';

@Injectable()
export class AccessGuard  implements CanActivate{

  constructor(private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot)
  : boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {

    const url = route.data.goBackUrl;
    console.log('canActivate()');
    console.log(route.data);
    console.log(url);
    console.log(localStorage.getItem(Properties.STORAGE_NAME));

    if (localStorage.getItem(Properties.STORAGE_NAME)) {
      // logged in so return true
      return true;
    }

    const extras: NavigationExtras = {
      queryParams: {
        goBackUrl: url
      }
    };
    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login', extras]);
    return false;
  }


  // canActivate(route: ActivatedRouteSnapshot)
  // : Observable<boolean>|Promise<boolean>|boolean {
  //   const requiresLogin = route.data.requiresLogin || false;
  //   if (requiresLogin) {
  //     // Check that the user is logged in...
  //   }
  // }



}


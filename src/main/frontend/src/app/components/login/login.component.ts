import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import jwtDecode from 'jwt-decode';
import { Properties } from 'src/app/properties';
import { SecurityService } from '../../services/security/security.service';
import { Credentials } from './credentials';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  public credentionals: Credentials;
  public message = '';
  public role = '';

  private goBackUrl = '';

  constructor(private securityService: SecurityService,
              private router: Router,
              private activeRoute: ActivatedRoute
  ) {
    this.credentionals = new Credentials();
  }

  ngOnInit(): void {

    // If user is loged in continue to Users Component
    const token = localStorage.getItem(Properties.STORAGE_NAME);
    if (token) {
      const decoded: string = jwtDecode(token);
      if (decoded) {
        this.router.navigate(['users']);
      }
    }
    // Get parametars from Users component
    this.activeRoute.queryParams.subscribe((params) => {
      this.goBackUrl = params.goBackUrl;
    });
  }

  // Register user
  public register(): void {
    this.securityService.register(this.credentionals).subscribe({
      next: (response) => {
        console.log(response);
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  // Login user
  public authenticate(): void {
    this.securityService.authenticate(this.credentionals).subscribe({
      next: (response) => {
        console.log('authenticate() ', response);
        const token = localStorage.getItem(Properties.STORAGE_NAME);

        if (token) {
          const decoded: string = jwtDecode(token);

          if (decoded) {
            this.router.navigate(['users']);
          }
        }
      },
      error: (error) => {
        console.log(error);
      },
    });
  }

  // Logout user
  public deleteToken(): void {
    localStorage.setItem(Properties.STORAGE_NAME, '');
  }
}

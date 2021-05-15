import { Component, Input, OnInit } from '@angular/core';
import { Properties } from '../properties';
import { SecurityService } from '../security/security.service';
import { Credentionals } from './credentials';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public credentionals: Credentionals;
  public message = '';
  public role = '';

  constructor(private securityService: SecurityService) {
    this.credentionals = new Credentionals();
  }

  ngOnInit(): void {
  }

  public getSignup() {
    this.securityService.register(this.credentionals).subscribe({

      next: response => {
        console.log('signup() next response= ' + response);
        console.log(response);
        if (response) {
          console.log('signup() next response headers= ' + response.headers);
          console.log('signup() next response jwt= ' + response.jwt);
        }
      },
      error: error => {
        console.log('signup() next error')
        console.log(error)
      }
    });
  }

  public getLogin(): void {
    this.securityService.authenticate(this.credentionals).subscribe({

      next: response => {
        console.log('login() next response= ' + response);
        console.log(response);
        if (response) {
          console.log('login() next response headers= ' + response.headers);
          console.log('login() next response jwt= ' + response.jwt);
        }
      },
      error: error => {
        console.log('login() next error')
        console.log(error)
      }
    });
  }

  public getMessage(): void {

    console.log('getMessage()');
    this.securityService.getMessage().subscribe({

      next: data => {
        console.log('getMessage() data= ' + data),
        this.message = JSON.stringify(data);
      },
      error: error => console.log(error)
    });

    //this.message = 'message';
  }

  public getRoleUser(): void {

    this.message = '';
    console.log('getRoleUser');
    this.securityService.getRoleUser().subscribe({

      next: data => {
        console.log(data),
        this.message = JSON.stringify(data);
      },
      error: error => console.log(error)
    });
  }

  public getRoleAdmin(): void {

    this.message = '';
    console.log('getAdminUser');
    this.securityService.getRoleAdmin().subscribe({

      next: data => {
        console.log(data),
        this.message = JSON.stringify(data);
      },
      error: error => console.log(error)
    });
  }
}

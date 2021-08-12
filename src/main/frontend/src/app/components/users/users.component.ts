import { Component, OnInit } from '@angular/core';
import { NavigationExtras, Router } from '@angular/router';
import jwtDecode from 'jwt-decode';
import { Properties } from '../../properties';
import { UsersService } from '../../services/users/users.service';
import { User } from './user';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {

public users: User[] = [];
public logedInUser = '';

constructor(private usersService: UsersService,
            private router: Router) {}

  ngOnInit(): void {
    this.logedInUser = this.getUsernameFromToken();
    this.getUserList();
  }

  public getUserList(): void {

    this.usersService.getUsers().subscribe({

      next: data =>  this.users = data,
      error: error => console.log(error)
    });
  }

  // Connect to selected user
  public connectToUser(selectedUser: string): void {

    const extras: NavigationExtras = {
      queryParams: {
        username: this.logedInUser,
        selected: selectedUser
      }
    };
    // Go to Chat component and pass username and selected user
    this.router.navigate(['/chat'], extras);
  }

  public getUsernameFromToken(): string {

    const token = localStorage.getItem(Properties.STORAGE_NAME);
    if (token) {
      const decoded: string = jwtDecode(token);

      return decoded.sub.toString();
    } else {
      console.log('Cannot get token');
    }
    return '';
  }
 }

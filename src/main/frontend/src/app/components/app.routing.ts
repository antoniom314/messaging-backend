import { Routes } from '@angular/router';
import { AccessGuard } from '../access-guard';
import { ChatComponent } from './chat/chat.component';
import { LoginComponent } from './login/login.component';
import { UsersComponent } from './users/users.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent},
  { path: 'chat', component: ChatComponent, data: { goBackUrl: 'chat' }, canActivate: [ AccessGuard ] },
  { path: 'users', component: UsersComponent, data: { goBackUrl: 'users' }, canActivate: [ AccessGuard ]},
  {path: '', redirectTo: 'users', pathMatch: 'full'},
  // { path: '**', component: PageNotFoundComponent}
];

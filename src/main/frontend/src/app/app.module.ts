import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { routes } from './components/app.routing';
import { AppComponent } from './components/app.component';
import { ChatComponent } from './components/chat/chat.component';
import { LoginComponent } from './components/login/login.component';
import { UsersComponent } from './components/users/users.component';
import { SecurityService } from './services/security/security.service';
import { UsersService } from './services/users/users.service';
import { HttpHeaderInterseptor } from './http-header-interceptor';
import { AccessGuard } from './access-guard';

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    LoginComponent,
    UsersComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(routes),
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [UsersService, SecurityService, AccessGuard,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpHeaderInterseptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})

export class AppModule { }

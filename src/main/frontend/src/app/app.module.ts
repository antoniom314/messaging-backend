import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ChatComponent } from './chat/chat.component';
import { HttpHeaderInterseptor } from './http.header.interceptor';
import { LoginComponent } from './login/login.component';
import { SecurityService } from './security/security.service';

@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [SecurityService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpHeaderInterseptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})

export class AppModule { }

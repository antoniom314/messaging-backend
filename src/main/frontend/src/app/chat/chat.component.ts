import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { StompConfig, StompService, StompState } from '@stomp/ng2-stompjs';
import { Message } from '@stomp/stompjs';
import { Observable } from 'rxjs';

const WEBSOCKET_URL = 'ws://localhost:8080/socket';
const STREAM_URL = '/queue/server-receiver';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {

  public state = 'NOT_CONNECTED';
  public user = '';
  public messagesHistory: string[] = [];

  private messages: Observable<Message>;
  private stompService: StompService;

  constructor() {
    const stompConfig: StompConfig = {
      url: WEBSOCKET_URL,
      headers: {login: '', passcode: ''},
      heartbeat_in: 0,
      heartbeat_out: 20000,
      reconnect_delay: 10000,
      debug: true
    };
    // Create Stomp Service
    this.stompService = new StompService(stompConfig);
    // Connect to a stream of messages
    this.messages = this.stompService.subscribe('/user/queue');
    // Subscribe to its stream (to listen on messages)
    this.messages.subscribe((message: Message) => {
      this.messagesHistory.unshift(message.body);
      console.log(message);
    });
    // Subscribe to its state (to know its connected or not)
    this.stompService.state.subscribe((state: StompState) => {
      this.state = StompState[state];
    });
  }

  // Use this methods to send message back to server
  public sendMessage(messageForm: NgForm): void {
    console.log('sendMessage', messageForm.value);
    const url = STREAM_URL;
    const message = {
      user: this.user,
      message: messageForm.value.message
    };

    this.stompService.publish(url, JSON.stringify(message));
    // Reset message input
    // messageForm.controls.message.reset();
  }



  public addUser(userForm: NgForm): void {
    console.log('addUser', userForm.value);
  }

  ngOnInit(): void {
    // this.chatService.connect();
  }

  connect(): void {
    // this.chatService.connect();
  }

  disconnect(): void {
    // this.chatService.disconnect();
  }
}

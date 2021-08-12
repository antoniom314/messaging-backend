import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { StompConfig, StompService, StompState } from '@stomp/ng2-stompjs';
import { Message } from '@stomp/stompjs';
import { Observable } from 'rxjs';
import { Properties } from '../../properties';
import { ChatMessage } from './chat.message';

const WEBSOCKET_URL = 'ws://localhost:8080/socket';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {

  public username = '';
  public selectedUser = '';

  public state = 'NOT_CONNECTED';
  public messagesHistory: ChatMessage[] = [];

  private observableMessage!: Observable<Message>;
  private stompService!: StompService;

  constructor(private activeRoute: ActivatedRoute,
              private router: Router) { }

  ngOnInit(): void {

    // Get parametars from Users component
    this.activeRoute.queryParams.subscribe(params => {
      this.username = params.username;
      this.selectedUser = params.selected;
    });
    // Get JWT token
    const token = localStorage.getItem(Properties.STORAGE_NAME);

    const stompConfig: StompConfig = {
      url: WEBSOCKET_URL,
      headers: {Authorization: '' + token},
      heartbeat_in: 0,
      heartbeat_out: 20000,
      reconnect_delay: 10000,
      debug: true
    };
    // Create Stomp Service
    this.stompService = new StompService(stompConfig);

    // Connect to STOMP service and get observable message
    this.observableMessage = this.stompService.subscribe(
            '/user/' + this.username + '/subscribe');

    // Subscribe to its stream (to listen on messages)
    this.observableMessage.subscribe((message: Message) => {
      const chatMessage: ChatMessage = JSON.parse(message.body);
      chatMessage.receved = true;
      this.messagesHistory.push(chatMessage);
    });
    // Subscribe to its state (to know its connected or not)
    this.stompService.state.subscribe((state: StompState) => {
      this.state = StompState[state];
    });
  }
  // Send message to server
  public sendMessage(messageForm: NgForm): void {

    const message = {
      userFrom: this.username,
      userTo: this.selectedUser,
      message: messageForm.value.message,
      receved: false
    };

    this.messagesHistory.push(message);

    this.stompService.publish('/app/send', JSON.stringify(message) );
    // Reset message input
    messageForm.controls.message.reset();
  }

  ngOnDestroy(): void {
    this.stompService.disconnect();
  }

  public goBack(): void {

    this.router.navigate(['users']);
  }
}

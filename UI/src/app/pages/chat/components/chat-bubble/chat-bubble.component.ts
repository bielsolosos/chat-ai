import { Component, input } from '@angular/core';
import { ChatMessage } from '../../utils/chat.types';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-chat-bubble',
  standalone: true,
  imports: [NgClass],
  template: `
    <div class="chat" [ngClass]="{'chat-end': message().role === 'user', 'chat-start': message().role === 'bot'}">
      <div class="chat-header text-xs opacity-50 mb-1">
        {{ message().role === 'user' ? 'Você' : 'Chat-AI' }}
      </div>
      <div class="chat-bubble shadow-sm" [ngClass]="{'bg-base-300 text-base-content': message().role === 'bot', 'bg-neutral text-neutral-content': message().role === 'user'}">
        {{ message().content }}
      </div>
    </div>
  `
})
export class ChatBubbleComponent {
  message = input.required<ChatMessage>();
}

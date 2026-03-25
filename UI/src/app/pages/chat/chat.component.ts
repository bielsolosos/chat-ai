import { Component, effect, ElementRef, inject, signal, ViewChild, OnInit } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { ChatBubbleComponent } from './components/chat-bubble/chat-bubble.component';
import { ChatInputComponent } from './components/chat-input/chat-input.component';
import { ChatService } from './services/chat.service';
import { ChatMessage } from './utils/chat.types';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [ChatBubbleComponent, ChatInputComponent],
  template: `
    <div class="drawer drawer-end bg-base-100 h-screen" [class.lg:drawer-open]="isSidebarOpen()">
      <input id="chat-drawer" type="checkbox" class="drawer-toggle" [checked]="isSidebarOpen()" (change)="toggleSidebar()" />
      
      <!-- Conteúdo Principal -->
      <div class="drawer-content flex flex-col h-full overflow-hidden">
        
        <!-- Header -->
        <div class="navbar bg-base-100 border-b border-base-200 shrink-0 px-4 flex justify-between">
          <div class="flex-1">
            <span class="text-xl font-semibold tracking-tight text-base-content">Assistente IA</span>
          </div>
          <div class="flex-none gap-2">
            <!-- Botão de Tema -->
            <button class="btn btn-ghost btn-circle" (click)="toggleTheme()" title="Alternar Tema">
              @if(currentTheme() === 'dark') {
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5"><path stroke-linecap="round" stroke-linejoin="round" d="M12 3v2.25m6.364.386l-1.591 1.591M21 12h-2.25m-.386 6.364l-1.591-1.591M12 18.75V21m-4.773-4.227l-1.591 1.591M5.25 12H3m4.227-4.773L5.636 5.636M15.75 12a3.75 3.75 0 11-7.5 0 3.75 3.75 0 017.5 0z" /></svg>
              } @else {
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5"><path stroke-linecap="round" stroke-linejoin="round" d="M21.752 15.002A9.718 9.718 0 0118 15.75c-5.385 0-9.75-4.365-9.75-9.75 0-1.33.266-2.597.748-3.752A9.753 9.753 0 003 11.25C3 16.635 7.365 21 12.75 21a9.753 9.753 0 009.002-5.998z" /></svg>
              }
            </button>
            
            <!-- Toggle Sidebar -->
            <label for="chat-drawer" class="btn btn-square btn-ghost" title="Alternar Histórico">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5" /></svg>
            </label>
          </div>
        </div>

        <!-- Área Central de Mensagens -->
        <div class="flex-1 overflow-y-auto px-4 py-8" #chatContainer>
          <div class="max-w-4xl mx-auto flex flex-col gap-6">
            
            @if(messages().length === 0) {
              <div class="flex flex-col items-center justify-center h-full min-h-[40vh] text-center opacity-40">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-16 h-16 mb-4"><path stroke-linecap="round" stroke-linejoin="round" d="M8.625 12a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H8.25m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0H12m4.125 0a.375.375 0 11-.75 0 .375.375 0 01.75 0zm0 0h-.375M21 12c0 4.556-4.03 8.25-9 8.25a9.764 9.764 0 01-2.555-.337A5.972 5.972 0 015.41 20.97a5.969 5.969 0 01-.474-.065 4.48 4.48 0 00.978-2.025c.09-.457-.133-.901-.467-1.226C3.93 16.178 3 14.189 3 12c0-4.556 4.03-8.25 9-8.25s9 3.694 9 8.25z" /></svg>
                <h2 class="text-xl font-medium">Como posso te ajudar hoje?</h2>
              </div>
            }

            @for (msg of messages(); track $index) {
              <app-chat-bubble [message]="msg"></app-chat-bubble>
            }
            
            @if (isLoading()) {
              <div class="chat chat-start">
                <div class="chat-header text-xs opacity-50 mb-1">Assistente</div>
                <div class="chat-bubble shadow-sm bg-base-300 text-base-content">
                  <span class="loading loading-dots loading-sm"></span>
                </div>
              </div>
            }
          </div>
        </div>

        <!-- Input Area Fixado ao fundo -->
        <app-chat-input [isLoading]="isLoading()" (onMessage)="sendMessage($event)"></app-chat-input>
      </div> 
      
      <!-- Drawer / Sidebar (Direita) -->
      <div class="drawer-side z-10">
        <label for="chat-drawer" aria-label="close sidebar" class="drawer-overlay"></label> 
        <ul class="menu p-4 w-80 min-h-full bg-base-200 text-base-content border-l border-base-300 flex flex-col justify-between">
          <div>
            <li>
              <button class="btn btn-ghost border border-base-content/20 justify-start shadow-sm mb-4 rounded-xl" (click)="newChat()">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5"><path stroke-linecap="round" stroke-linejoin="round" d="M12 4.5v15m7.5-7.5h-15" /></svg>
                Nova Conversa
              </button>
            </li>
            <li class="menu-title">Conversas Recentes</li>
            <!-- O backend retornaria dinamicamente aqui quando a funcionalidade de histórico estiver disponível -->
            <li><a class="active">Sessão Atual</a></li>
          </div>
          
          <div>
            <li>
              <a>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5"><path stroke-linecap="round" stroke-linejoin="round" d="M9.594 3.94c.09-.542.56-.94 1.11-.94h2.593c.55 0 1.02.398 1.11.94l.213 1.281c.063.374.313.686.645.87.074.04.147.083.22.127.324.196.72.257 1.075.124l1.217-.456a1.125 1.125 0 011.37.49l1.296 2.247a1.125 1.125 0 01-.26 1.431l-1.003.827c-.293.24-.438.613-.431.992a6.759 6.759 0 010 .255c-.007.378.138.75.43.99l1.005.828c.424.35.534.954.26 1.43l-1.298 2.247a1.125 1.125 0 01-1.369.491l-1.217-.456c-.355-.133-.75-.072-1.076.124a6.57 6.57 0 01-.22.128c-.331.183-.581.495-.644.869l-.213 1.28c-.09.543-.56.941-1.11.941h-2.594c-.55 0-1.02-.398-1.11-.94l-.213-1.281c-.062-.374-.312-.686-.644-.87a6.52 6.52 0 01-.22-.127c-.325-.196-.72-.257-1.076-.124l-1.217.456a1.125 1.125 0 01-1.369-.49l-1.297-2.247a1.125 1.125 0 01.26-1.431l1.004-.827c.292-.24.437-.613.43-.992a6.932 6.932 0 010-.255c.007-.378-.138-.75-.43-.99l-1.004-.828a1.125 1.125 0 01-.26-1.43l1.297-2.247a1.125 1.125 0 011.37-.491l1.216.456c.356.133.751.072 1.076-.124.072-.044.146-.087.22-.128.332-.183.582-.495.644-.869l.214-1.281z" /><path stroke-linecap="round" stroke-linejoin="round" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" /></svg>
                Configurações
              </a>
            </li>
          </div>
        </ul>
      </div>
    </div>
  `
})
export class ChatComponent implements OnInit {
  private chatService = inject(ChatService);
  private document = inject(DOCUMENT);

  @ViewChild('chatContainer') private chatContainer!: ElementRef<HTMLDivElement>;

  // Estado Local Inteligente
  messages = signal<ChatMessage[]>([]);
  isLoading = signal(false);
  
  // Controles de UI
  isSidebarOpen = signal(true);
  currentTheme = signal<'light'|'dark'>('dark');

  ngOnInit() {
    // Inicializar o tema lendo do HTML ou deixar default dark.
    const savedTheme = localStorage.getItem('theme') as 'light'|'dark' || 'dark';
    this.setTheme(savedTheme);
  }

  constructor() {
    // Scroll To Bottom automaticamente quando a lista de mensagens for alterada
    effect(() => {
      // Registrar que o signal atualizou
      this.messages();
      setTimeout(() => this.scrollToBottom(), 50);
    });
  }

  sendMessage(prompt: string) {
    if (!prompt) return;

    // Adiciona a mensagem do Usuário
    this.messages.update(prev => [...prev, { role: 'user', content: prompt }]);
    this.isLoading.set(true);

    // Faz o POST pro backend (/test-message)
    this.chatService.sendMessage(prompt, false) // false = salva historico no banco
      .pipe(
        finalize(() => this.isLoading.set(false))
      )
      .subscribe({
        next: (res) => {
          this.messages.update(prev => [...prev, { role: 'bot', content: res.response }]);
        },
        error: (err) => {
          console.error(err);
          this.messages.update(prev => [...prev, { role: 'bot', content: 'Ops! Ocorreu um erro ao comunicar com a inteligência artificial. O backend parece estar indisponível ou ocorreu um timeout.' }]);
        }
      });
  }

  newChat() {
    this.messages.set([]);
  }

  toggleSidebar() {
    this.isSidebarOpen.update(v => !v);
  }

  toggleTheme() {
    const newTheme = this.currentTheme() === 'dark' ? 'light' : 'dark';
    this.setTheme(newTheme);
  }

  private setTheme(theme: 'light'|'dark') {
    this.currentTheme.set(theme);
    this.document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('theme', theme);
  }

  private scrollToBottom() {
    if (this.chatContainer) {
      this.chatContainer.nativeElement.scrollTop = this.chatContainer.nativeElement.scrollHeight;
    }
  }
}

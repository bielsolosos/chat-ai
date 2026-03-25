import { Component, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat-input',
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="p-4 shrink-0 max-w-4xl mx-auto w-full mb-4">
      <div class="relative flex items-end gap-2 bg-base-200 rounded-3xl shadow-sm border border-base-300">
        
        <!-- Wrapper com scroll e dir="rtl" para manter o eixo oculto do scroll do lado ESQUERDO -->
        <div class="w-full max-h-[200px] overflow-y-auto [&::-webkit-scrollbar]:hidden [-ms-overflow-style:none] [scrollbar-width:none]" dir="rtl">
          <textarea
            #textareaElement
            class="w-full bg-transparent border-0 focus:outline-none resize-none min-h-[56px] py-4 pl-6 pr-16 text-base overflow-hidden"
            dir="ltr"
            placeholder="Envie uma mensagem para o assistente..."
            rows="1"
            [(ngModel)]="prompt"
            (input)="adjustHeight(textareaElement)"
            (keydown.enter)="handleEnter($event, textareaElement)"
            [disabled]="isLoading()"
          ></textarea>
        </div>

        <button 
          class="btn btn-neutral absolute right-2 bottom-2 btn-sm rounded-full w-10 h-10 p-0 z-10"
          [disabled]="!prompt().trim() || isLoading()"
          (click)="submit(textareaElement)"
        >
          @if(isLoading()) {
            <span class="loading loading-spinner loading-xs"></span>
          } @else {
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5">
              <path stroke-linecap="round" stroke-linejoin="round" d="M4.5 10.5L12 3m0 0l7.5 7.5M12 3v18" />
            </svg>
          }
        </button>
      </div>
      <div class="text-xs text-center text-base-content/40 mt-3 pt-1">
        A IA pode produzir informações imprecisas. Confirme os dados importantes.
      </div>
    </div>
  `
})
export class ChatInputComponent {
  isLoading = input<boolean>(false);
  onMessage = output<string>();

  prompt = signal('');

  adjustHeight(el: HTMLTextAreaElement) {
    el.style.height = 'auto'; // Reseta
    el.style.height = el.scrollHeight + 'px'; // Cresce até o limite
  }

  handleEnter(event: Event, el: HTMLTextAreaElement) {
    // Permite quebrar linha com Shift+Enter
    if (!(event as KeyboardEvent).shiftKey) {
      event.preventDefault();
      if (this.prompt().trim() && !this.isLoading()) {
        this.submit(el);
      }
    }
  }

  submit(el: HTMLTextAreaElement) {
    if (!this.prompt().trim() || this.isLoading()) return;
    this.onMessage.emit(this.prompt());
    this.prompt.set('');
    
    // Reseta o tamanho dinâmico após o envio
    setTimeout(() => {
      el.style.height = 'auto';
    }, 0);
  }
}

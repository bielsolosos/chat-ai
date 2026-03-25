import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestPrompt, ResponseFromLlm } from '../utils/chat.types';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private http = inject(HttpClient);
  private backendUrl = 'http://localhost:8080/test-message';

  /**
   * Envia uma mensagem para a API de IA Generativa do Spring Boot
   * @param prompt Texto do usuário
   * @param withoutHistory Se true, não grava o histórico na tabela `history` do DB
   */
  sendMessage(prompt: string, withoutHistory: boolean = false): Observable<ResponseFromLlm> {
    const body: RequestPrompt = { prompt };
    
    // Constrói a URL dinamicamente caso deseje suprimir histórico do banco
    let url = this.backendUrl;
    if (withoutHistory) {
      url += '?withoutHistory=true';
    }

    return this.http.post<ResponseFromLlm>(url, body);
  }
}

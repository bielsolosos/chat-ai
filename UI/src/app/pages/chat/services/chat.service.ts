import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestPrompt, ResponseFromLlm } from '../utils/chat.types';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private http = inject(HttpClient);
  private backendUrl = 'http://localhost:8080/test-message';

  /**
   * Envia uma mensagem para a API de IA Generativa do Spring Boot
   * @param prompt Texto do usuário
   * @param withoutHistory Se true, não grava o histórico na tabela `history` do DB
   */
  sendMessage(prompt: string): Observable<ResponseFromLlm> {
    const body: RequestPrompt = { prompt };

    return this.http.post<ResponseFromLlm>(this.backendUrl, body);
  }
}

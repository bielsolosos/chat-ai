export interface RequestPrompt {
  prompt: string;
}

export interface ResponseFromLlm {
  response: string;
}

export interface ChatMessage {
  role: 'user' | 'bot';
  content: string;
}

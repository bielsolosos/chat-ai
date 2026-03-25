# AI Agent API Contract (Contract-First)

## 1) Objetivo

Este documento define o contrato da API backend para integrar um chatbot em Angular.
Atualmente existe **1 endpoint** de chat.

## 2) Stack e contexto tecnico (Backend)

- Runtime: Java 21 + Spring Boot 3.5
- Persistencia: PostgreSQL + JPA
- Migrations: Flyway
- Provedores LLM: Google Gemini e Ollama (via Spring AI)
- Selecao de provedor por configuracao (`MODEL_TO_USE`)

## 2.1) Stack e contexto tecnico (Frontend)

- **Framework**: Angular 21 (Standalone Components)
- **Estilização e UI**: Tailwind CSS v4 + DaisyUI v5
- **Reatividade e Estado**: Angular Signals (`signal`, `computed`, `effect`)
- **Bibliotecas**: RxJS para chamadas HTTP encapsuladas em Signals na camada View.
- **Arquitetura**: Focada em domínios locais. Funcionalidades encapsuladas.

### Exemplo da Estrutura de Domínio Angular Atual
O projeto utiliza uma organização clara e modular focada na página/funcionalidade:

```text
src/
 └── app/
      ├── core/          # Guardiões, Interceptors e Providers Globais
      ├── shared/        # Peças de UI reaproveitáveis e Tipos Genéricos
      ├── pages/         # ⬅️ O CORAÇÃO DA APLICAÇÃO
      │    └── chat/     # Ex: O domínio do chat totalmente isolado
      │         ├── components/      # (ex: chat-input, chat-bubble)
      │         ├── services/        # Regras de Negócio e APIs (ex: chat.service.ts)
      │         ├── utils/           # Helper functions e Interfaces locais
      │         ├── chat.component.ts
      │         └── chat.routes.ts   # Roteamento isolado via loadChildren
      ├── app.routes.ts
      └── app.config.ts
```

## 3) Base URL

- Local default: `http://localhost:8080`

## 4) Endpoint de chat

### `POST /test-message`

Gera resposta do modelo de IA para o prompt enviado.

#### Query params

- `withoutHistory` (boolean, opcional)
  - `true`: nao salva historico no banco
  - `false` ou ausente: salva historico no banco

#### Request body (`application/json`)

```json
{
  "prompt": "string"
}
```

#### Response body (`200 OK`, `application/json`)

```json
{
  "response": "string"
}
```

#### Exemplo 1 - sem historico

```http
POST /test-message?withoutHistory=true HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "prompt": "Me explique o que e IA generativa em 2 linhas"
}
```

Resposta esperada:

```json
{
  "response": "IA generativa cria conteudo novo a partir de padroes aprendidos em dados. Ela pode gerar texto, imagens, audio ou codigo conforme o contexto solicitado."
}
```

#### Exemplo 2 - com historico (default)

```http
POST /test-message HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "prompt": "Me de uma ideia de projeto Angular com chatbot"
}
```

Resposta esperada:

```json
{
  "response": "Voce pode criar um assistente de suporte com Angular, chat em tempo real e historico salvo no backend para analise posterior."
}
```

## 5) Regras de persistencia (tabela `history`)

Quando `withoutHistory=false` (ou ausente), o backend salva:

- `message`: **resposta do modelo** (nao salva o prompt do usuario)
- `model`: nome do vendor selecionado na configuracao (`ollama` ou `gemini`)
- `vendor`: enum como **STRING em maiusculo** (`OLLAMA` ou `GEMINI`)

## 6) CORS

CORS esta liberado globalmente para `/**`:

- origins: `*` (via `allowedOriginPatterns("*")`)
- methods: `GET, POST, PUT, PATCH, DELETE, OPTIONS`
- headers: `*`

Arquivo de referencia: `src/main/java/br/dev/bielsolosos/aichat/infrastructre/config/CorsConfig.java`.

## 7) Configuracao relevante (backend)

Em `application.yml`:

- Banco Postgres (`spring.datasource.*`)
- Flyway habilitado (`spring.flyway.enabled=true`)
- Selecao do modelo para chat (`spring.chat-ai.model-to-use`)

Variaveis de ambiente esperadas:

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `MODEL_TO_USE` (`ollama` ou `gemini`)
- `GEMINI_API_KEY` (quando usar Gemini)
- `OLLAMA_BASE_URL` e `OLLAMA_MODEL` (quando usar Ollama)

## 8) Contrato para Angular (resumo rapido)

- Metodo: `POST`
- URL: `/test-message`
- Query opcional: `withoutHistory=true|false`
- Body: `{ prompt: string }`
- Response: `{ response: string }`
- Tipo esperado no frontend:

```ts
export interface RequestPrompt {
  prompt: string;
}

export interface ResponseFromLlm {
  response: string;
}
```

## 9) Observacoes importantes

- O endpoint hoje sempre retorna `200` em sucesso funcional.
- Validacoes de payload (ex.: `prompt` vazio) ainda nao estao explicitamente tratadas no controller.
- A API nao possui versionamento de rota por enquanto (ex.: `/api/v1`).

### TAREFA:

Após ler a documentação do Back-end faça o seguinte:

1. Crie um serviço em Angular para se comunicar com a API.
2. Crie uma página de testes para o chat.
3. Implemente a funcionalidade de chat.
4. Implemente telas bonitas com um layout bem simples de realmente ser apenas o Chat.
5. O render deve ser feito com Angular Signals. E deve ter um loading enquanto espera voltar o response do backend.
6. faça o layout já pronto para ser escalavel para ter mais funcionalidades no futuro.
7. Não esqueça que esse projeto é inspirado em um chatbot como o site do gemini e do chatgpt.

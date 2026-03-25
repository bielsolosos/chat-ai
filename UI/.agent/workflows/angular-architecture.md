---
description: Arquitetura e Padrões do Projeto Angular
---

# Diretrizes de Arquitetura e Empacotamento - Angular 21 (chat-ai)

Este documento dita as regras de desenvolvimento que nós seguiremos para o seu projeto. Como esse projeto utiliza uma stack muito moderna (**Angular 21**, **Vitest** e **Tailwind CSS v4**), nosso empacotamento será construído em cima dos mais recentes padrões do framework.

## 1. Estrutura de Pastas (Empacotamento baseado em Features)
O projeto deve seguir uma estrutura agrupada por contexto (Domínio / *Feature*) em vez do tipo de arquivo (Services, Components, etc.). 

Sua pasta `src/app/` deve ser organizada da seguinte forma:

```text
src/
 └── app/
      ├── core/          # (Opcional) Guardiões, Interceptors de HTTP, Serviços Globais Críticos.
      ├── shared/        # (Opcional) Componentes de UI Genéricos (Botões, Inputs), Types genéricos.
      ├── features/      # O CORAÇÃO DO PROJETO. Módulos da sua aplicação.
      │    ├── chat/     # Exemplo: O domínio do chat
      │    │    ├── ui/              # Componentes puramente visuais (Dumb)
      │    │    ├── data-access/     # Serviços de API, Estado
      │    │    ├── utils/           # Funções auxiliares, Types e Interfaces
      │    │    └── chat.routes.ts   # Rotas filhas lazy-loaded
      │    └── auth/
      ├── app.routes.ts
      └── app.config.ts
```
**Por que fazer assim?** 
Isso garante baixo acoplamento. Se um dia precisarmos modificar algo na funcionalidade do chat, sabemos exatamente que tudo estará reunido na mesma pasta, e não espalhado pela aplicação inteira.

## 2. Padrões de Componentes (Standalone & Control Flow)
- **Zero NgModules**: Não usaremos a arquitetura antiga baseada em `@NgModule`. Tudo será construído como **Standalone Components** (`standalone: true`).
- **Novo Control Flow**: Na criação dos templates (`.html`), em vez das diretivas estruturais antigas como `*ngIf` e `*ngFor`, usaremos a sintaxe limpa de controle de fluxo nativa do novo Angular: `@if`, `@for` e `@switch`.

## 3. Gerenciamento de Estado e Reatividade (Signals)
- **Signals**: Toda a reatividade e manipulação de estado dos nossos componentes utilizará as APIs de [Signals](https://angular.dev/guide/signals). Faremos uso intenso de `signal()`, `computed()` e, se extremamente necessário, `effect()`.
- **Comunicação de rede (RxJS + Signals)**: O recurso `HttpClient` para as requisições continuará devolvendo `Observables` via RxJS, mas sempre que eles chegarem nas views ou no estado do componente, nós podemos transacionar isso facilmente usando a conversão (exemplo: `toSignal()`).

## 4. Estilos e Bibliotecas
- **Tailwind CSS v4**: Sua aplicação já está configurada com a nova versão do Tailwind. Todo o nosso sistema de design visual será pautado pelas classes utilitárias dele. Apenas escreveremos CSS vanilla pontual para casos muito excepcionais.
- **Teste com Vitest**: Como estamos usando `vitest` em lugar do Karma/Jasmine clássico, nossos arquivos de testes (`.spec.ts`) buscarão a velocidade e a compatibilidade do ambiente moderno do Vite.

## 5. Como aplicar tudo isso (Workflow Prático)
Sempre que pedirmos à IA para criar algo novo ou quando você mesmo for codar, a convenção será: 
1. **É uma tela ou fluxo inteiro?** Cria-se uma nova Feature (ex: `features/dashboard`).
2. **É um componente isolado e reaproveitável?** Coloca-se no `shared/ui/nome-do-componente`.
3. **Comunica com API?** Crie os serviços isolados na sub-camada `data-access` da sua Feature e aplique a injeção de dependência globalmente (`providedIn: "root"`) se couber, ou restrinja esse serviço apenas ao escopo necessário.

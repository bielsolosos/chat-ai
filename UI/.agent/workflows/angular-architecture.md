---
description: Arquitetura e Padrões do Projeto Angular
---

# Diretrizes de Arquitetura e Empacotamento - Angular 21 (chat-ai)

Este documento dita as regras de desenvolvimento que nós seguiremos para o seu projeto. Como esse projeto utiliza uma stack muito moderna (**Angular 21** e **Signals**), nosso empacotamento será construído em cima dos mais recentes padrões do framework, focado em domínios.

> **Nota:** Este projeto **NÃO** terá testes unitários, portanto, não criaremos arquivos `.spec.ts` para componentes ou serviços.

## 1. Estrutura de Pastas (Empacotamento baseado em Domínios)
O projeto deve seguir uma estrutura agrupada por contexto (Domínio) em pastas chamadas `pages` ou `modules`. 

Sua pasta `src/app/` deve ser organizada da seguinte forma:

```text
src/
 └── app/
      ├── core/          # (Opcional) Guardiões, Interceptors de HTTP, Configurações Globais.
      ├── shared/        # (Opcional) Componentes de UI Genéricos (Botões, Inputs), Types genéricos.
      ├── pages/         # O CORAÇÃO DO PROJETO. Organizado por domínios.
      │    ├── chat/     # Exemplo: O domínio do chat
      │    │    ├── components/      # Componentes visuais referentes a esta página (em vez de 'ui')
      │    │    ├── services/        # Serviços de API e Estado (em vez de 'data-access')
      │    │    ├── utils/           # Funções auxiliares, Types e Interfaces
      │    │    └── chat.routes.ts   # Rotas filhas lazy-loaded
      │    └── auth/
      ├── app.routes.ts
      └── app.config.ts
```
**Por que fazer assim?** 
Isso garante baixo acoplamento e separação por domínio. Se precisarmos modificar a funcionalidade do chat, sabemos exatamente que tudo estará reunido na mesma pasta `pages/chat/`, dividindo responsabilidades claramente entre `components` e `services`.

## 2. Padrões de Componentes (Standalone & Control Flow)
- **Zero NgModules**: Tudo será construído como **Standalone Components** (`standalone: true`).
- **Novo Control Flow**: Na criação dos templates (`.html`), usaremos a sintaxe limpa de controle de fluxo nativa do novo Angular: `@if`, `@for` e `@switch`.

## 3. Gerenciamento de Estado e Reatividade (Signals)
- **Signals para Estado Local**: Toda a reatividade visual e gerenciamento de estado dos nossos componentes utilizará as APIs nativas de [Signals](https://angular.dev/guide/signals). Faremos uso de `signal()`, `computed()` e `effect()`.
- **HttpClient Funcional**: O `HttpClient` continuará sendo usado normalmente para chamadas de API devolvendo `Observables`. Faremos a conversão natural desses observables para Signals (`toSignal()`) quando os dados chegarem na camada de View (template do componente) para manter o fluxo fácil e sem complicações.

## 4. Estilos e Bibliotecas
- **Estilização Core**: Tailwind CSS v4 para classes utilitárias.
- **Biblioteca de Componentes**: DaisyUI v5 para componentes de UI pre-estilizados e suporte a temas (Light/Dark).
- **Sem Bibliotecas de Componentes Pesadas**: Focaremos no DaisyUI combinado com Tailwind para manter a aplicação extremamente rápida, renderizando tudo de forma limpa.

## 5. Como aplicar tudo isso (Workflow Prático)
Sempre que formos criar código novo: 
1. **É uma tela ou fluxo novo?** Cria-se um novo domínio dentro de `pages/` (ex: `pages/dashboard`).
2. **É um componente isolado e reaproveitável?** Coloca-se no `shared/components/nome-do-componente`.
3. **Comunica com API?** Crie os serviços na pasta `services/` dentro da sua página (ex: `pages/chat/services/`) e injete no componente que precisa usá-lo.
4. **Sem Arquivos de Teste**: Pule a criação do sufixo `.spec.ts` em toda a base de código.

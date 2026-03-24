# chatAi

Projeto Spring Boot com suporte a MySQL, Flyway migrations e variaveis via `.env`.

## Requisitos

- Java 21
- Maven Wrapper (`mvnw`)
- MySQL em execucao

## Configuracao de ambiente

1. Copie o arquivo de exemplo:

```powershell
Copy-Item .env.example .env
```

2. Edite o `.env` com suas credenciais e chaves.

Variaveis padronizadas:

- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `GEMINI_API_KEY`
- `GEMINI_MODEL`
- `OLLAMA_BASE_URL`
- `OLLAMA_MODEL`

## Banco e migrations

- Flyway esta habilitado.
- Migration inicial: `src/main/resources/db/migration/V1__create_history_table.sql`.
- A tabela `history` possui:
  - `id` (`BIGINT`, PK, auto increment)
  - `message` (`TEXT`, descricao do evento)

## Executar

```powershell
.\mvnw.cmd spring-boot:run
```

## Testes

```powershell
.\mvnw.cmd test
```


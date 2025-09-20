
# OpenFlag — Feature Flags self-hosted (Java • Spring Boot)

Servidor de **feature flags** auto-hospedado, simples e leve, com API de administração e endpoint de avaliação para apps e serviços. Útil para liberar funcionalidades gradualmente, fazer canary/rollout percentual, ou ativar recursos por país/usuário — um tipo de serviço que muitas vezes é pago.

## Recursos
- CRUD de flags por API (autenticada por **API Key**).
- Estratégias de avaliação:
  - `enabled`: liga/desliga global.
  - `includeIds` e `excludeIds`: listas de usuários.
  - `percentage`: rollout de 0–100% baseado em hash consistente do `userId`.
  - `attributeKey` + `attributeAllowedCsv`: permite restringir por atributo (ex.: `country=BR,PT`).
- Banco H2 por padrão (arquivo), com perfil opcional para PostgreSQL.
- Sem dependência de painel web (fácil de embutir em pipelines/infra como código).

## Como rodar
Pré-requisitos: Java 17+ e Maven 3.9+

```bash
# compilar e rodar
mvn -q -DskipTests package
java -jar target/openflag-0.1.0.jar

# ou
mvn spring-boot:run
```

Variáveis de ambiente úteis:
- `ADMIN_API_KEY` (default `changeme`) — chave para endpoints `/api/**`.
- `SPRING_PROFILES_ACTIVE=postgres` — ativa o perfil PostgreSQL.

## API
### Criar flag (admin)
```
POST /api/flags
X-API-Key: <sua-chave>
{
  "key": "checkout.newExperience",
  "enabled": true,
  "percentage": 25,
  "includeIds": "user123,user999",
  "excludeIds": "",
  "attributeKey": "country",
  "attributeAllowedCsv": "BR,PT"
}
```

### Listar flags (admin)
```
GET /api/flags
X-API-Key: <sua-chave>
```

### Avaliar flag (SDK)
```
GET /sdk/evaluate?flagKey=checkout.newExperience&userId=user123&attr.country=BR
# resposta
{ "flagKey":"checkout.newExperience", "enabled": true, "reason":"included" }
```

## Persistência
- H2 padrão: `./openflag-data`.
- PostgreSQL: defina `SPRING_PROFILES_ACTIVE=postgres` e ajuste `application-postgres.yml`.

## Segurança
- Use **API Key** forte em produção.
- Coloque atrás de um proxy com TLS.
- Faça backup do H2 ou use banco externo.

## Licença
MIT.

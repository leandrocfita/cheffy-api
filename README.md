# Cheffy — Sistema de Gestão para Restaurantes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.0-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-latest-47A248.svg)](https://www.mongodb.com/)
[![Kafka](https://img.shields.io/badge/Apache%20Kafka-7.5.0-black.svg)](https://kafka.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)

Backend multi-tenant para gestão de restaurantes, desenvolvido como Tech Challenge da Pós-Graduação em Arquitetura e Desenvolvimento Java da FIAP.

> Veja a versão anterior (monolítica) [aqui](https://github.com/thiagosslima/cheffy).

---

## O que é o Cheffy?

O Cheffy é uma plataforma de gestão de restaurantes construída como uma arquitetura de microsserviços. Ela permite que restaurantes gerenciem seus cardápios, usuários façam pedidos e o sistema processe pagamentos — tudo de forma distribuída, assíncrona e segura.

O sistema é composto por três microsserviços principais que se comunicam via HTTP/REST (autenticação) e Apache Kafka (pedidos), além de serviços de infraestrutura como PostgreSQL, MongoDB e um gateway de pagamentos externo.

---

## Arquitetura de Microsserviços

```
┌─────────────┐     JWT      ┌──────────────────┐
│   Cliente   │─────────────▶│   auth-service   │  :8085
│  (HTTP/REST)│              │  OAuth2 + RS256   │──── PostgreSQL (auth_service)
└──────┬──────┘              └──────────────────┘
       │ Bearer Token
       ▼
┌──────────────────┐  Feign  ┌──────────────────┐
│   cheffy-api     │────────▶│   auth-service   │
│  (API principal) │  :8080  │  (registro user) │
└──────┬───────────┘         └──────────────────┘
       │ PostgreSQL (cheffy)
       │
       │ Kafka: order.created
       ▼
┌──────────────────┐  Feign  ┌──────────────────┐
│  order-service   │────────▶│    procpag       │  :8089
│  Pedidos + Pag.  │  :8083  │  Gateway Pagto.  │
└──────┬───────────┘         └──────────────────┘
       │ MongoDB (cheffy-order-service)
       │
       │ Kafka: order.status-change
       └──────────────────▶ cheffy-api (atualiza status)
```

### Serviços

| Serviço | Responsabilidade | Porta | Banco de Dados |
|---|---|---|---|
| `cheffy-api` | Usuários, restaurantes, cardápio, pedidos | 8080 | PostgreSQL (`cheffy`) |
| `auth-service` | Autenticação OAuth2, emissão de JWT (RS256) | 8085 | PostgreSQL (`auth_service`) |
| `order-service` | Processamento de pedidos e pagamentos | 8083 | MongoDB (`cheffy-order-service`) |
| `procpag` | Gateway de pagamentos (serviço externo) | 8089 | — |
| `kafka` | Message broker em modo KRaft (sem Zookeeper) | 9092 | — |
| `postgres` | Banco relacional compartilhado | 5432 | — |
| `mongoDb` | Banco de documentos para pedidos | 27017 | — |

---

## Stack Técnica

### cheffy-api

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.4.5 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 18.0 |
| Mensageria | Apache Kafka (KRaft) |
| Autenticação | Spring Security + OAuth2 Resource Server (JWT RS256) |
| Integração externa | OpenFeign (auth-service) |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Build | Maven (cobertura mínima: 80% via JaCoCo) |
| Containerização | Docker + Docker Compose |

### auth-service

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.7 |
| Autenticação | Spring Security + OAuth2 Authorization Server |
| JWT | JJWT 0.12.5 (RS256 com par de chaves RSA) |
| Banco de dados | PostgreSQL 18.0 |

### order-service

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.4.0 |
| Persistência | Spring Data MongoDB |
| Mensageria | Apache Kafka |
| Integração externa | OpenFeign + Resilience4j (Circuit Breaker + Retry) |
| Autenticação | Spring Security + OAuth2 Resource Server (JWT RS256) |

---

## Arquitetura Interna (cheffy-api)

O `cheffy-api` segue Clean Architecture + DDD, organizado em 4 camadas:

```
Presentation  →  Controllers · DTOs · Validações · Swagger
Application   →  Use Cases · Ports (Input/Output) · Services · Mappers
Domain        →  Entities · Value Objects · Exceptions
Infrastructure→  JPA Adapters · Kafka Producer/Consumer · Feign Clients · Security
```

**Entidades de domínio:** `User`, `Address`, `Restaurant`, `Menu`, `FoodItem`, `Order`, `OrderItem`, `Profile`

**Value Objects:** `Money` (precisão de 2 casas decimais), `WorkingHours` (horário de funcionamento + suporte a 24h), `PageRequest`/`PageResult` (paginação)

---

## Segurança

O sistema usa OAuth2 distribuído com JWT assinado por RSA (RS256):

1. O `auth-service` gera tokens com chave privada RSA
2. `cheffy-api` e `order-service` validam os tokens via endpoint JWKS público (`/.well-known/jwks.json`)
3. As chaves RSA ficam montadas em volume (`./keys/`) e nunca são distribuídas junto ao código

**Endpoints públicos:**
- `POST /auth/login` (auth-service)
- `POST /api/v1/users` (cadastro de usuário)
- `GET /api/v1/profiles/**` e `POST /api/v1/profiles/**`
- Documentação Swagger (`/swagger-ui.html`, `/v3/api-docs/**`)
- Health checks (`/actuator/**`)

Todos os demais endpoints exigem `Authorization: Bearer <token>`.

Erros seguem o formato RFC 7807 (`status`, `title`, `detail`).

---

## Eventos Kafka

| Tópico | Produzido por | Consumido por | Descrição |
|---|---|---|---|
| `order.created` | cheffy-api | order-service | Novo pedido criado |
| `order.status-change` | order-service | cheffy-api | Atualização de status do pedido |

---

## Funcionalidades

- Gestão de usuários com perfis e múltiplos endereços
- Autenticação stateless via JWT (RS256)
- Cadastro e gerenciamento de restaurantes (horário de funcionamento + fuso horário)
- Cardápio por restaurante com controle de disponibilidade e entrega
- Criação e acompanhamento de pedidos com processamento assíncrono
- Integração com gateway de pagamentos via Circuit Breaker (Resilience4j)
- Paginação e ordenação em todas as listagens

---

## Como Executar

**Pré-requisitos:**
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/) instalado
- Portas disponíveis: `8080`, `8083`, `8085`, `8089`, `8090`, `8081`, `5432`, `27017`, `9092`

```bash
git clone https://github.com/leandrocfita/cheffy-api.git
cd cheffy-api
```

Crie o arquivo `.env` na raiz do projeto (este arquivo **não está versionado**):

```env
POSTGRES_PASSWORD=P4ssword!
DB_HOST=postgres
DB_PORT=5432
DB_USERNAME=postgres
JWT_SECRET=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
JWT_ISSUER=cheffy-auth-service
JWT_EXPIRATION=3600
PRIVATE_KEY_PATH=/app/keys/private.pem
PUBLIC_KEY_PATH=/app/keys/public.pem
KEY_DIR=/app/keys
DB_NAME=auth_service
JWKS_URL=http://procpag:8085/.well-known/jwks.json
```

```bash
docker compose up --build
```

```bash
docker compose down      # mantém os dados
docker compose down -v   # remove volumes (apaga dados)
```

---

## API — Endpoints Principais

> Acesse o `.json` da collection Postman [aqui](https://github.com/leandrocfita/cheffy/blob/main/src/main/resources/Cheffy_Postman_Collection.json).

### cheffy-api — `http://localhost:8080`

| Recurso | Método | Path | Auth |
|---|---|---|---|
| Login | POST | `/auth/login` (via auth-service) | Público |
| Criar usuário | POST | `/api/v1/users` | Público |
| Usuários | GET / PATCH / DELETE | `/api/v1/users/**` | Bearer Token |
| Endereços | POST / PATCH / DELETE | `/api/v1/users/{id}/addresses/**` | Bearer Token |
| Restaurantes | GET / POST / PATCH | `/api/v1/restaurants/**` | Bearer Token |
| Cardápio | GET / POST / PATCH | `/api/v1/restaurants/{id}/food-items/**` | Bearer Token |
| Pedidos | GET / POST / PATCH | `/api/v1/orders/**` | Bearer Token |
| Perfis | GET / POST / PUT / DELETE | `/api/v1/profiles/**` | Público (GET/POST) / Token (PUT/DELETE) |

### auth-service — `http://localhost:8085`

| Endpoint | Descrição |
|---|---|
| `POST /auth/login` | Autentica usuário e retorna JWT |
| `GET /.well-known/jwks.json` | Chave pública RSA para validação de tokens |

### order-service — `http://localhost:8083`

Processamento de pedidos integrado via Kafka (consulte Swagger em `:8083/swagger-ui.html`).

---

## UIs de Administração

| Ferramenta | URL | Credenciais |
|---|---|---|
| Swagger — cheffy-api | `http://localhost:8080/swagger-ui.html` | — |
| Swagger — auth-service | `http://localhost:8085/swagger-ui.html` | — |
| Swagger — order-service | `http://localhost:8083/swagger-ui.html` | — |
| Kafka UI | `http://localhost:8090` | — |
| Mongo Express | `http://localhost:8081` | `admin` / `pass` |

---

## 👥 Equipe

- Leandro Fita
- Igor Costa
- Rodrigo Ferreira
- Thiago Soares
- Victor Reis

## 📄 Licença

Este projeto foi desenvolvido como parte do Tech Challenge da FIAP e é disponibilizado para fins educacionais.

## 🤝 Contribuindo

Este é um projeto acadêmico, mas sugestões e feedback são bem-vindos!

## 📞 Contato

Para dúvidas ou sugestões, abra uma issue no repositório.

---

Desenvolvido pela equipe Cheffy - FIAP 2026

# Cheffy — Sistema de Gestão para Restaurantes

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18.0-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED.svg)](https://www.docker.com/)

Backend multi-tenant para gestão de restaurantes, desenvolvido como Tech Challenge da Pós-Graduação em Arquitetura e Desenvolvimento Java da FIAP.

> Veja a versão anterior [aqui](https://github.com/thiagosslima/cheffy).

## 🚀 Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.7 |
| Persistência | Spring Data JPA + Hibernate |
| Banco de dados | PostgreSQL 18.0 |
| Autenticação | Spring Security + JWT |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Build | Maven |
| Containerização | Docker + Docker Compose |

## 🏗️ Arquitetura

Clean Architecture + DDD, organizada em 4 camadas:

```
Presentation  →  Controllers · DTOs · Swagger
Application   →  Use Cases · Ports (Input/Output)
Domain        →  Entities · Value Objects · Exceptions
Infrastructure→  JPA Adapters · Security · Bean Config
```

## Funcionalidades

- Gestão de usuários com perfis
- Autenticação stateless via JWT
- Cadastro e gerenciamento de restaurantes (horário de funcionamento + fuso horário)
- Cardápio por restaurante com controle de disponibilidade e entrega
- Gerenciamento de endereços por usuário
- Paginação e ordenação em todas as listagens

## 🛠️ Como Executar

**Pré-requisitos:** 
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [Git](https://git-scm.com/) instalado
- Portas `8080` (aplicação) e `5432` (PostgreSQL) disponíveis

```bash
git clone https://github.com/thiagosslima/cheffy.git
cd cheffy
```

Crie o arquivo `.env` na raiz:

```env
POSTGRES_PASSWORD=postgres123
DB_HOST=postgres
DB_PORT=5432
JWT_SECRET=chave_secreta_jwt_minimo_256_bits_para_seguranca_adequada
```

```bash
docker compose up --build
```

- API: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

```bash
docker compose down      # mantém os dados
docker compose down -v   # remove volumes
```

## API — Endpoints principais

> Acesse já o `.json` da collection [aqui](https://github.com/leandrocfita/cheffy/blob/main/src/main/resources/Cheffy_Postman_Collection.json).

| Recurso | Base path |
|---|---|
| Auth | `POST /api/v1/auth/login` |
| Usuários | `/api/v1/users` |
| Restaurantes | `/api/v1/restaurants` |
| Cardápio | `/api/v1/restaurants/{id}/food-items` |
| Perfis | `/api/v1/profiles` |

Endpoints públicos: `POST /auth/login`, `POST /users` e docs Swagger. Todos os demais exigem `Authorization: Bearer <token>`.

Erros seguem o formato RFC 7807 (`status`, `title`, `detail`).

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

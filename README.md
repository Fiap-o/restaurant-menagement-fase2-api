# Restaurant Management API

Backend do **Tech Challenge — Fase 2** (Pós Tech Arquitetura e Desenvolvimento Java), desenvolvido em Spring Boot. Fornece a base de um sistema de gestão compartilhado entre restaurantes, cobrindo cadastro e autenticação de usuários, gestão de tipos de usuário, cadastro de restaurantes e dos itens de seus cardápios.

## Integrantes

- Sophia Amaral Silva
- Erick Roberto Ribeiro

## Tecnologias

- Java 17
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- Spring Security + JWT 
- PostgreSQL 16
- springdoc-openapi (Swagger / OpenAPI 3)
- Docker & Docker Compose
- Lombok

## Funcionalidades

- Cadastro, atualização e exclusão de usuários
- CRUD completo de tipos de usuário, com associação a usuários existentes
- Cadastro, atualização, consulta e exclusão de restaurantes, vinculados a um usuário responsável
- Cadastro, atualização, consulta e exclusão de itens de cardápio, vinculados a um restaurante
- Endpoint dedicado para troca de senha
- Endpoint dedicado para atualização dos demais dados do usuário
- Busca de usuários por nome
- Garantia de e-mail, login e nome de tipo de usuário únicos
- Autenticação via login/senha com emissão de token JWT
- Respostas de erro padronizadas via RFC 7807 (`ProblemDetail`)
- Versionamento de API (`/api/v1`)
- Documentação interativa via Swagger UI

## Como executar

### Pré-requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado e em execução
- Para rodar localmente sem Docker (seção abaixo): [Java 17+](https://adoptium.net/) instalado

### Subindo com Docker Compose (recomendado)

```bash
docker compose up -d --build
```

Isso sobe dois containers:

| Serviço    | Descrição                  | Porta |
|------------|-----------------------------|-------|
| `postgres` | Banco de dados PostgreSQL   | 5432  |
| `api`      | Aplicação Spring Boot       | 8080  |

A aplicação fica disponível em `http://localhost:8080`.

### Rodando localmente (sem empacotar em Docker, para desenvolvimento)

1. Suba apenas o banco: `docker compose up -d postgres`
2. Rode a aplicação pela IDE (classe `RestaurantManagementApplication`) ou via:
   ```bash
   ./mvnw spring-boot:run
   ```

> Se você tiver um PostgreSQL nativo instalado no Windows, garanta que ele não esteja ocupando a porta `5432` antes de subir o container — os dois não podem escutar na mesma porta ao mesmo tempo.

## Configuração

Credenciais padrão do banco (definidas em `docker-compose.yml`, usadas também como default em `application.yaml` para execução local):

| Variável | Valor padrão            |
|----------|--------------------------|
| Banco    | `restaurant_management`  |
| Usuário  | `admin`                  |
| Senha    | `admin123`               |

Ao rodar via Docker Compose, essas variáveis são sobrescritas automaticamente no container `api` (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`), apontando para o serviço `postgres` dentro da rede interna do Compose.

O schema do banco é criado/atualizado automaticamente pelo Hibernate (`ddl-auto: update`) na inicialização — não há necessidade de scripts de migração manuais.

## Documentação da API (Swagger)

Com a aplicação rodando, acesse:

- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Autenticação

A maior parte dos endpoints exige um token JWT. Fluxo:

1. Cadastre um tipo de usuário — `POST /api/v1/tipos-usuario` (não requer autenticação).
2. Cadastre um usuário, informando o `tipoUsuarioId` do passo anterior — `POST /api/v1/usuarios` (não requer autenticação).
3. Autentique-se — `POST /api/v1/auth/login` com login e senha, recebendo um token JWT.
4. Envie o token nas próximas requisições pelo header `Authorization: Bearer <token>`.

No Swagger UI, use o botão **Authorize** para informar o token e testar as rotas protegidas diretamente pela interface.

## Coleção Postman

O arquivo [`restaurant-management.postman_collection.json`](./restaurant-management.postman_collection.json) contém os principais cenários de teste: cadastro de usuário, tipo de usuário, restaurante e item de cardápio (válidos e inválidos), troca de senha, atualização de dados, busca por nome e validação de login. Basta importar no Postman.

## Endpoints principais

| Método | Rota                          | Descrição                        | Autenticação |
|--------|-------------------------------|-----------------------------------|--------------|
| POST   | `/api/v1/usuarios`            | Cadastrar usuário                 | Não          |
| PUT    | `/api/v1/usuarios/{id}`       | Atualizar dados do usuário        | Sim          |
| PATCH  | `/api/v1/usuarios/{id}/senha` | Trocar senha                      | Sim          |
| DELETE | `/api/v1/usuarios/{id}`       | Excluir usuário                   | Sim          |
| GET    | `/api/v1/usuarios/{id}`       | Buscar usuário por ID             | Sim          |
| GET    | `/api/v1/usuarios?nome=`      | Buscar usuários por nome          | Sim          |
| POST   | `/api/v1/auth/login`          | Autenticar e obter token JWT      | Não          |
| POST   | `/api/v1/tipos-usuario`       | Cadastrar tipo de usuário          | Não          |
| PUT    | `/api/v1/tipos-usuario/{id}`  | Atualizar tipo de usuário          | Sim          |
| DELETE | `/api/v1/tipos-usuario/{id}`  | Excluir tipo de usuário            | Sim          |
| GET    | `/api/v1/tipos-usuario/{id}`  | Buscar tipo de usuário por ID      | Sim          |
| GET    | `/api/v1/tipos-usuario`       | Listar tipos de usuário            | Sim          |
| PATCH  | `/api/v1/tipos-usuario/{tipoUsuarioId}/usuarios/{usuarioId}` | Associar tipo a um usuário | Sim |
| POST   | `/api/v1/restaurantes`        | Cadastrar restaurante              | Sim          |
| PUT    | `/api/v1/restaurantes/{id}`   | Atualizar restaurante              | Sim          |
| DELETE | `/api/v1/restaurantes/{id}`   | Excluir restaurante                | Sim          |
| GET    | `/api/v1/restaurantes/{id}`   | Buscar restaurante por ID          | Sim          |
| GET    | `/api/v1/restaurantes`        | Listar restaurantes                | Sim          |
| POST   | `/api/v1/itens-cardapio`      | Cadastrar item de cardápio         | Sim          |
| PUT    | `/api/v1/itens-cardapio/{id}` | Atualizar item de cardápio         | Sim          |
| DELETE | `/api/v1/itens-cardapio/{id}` | Excluir item de cardápio           | Sim          |
| GET    | `/api/v1/itens-cardapio/{id}` | Buscar item de cardápio por ID     | Sim          |
| GET    | `/api/v1/itens-cardapio/restaurante/{restauranteId}` | Listar itens de um restaurante | Sim |

## Tratamento de erros

Erros seguem o padrão RFC 7807 (`ProblemDetail`), retornando `type`, `title`, `status`, `detail` e `timestamp`. Alguns exemplos:

- `400 Bad Request` — dados inválidos, com detalhamento por campo
- `401 Unauthorized` — credenciais inválidas no login
- `404 Not Found` — usuário não encontrado
- `409 Conflict` — e-mail ou login já cadastrado

## Testes automatizados

O projeto inclui testes unitários (JUnit 5 + Mockito) para a camada de serviço, cobrindo cenários de sucesso e de erro:

- `UsuarioServiceImplTest` — cadastro, atualização, troca de senha, exclusão e busca de usuários
- `AuthServiceImplTest` — autenticação com credenciais válidas e inválidas

Para rodar:

```bash
./mvnw test
```

Ou, pela IDE, clique no ícone ▶️ ao lado da classe de teste (`src/test/java/.../service/impl/`).

## Arquitetura

O projeto segue **Clean Architecture**, organizado em três camadas com uma regra de dependência única: o código
só pode depender de camadas mais internas, nunca o contrário.

| Camada | Responsabilidade | Não depende de |
|---|---|---|
| **Domain** | Entidades de negócio e regras invariantes, portas (interfaces) de repositório, exceções de domínio | Nenhum framework (sem JPA, sem Spring) |
| **Application** | Casos de uso (um por ação de negócio), DTOs de entrada/saída, mapeamento entre DTO e domínio | Detalhes de persistência ou protocolo HTTP |
| **Infrastructure** | Implementações concretas: JPA/Hibernate, controllers REST, Spring Security, JWT, Swagger | — (é a camada mais externa) |

Essa separação garante que as regras de negócio (camada `domain`) possam ser entendidas e testadas sem depender de
banco de dados, framework web ou biblioteca de segurança — e permite trocar detalhes técnicos (ex: trocar de
banco de dados) sem alterar a lógica de negócio.

### Princípios SOLID aplicados

| Princípio | Aplicação no projeto |
|---|---|
| **SRP** (Responsabilidade Única) | Cada caso de uso (`application/usecase`) trata de uma única ação de negócio (ex: `CriarUsuarioUseCase`, `AtualizarSenhaUseCase`); controllers só traduzem requisição/resposta HTTP |
| **OCP** (Aberto/Fechado) | Repositórios e serviços externos são acessados via interfaces (`domain/repository`, `application/port`), permitindo trocar a implementação sem alterar os casos de uso |
| **LSP** (Substituição de Liskov) | As implementações de repositório (`infrastructure/persistence/repository`) cumprem integralmente o contrato definido pela interface de domínio correspondente |
| **ISP** (Segregação de Interfaces) | Interfaces enxutas, com apenas os métodos realmente usados pelos casos de uso (ex: `TokenService` expõe só geração de token, não validação) |
| **DIP** (Inversão de Dependência) | Casos de uso dependem de abstrações de domínio (`UsuarioRepository`, `TokenService`, `AuthenticationPort`), nunca de `JpaRepository` ou de bibliotecas de terceiros diretamente |

## Estrutura do projeto

```
src/main/java/com/fiap/restaurant_management/
├── domain/                      # Regras de negócio, independentes de framework
│   ├── model/                   # Entidades de domínio (Usuario, Endereco, TipoUsuario, Restaurante, ItemCardapio)
│   ├── repository/              # Portas (interfaces) de persistência
│   └── exception/                # Exceções de regra de negócio
├── application/                 # Orquestração dos casos de uso
│   ├── usecase/                 # Um caso de uso por ação de negócio
│   ├── dto/                     # DTOs de requisição e resposta da API
│   ├── mapper/                  # Conversão entre DTOs e entidades de domínio
│   └── port/                    # Portas para serviços externos (ex: geração de token)
└── infrastructure/              # Implementações concretas e detalhes técnicos
    ├── persistence/
    │   ├── entity/               # Entidades JPA
    │   ├── repository/           # Repositórios Spring Data JPA e adaptadores das portas de domínio
    │   └── mapper/               # Conversão entre entidades de domínio e entidades JPA
    ├── web/
    │   ├── controller/           # Controllers REST
    │   └── exception/            # Handler global de exceções (ProblemDetail)
    ├── security/                 # Filtro e serviço JWT, UserDetailsService
    └── config/                   # Configuração de segurança e Swagger/OpenAPI
```

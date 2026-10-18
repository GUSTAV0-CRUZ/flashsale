# 🎫 FlashSale API

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> Uma API de vendas de ingressos de alta concorrência (*Flash Sale*), projetada inicialmente como um monolito modular.

---

## 🎯 Sobre o Projeto e o Desafio

A **FlashSale API** é um projeto laboratório focado em engenharia de software de alta concorrência. O cenário simulado é uma venda relâmpago de ingressos altamente disputada, como um lote promocional de festival.

O objetivo principal é garantir as seguintes regras de negócio sob pressão extrema:

1. **Zero Overbooking:** impedir a venda de mais ingressos do que o disponível, mesmo com múltiplas tentativas de compra simultâneas.
2. **Reserva Temporária e Liberação Automática:** garantir o ciclo de vida do ingresso (Disponível → Pendente → Vendido/Cancelado).
3. **Consistência Distribuída:** orquestrar o pagamento e a notificação de forma consistente.

Aplicação foi construída intencionalmente como um monolito para estabelecer uma *baseline* de performance. O objetivo é executar testes de carga, identificar gargalos (*race conditions*, latência de banco de dados e falhas de integração) e documentar a evolução arquitetural para resolver esses problemas.

---

## 🏛️ Decisões Arquiteturais e de Negócio

### 1. Monolito Modular

- **Decisão:** a API foi construída inicialmente como um monolito, mas com pacotes isolados por domínio (`ticket`, `order`, `payment` e `notification`).
- **Justificativa:** acelerar o desenvolvimento inicial, reduzindo o *boilerplate* de infraestrutura distribuída, como filas, *service discovery* e comunicação de rede. Isso permite validar as regras de domínio antes de introduzir a complexidade de microsserviços. Quando um gargalo for identificado, a extração de um domínio poderá ser planejada.

### 2. MySQL como Banco de Dados

- **Decisão:** adoção do MySQL.
- **Justificativa:** o modelo de dados (ingressos, pedidos e notificações) é relativamente simples e não exige consultas analíticas complexas. O MySQL atende às necessidades da arquitetura inicial.

### 3. Integração com Serviços Externos via Adapters

- **Decisão:** utilização do padrão Adapter (Gateways) para isolar as integrações.
- **Provedores iniciais:**
    - **Mercado Pago:** pagamentos via PIX e cartão de crédito.
    - **Mailtrap:** serviço de envio de e-mails.
- **Justificativa:** isolar o núcleo da aplicação dos SDKs externos. A substituição do Mercado Pago por Stripe ou do Mailtrap por AWS SES exige alterações principalmente na camada de infraestrutura, mantendo o domínio desacoplado dos provedores.

### 4. Fluxo de Compra e Identidade

- **Decisão de negócio:** a compra não exige a criação prévia de login ou conta na plataforma.
- **Fluxo:** a transação é vinculada ao CPF informado. Após a confirmação, a entrada no evento é validada pelo CPF associado ao pedido.
- **Justificativa:** simplificar o funil de conversão e reduzir etapas no processo de compra, especialmente em cenários de *compras rápidas*.

### 5. Controle Transacional Estratégico (`@Transactional`)

- **Decisão:** utilização estratégica de `@Transactional` na camada `OrderService`, por exemplo, nos métodos `sell` e `confirmSale`.

- **Justificativa — trade-offs conscientes:**
    - No método `sell`, a transação engloba a integração com o Mercado Pago. A intenção é reverter a criação do pedido caso a API de pagamento retorne um erro síncrono.
    - Nos métodos de `Order`, a transação foi estendida ao envio de e-mails. Pois o email não pode ser perdido, porque é a única forma do cliente receber o ingresso.

> **Nota arquitetural:** manter uma transação de banco de dados aberta durante uma chamada HTTP externa pode aumentar a contenção e não garante atomicidade entre os dois sistemas.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3:** WebMVC, Spring Data JPA e Validation
- **MySQL:** banco de dados relacional
- **Docker e Docker Compose:** containerização
- **Lombok**
- **JUnit 5:** testes automatizados
- **Swagger / SpringDoc OpenAPI 3:** documentação da API
- **Mercado Pago Java SDK:** integração de pagamentos
- **Mailtrap Java SDK:** integração de e-mails

---

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

- Docker e Docker Compose instalados.
- Conta no Mercado Pago para obter credenciais de teste.
- Conta no Mailtrap para obter as credenciais de e-mail.

### 1. Clone o repositório

```bash
git clone https://github.com/GUSTAV0-CRUZ/flashsale.git
cd flashsale
```

### 2. Configure as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto com suas credenciais:

```dotenv
MERCADO_PAGO_ACCESS_TOKEN=seu_access_token_aqui
MERCADO_PAGO_SECRET_SIGNATURE=sua_secret_signature_aqui
MAILTRAP_TOKEN=seu_mailtrap_token_aqui
```

Certifique-se de que o `docker-compose.yml` e a configuração do Spring Boot utilizem essas variáveis com os nomes correspondentes.

### 3. Suba os containers

```bash
docker-compose up -d --build
```

O serviço MySQL possui um *healthcheck* configurado. A API Spring Boot (`flashsale_api`) deverá iniciar após o banco de dados estar saudável, conforme a configuração do Docker Compose.

### 4. Acesse a documentação da API

Swagger UI ou documentação OpenAPI:

```text
http://localhost/swagger-ui/index.html
```

Especificação OpenAPI, caso esteja exposta nesse caminho:

```text
http://localhost/openapi.yaml
```

> Os caminhos dependem da configuração do SpringDoc e do servidor. A porta `80` pressupõe que esteja publicada dessa forma no Docker Compose.

---

## 📚 Endpoints Principais da API

A documentação detalhada dos contratos está disponível no Swagger.

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/tickets` | Lista todos os ingressos. |
| `GET` | `/tickets/{id}` | Busca os detalhes de um ingresso. |
| `POST` | `/tickets/{id}/buy` | Inicia a compra, gerando QR Code PIX ou processando o cartão. |
| `PATCH` | `/tickets/{orderId}/refund` | Solicita o reembolso, considerando as regras de prazo configuradas. |

---

## 📈 Evolução Arquitetural e Métricas de Carga

Esta seção documentará os resultados dos testes de estresse, os gargalos identificados e as refatorações realizadas para melhorar a consistência e o desempenho.

---

## 👨‍💻 Autor

## 👨‍💻 Autor

<img src="https://github.com/GUSTAV0-CRUZ.png" width="100px;" />

**Gustavo Cruz**  
📧 gustavo.cruzs.dev@gmail.com

🔗 https://github.com/GUSTAV0-CRUZ

🔗 https://www.linkedin.com/in/gustavo-cruz-4a467b377

---
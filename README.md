# Lab V1 — DevOps Platform Operations

A progressive platform-operations lab built to practise deploying, operating, validating, troubleshooting, and documenting a realistic Java application stack.

The repository begins with a working application path built on Docker Compose:

```text
Client
  │
  ▼
NGINX reverse proxy
  │
  ▼
Apache Tomcat
  │
  ▼
Java WAR application
  │
  ▼
MySQL
```

The project is developed in controlled phases. Each phase adds one operational capability, validates it at runtime, and records the evidence before the next layer is introduced.

## Current implementation

The current repository contains a working local stack with:

- NGINX 1.27 as the external reverse proxy
- Apache Tomcat 9 with Java 17 as the application runtime
- A custom Java servlet application packaged as a WAR
- MySQL 8.4 as the persistent database
- Docker Compose service orchestration
- A dedicated Docker bridge network
- Environment-based runtime configuration
- MySQL schema initialization
- Java-to-MySQL connection validation
- Persistent MySQL storage
- Service logs and implementation evidence

## What has been validated

The completed phases demonstrate that:

- Docker Compose resolves the environment and service configuration
- NGINX reaches Tomcat through Docker's internal service-name resolution
- Tomcat deploys the Java WAR application
- MySQL initializes the application database and validation table
- The Java application reads database settings from runtime environment variables
- The application successfully connects to MySQL
- NGINX, Tomcat, and MySQL logs are available for operational investigation
- Database state survives normal container recreation through a named volume
- Local secrets remain outside version control

Detailed records are available in [`evidence/`](evidence/).

## Repository structure

```text
lab-v1/
├── docker-compose.yml
├── .env.example
├── .gitignore
├── Project_Schema.md
│
├── java-app/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/labv1/
│           │   ├── HealthServlet.java
│           │   └── DbCheckServlet.java
│           └── webapp/WEB-INF/
│               └── web.xml
│
├── nginx/
│   └── default.conf
│
├── mysql/
│   └── init/
│       └── 001-init.sql
│
├── tomcat/
│   └── webapps/
│       └── labapp.war
│
└── evidence/
    ├── phase-00-setup.md
    ├── phase-02-nginx-tomcat-mysql.md
    ├── phase-03-java-war-mysql-proof.md
    └── screenshots/
```

## Runtime design

### NGINX

NGINX accepts requests on the host-facing port and proxies them to the Tomcat service using Docker Compose internal DNS:

```text
nginx → tomcat:8080
```

The proxy uses the Compose service name rather than `localhost`. Inside the NGINX container, `localhost` would refer to NGINX itself.

### Tomcat and Java application

Tomcat runs the Java application as a Web Application Archive (WAR). Runtime database settings are injected through environment variables:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
```

This separates application code from environment-specific configuration.

### MySQL

MySQL starts with:

- an application database;
- a non-root application user;
- a persistent named volume;
- an initialization script under `mysql/init/`.

The initialization script creates a small validation table used to prove that database startup and SQL initialization completed successfully.

## Prerequisites

- Git
- Docker Desktop or Docker Engine
- Docker Compose v2

Maven is required only when rebuilding the Java WAR from source.

## Run locally

### 1. Clone the repository

```bash
git clone https://github.com/CyberEclipses/lab-v1.git
cd lab-v1
```

### 2. Create the local environment file

PowerShell:

```powershell
Copy-Item .env.example .env
```

Linux or macOS:

```bash
cp .env.example .env
```

Edit `.env` and replace the example database passwords with local values.

The real `.env` file is excluded from Git.

### 3. Validate the Compose configuration

```bash
docker compose --env-file .env config
```

This resolves variables and validates the Compose structure before containers are started.

### 4. Start the stack

```bash
docker compose --env-file .env up -d
```

### 5. Check service status

```bash
docker compose --env-file .env ps
```

Expected services:

```text
labv1_nginx
labv1_tomcat
labv1_mysql
```

### 6. Validate the request path

Tomcat directly:

```bash
curl http://localhost:8080
```

Through NGINX:

```bash
curl http://localhost:8088
```

Receiving the Tomcat response through port `8088` proves:

```text
Host → NGINX → Tomcat
```

### 7. Inspect logs

```bash
docker logs labv1_nginx
docker logs labv1_tomcat
docker logs labv1_mysql
```

### 8. Stop the stack

```bash
docker compose --env-file .env down
```

This removes the containers and network while preserving the MySQL volume.

To intentionally reset the lab database:

```bash
docker compose --env-file .env down -v
```

The `-v` option deletes the named volume and its database data.

## Rebuild the Java WAR

From `java-app/`:

```bash
mvn clean package
```

The generated artifact is created under:

```text
java-app/target/labapp.war
```

The Maven `target/` directory is excluded from Git because it is generated build output.

For the current lab slice, the deployable WAR used by Compose is stored at:

```text
tomcat/webapps/labapp.war
```

## Operational checks

Useful first checks during an incident:

```bash
docker compose ps
docker stats
docker logs labv1_nginx
docker logs labv1_tomcat
docker logs labv1_mysql
docker network inspect lab-v1_labv1_net
```

Typical failure areas include:

- an occupied host port;
- an incorrect NGINX upstream;
- a stopped Tomcat container;
- mismatched database credentials;
- an unavailable MySQL service;
- a missing WAR file;
- stale database-volume configuration;
- an invalid environment variable.

## Evidence model

Each phase records:

- objective;
- commands executed;
- expected result;
- actual result;
- runtime proof;
- logs where relevant;
- operational meaning;
- interview-ready explanation.

This makes the repository an implementation record rather than a collection of configuration files.

## Development progression

### Completed

- [x] Environment and cost guardrails
- [x] Git repository and secret hygiene
- [x] Docker Compose foundation
- [x] NGINX reverse proxy
- [x] Tomcat runtime
- [x] MySQL initialization and persistence
- [x] Java WAR deployment
- [x] Java-to-MySQL connectivity proof
- [x] Runtime logs and phase evidence

### Planned

- [ ] Apache service layer
- [ ] MariaDB compatibility exercise
- [ ] Elasticsearch
- [ ] Prometheus
- [ ] Node Exporter and cAdvisor
- [ ] Grafana
- [ ] Loki and log shipping
- [ ] Alertmanager
- [ ] Nagios
- [ ] AWS EC2 deployment
- [ ] Amazon S3 backup workflow
- [ ] Amazon CloudWatch integration
- [ ] Controlled failure and recovery scenarios
- [ ] Operations runbook and troubleshooting playbook

Planned components are listed as roadmap only and are not presented as completed implementation.

## Project objective

The objective is to build operational ability across the complete service lifecycle:

```text
Build
  ↓
Deploy
  ↓
Validate
  ↓
Operate
  ↓
Break
  ↓
Diagnose
  ↓
Recover
  ↓
Document
```

The emphasis is on runtime evidence, service interaction, logs, networking, database availability, recoverability, and clear operational documentation.

## Technologies currently demonstrated

`Docker` · `Docker Compose` · `NGINX` · `Tomcat` · `Java` · `Maven` · `WAR deployment` · `MySQL` · `SQL initialization` · `Docker networking` · `environment configuration` · `persistent volumes` · `runtime validation` · `log inspection`

## Status

Active development.

The current application spine is operational. Additional platform, observability, cloud, and incident-response layers will be added progressively and documented only after runtime validation.

# Phase 3 — Java WAR Deployment and MySQL Connection Proof

## Goal

Deploy a simple Java WAR into Tomcat and prove the Java application can connect to MySQL.

## Architecture Proven

User / curl
→ NGINX
→ Tomcat
→ Java WAR application
→ MySQL

## New Artifact

| Artifact                   | Purpose                  |
| -------------------------- | ------------------------ |
| java-app/                  | Java source code         |
| java-app/target/labapp.war | Built WAR artifact       |
| tomcat/webapps/labapp.war  | WAR deployed into Tomcat |

## Endpoints

| Endpoint         | Purpose                              |
| ---------------- | ------------------------------------ |
| /labapp/health   | Proves Java WAR is running in Tomcat |
| /labapp/db-check | Proves Java WAR can connect to MySQL |

## Commands Used

```powershell
docker run --rm `
  -v "${PWD}\java-app:/app" `
  -w /app `
  maven:3.9-eclipse-temurin-17 `
  mvn clean package

Copy-Item -Force java-app\target\labapp.war tomcat\webapps\labapp.war

docker compose --env-file .env -f compose\docker-compose.yml config

docker compose --env-file .env -f compose\docker-compose.yml up -d --force-recreate tomcat nginx

docker compose --env-file .env -f compose\docker-compose.yml ps

docker logs labv1_tomcat

curl http://localhost:8080/labapp/health
curl http://localhost:8080/labapp/db-check

curl http://localhost:8088/labapp/health
curl http://localhost:8088/labapp/db-check

docker exec -it labv1_mysql mysql -u labuser -plabpass123 labapp -e "SELECT * FROM health_check;"

docker logs labv1_nginx
docker logs labv1_tomcat
docker logs labv1_mysql
```

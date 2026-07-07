\# Phase 2 / 3 — NGINX + Tomcat + MySQL Base Stack



\## Goal



Build the first Lab V1 platform spine locally using Docker Compose.



Request path:



User / curl

→ NGINX

→ Tomcat



Database layer:



MySQL



\## Services



| Service | Container | Port |

|---|---|---|

| NGINX | labv1\_nginx | 8088 → 80 |

| Tomcat | labv1\_tomcat | 8080 → 8080 |

| MySQL | labv1\_mysql | 3306 → 3306 |



\## Commands Used



```powershell

docker compose --env-file .env -f compose\\docker-compose.yml config

docker compose --env-file .env -f compose\\docker-compose.yml up -d

docker compose --env-file .env -f compose\\docker-compose.yml ps

curl http://localhost:8080

curl http://localhost:8088

docker exec -it labv1\_mysql mysql -u labuser -plabpass123 labapp -e "SELECT \* FROM health\_check;"

docker logs labv1\_nginx

docker logs labv1\_tomcat

docker logs labv1\_mysql


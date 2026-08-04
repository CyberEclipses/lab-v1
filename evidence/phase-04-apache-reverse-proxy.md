# Phase 4 — Apache Reverse Proxy Layer

## Objective

Add Apache HTTP Server between NGINX and Tomcat and validate the complete request path.

## Architecture

```text
Client
→ NGINX
→ Apache
→ Tomcat
→ Java WAR
→ MySQL
```

# Incident — Apache Wrong Upstream Port

## Incident Summary

The application became unavailable through Apache and NGINX after the Apache reverse-proxy upstream was changed from Tomcat port 8080 to port 9999.

## Architecture

```text
Client
→ NGINX
→ Apache
→ Tomcat
→ Java WAR
→ MySQL
```

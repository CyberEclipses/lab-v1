# Phase 0 — Environment and Cost Guardrails

## Goal

Prepare the local environment before building Lab V1.

Lab V1 starts locally first. No AWS resources are created in this phase.

## Tool Verification

| Tool           | Result                                   |
| -------------- | ---------------------------------------- |
| Docker         | Docker version 28.4.0, build d8eb465     |
| Docker Compose | Docker Compose version v2.39.2-desktop.1 |
| Git            | git version 2.38.1.windows.1             |

## Environment Decision

Local-first implementation is confirmed.

Reason:

- No AWS cost during early build.
- Faster troubleshooting.
- Safer experimentation.
- Easier break/fix practice.
- Docker Compose can simulate the platform before EC2 deployment.

## Cost Guardrails

AWS is not used in Phase 0.

No EC2 instance created.  
No S3 bucket created.  
No CloudWatch agent configured.  
No EKS cluster created.  
No RDS database created.  
No managed Elasticsearch/OpenSearch created.

## Phase 0 Result

Environment is ready for Track A — Build the Platform.

Next phase:

Phase 1 / Phase 2 combined start:

- create minimal Docker Compose foundation
- start with NGINX + Tomcat + MySQL
- validate container startup
- validate request path

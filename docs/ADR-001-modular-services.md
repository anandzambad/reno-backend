# ADR-001: Modular services before full microservices

## Decision
Renevo will use modular domain boundaries first and a small number of independently deployable services. We will not split every business domain into a separate runtime service at startup.

## Why
A startup needs low operational overhead, simple deployment and fast iteration. Microservices are useful where independent scaling, deployment, security or team ownership justifies the network boundary.

## Initial runtime

- Renevo web shell / domain modules
- Core Spring Boot backend with clear domain packages
- Dedicated AI service
- MySQL
- Redis when needed

## Extraction triggers

A module can become a standalone microservice when at least one is true:

- It needs independent scaling.
- It needs an independent deployment cadence.
- It has a materially different runtime/resource profile (AI is the first example).
- It requires a separate security boundary.
- It has a separate owning team.

## Consequences

Positive: simpler operations, lower infrastructure cost, fewer distributed-system failures, faster development.

Trade-off: some services remain together longer than a textbook microservice architecture. We accept this because boundaries are maintained in code and can be extracted later.

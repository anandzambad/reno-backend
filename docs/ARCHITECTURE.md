# Renevo Architecture

## Principle
Renevo uses modular boundaries first and extracts independent microservices only when scaling, deployment, security, or ownership requires it. Avoid creating a microservice for every feature.

## Target modules

- Project — project identity, customer relationship, stages and progress
- Work — work plans, tasks, board and execution tracking
- Payment — estimates, milestones, payment status and transactions
- Material — project material requirements and delivery tracking
- Supplier — supplier identity, catalog and future supplier matching
- CRM/Notification — follow-ups, reminders and notifications
- AI — provider-neutral AI capabilities such as Work Planner

## Runtime shape

```text
Renevo Web / Micro-apps
        |
     API/BFF
        |
  +-----+------+------+------+------+
  |            |             |      |
Project       Work        Payment Material
                              |
                         Supplier
        |
     AI Service
        |
  Ollama/Open Model
```

## Startup deployment

Do not deploy every domain as a separate container initially. Start with a small number of independently deployable units:

1. Web frontend/shell
2. Core Spring Boot backend with modular domain packages
3. AI service
4. MySQL
5. Redis when required

Extract Work, Payment, Material, Supplier, etc. into separate services when there is a measurable reason.

## Frontend strategy

Use a shell + domain micro-app approach. The shell owns authentication, session, navigation, permissions and shared UI. Domain apps own their screens and domain API clients. Keep the initial browser bundle small through route-level/lazy loading; do not introduce a complex runtime federation system until independently deployed micro-apps are actually required.

## AI boundary

The core backend calls an AI service through a stable HTTP contract. The AI service owns model/provider adapters. Business services must not depend directly on a model vendor or Ollama SDK.

```text
Work Service -> AI Service -> Provider Adapter -> Ollama/Open Model
```

AI output is untrusted input. Validate it before it becomes business data. AI may suggest work; it must not directly approve tasks, payments, suppliers, refunds, or customer notifications.

## Evolution path

### Phase 1
Modular Spring Boot backend + frontend shell/micro-app boundaries + separate AI service.

### Phase 2
Extract high-load or independently owned domains (likely Work, Payment, AI, Notification).

### Phase 3
Kubernetes and independent autoscaling when traffic/operations justify it.

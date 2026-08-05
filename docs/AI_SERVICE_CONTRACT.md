# Renevo AI Service Contract

## Goal
Keep the core backend independent from Ollama, model names and future paid AI providers.

## Boundary

```text
Core Backend
   -> POST /internal/ai/work-plans/draft
AI Service
   -> Provider Adapter
      -> Ollama / open model
```

## Contract principles

- Request and response are versionable JSON.
- AI service returns suggestions only.
- Core backend validates business rules and authorization.
- No AI service access to payment write operations.
- No direct AI-to-database writes.
- Provider/model selection stays inside AI service configuration.
- Prompt and model changes must not require frontend changes.

## Current implementation

The current WorkPlannerService is a provider-neutral seam and uses deterministic rules so development does not require a model or paid API. The next implementation will move the provider behind an AI service boundary and validate structured output before business persistence.

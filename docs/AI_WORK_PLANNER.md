# Renevo AI Work Planner

## Goal
Help contractors create a first-draft project work plan from simple renovation requirements. The first release is intentionally conservative: AI assists planning; it does not create approved work, change payments, or make financial decisions automatically.

## User flow

1. Open a Renevo Project.
2. Enter or review project requirements/scope.
3. Select **Generate Work Plan**.
4. Renevo creates a draft containing stages, tasks, priority, estimated duration and dependencies.
5. Contractor/project manager reviews the draft.
6. Future release: user can edit/add/remove tasks and explicitly approve the plan.
7. Only approved tasks are created on the Work Board.

## API (phase 1)

`POST /api/ai/work-plans/draft`

Example request:

```json
{
  "projectCode": "REN-PUN-2026-00123",
  "projectTitle": "2 BHK Renovation",
  "propertyType": "2 BHK",
  "requirements": "Kitchen, two bathrooms, electrical and painting",
  "scope": "Kitchen renovation, bathroom renovation, new electrical wiring and painting"
}
```

The response is a **draft**. It contains `projectCode`, `summary`, `tasks`, and a disclaimer requiring human review.

## Safety / business rules

- AI output is never treated as a final quotation or guaranteed cost.
- AI cannot directly modify payment records.
- AI cannot directly approve contractors or suppliers.
- AI cannot directly publish tasks to the production Work Board.
- Human approval is required before generated tasks become project execution tasks.
- All AI-generated content should be auditable when persistence is introduced.

## AI provider strategy

The current service is a provider-neutral seam. It uses deterministic rules so the API can be developed and tested without a paid AI API or local model. The next increment can add an isolated AI provider adapter (for example an Ollama-hosted open model) behind the same service contract.

Do not couple the domain layer directly to Ollama or any external provider.

## Planned architecture

```text
Next.js
  -> Spring Boot /api/ai/work-plans/draft
      -> WorkPlannerService
          -> AI provider adapter (future)
              -> Ollama / open model (future)
```

## Next increments

- Add AI provider interface + Ollama adapter.
- Add structured JSON schema validation for model output.
- Add work-plan review/edit UI.
- Add explicit approve endpoint.
- Persist AI generation history and approval audit.
- Convert approved tasks into the existing Work Board.
- Add automated tests for malformed/unsafe model output.

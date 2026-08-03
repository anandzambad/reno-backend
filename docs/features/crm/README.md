# Reno CRM & Automated Follow-up

## Goal

Turn every new customer enquiry, estimate, quotation, booking and completed job into a CRM relationship with configurable automated follow-ups at 30, 60 and 90 days.

## Core lifecycle

```text
Lead / New Work
   -> Customer + Project
   -> Estimate / Quotation
   -> Booking / Work Order
   -> Completion
   -> CRM Follow-up Plan
   -> 30 days
   -> 60 days
   -> 90 days
   -> Repeat work / referral / retention
```

## Follow-up rules

Default cadence:

| Milestone | Suggested purpose |
|---|---|
| 30 days | Satisfaction check, unresolved issue, warranty/support check |
| 60 days | Maintenance reminder, related service recommendation |
| 90 days | Repeat service, seasonal service, referral/review request |

The cadence must be configurable by service/category and contractor. Do not hard-code all business rules into the frontend.

## CRM entities

### crm_contacts

- id
- customer_id
- contractor_id
- source
- lifecycle_stage
- preferred_channel
- consent_status
- last_contacted_at
- next_follow_up_at
- created_at
- updated_at

### crm_interactions

- id
- contact_id
- project_id
- channel
- interaction_type
- direction
- subject
- notes
- provider_message_id
- occurred_at

### crm_follow_up_plans

- id
- contact_id
- project_id
- trigger_type
- trigger_at
- status
- timezone
- created_at

### crm_follow_up_tasks

- id
- plan_id
- due_at
- milestone_days
- channel
- template_key
- status
- attempts
- sent_at
- provider_message_id
- failure_reason
- created_at
- updated_at

### crm_preferences

- contractor_id
- service_category
- enabled
- day_30_enabled
- day_60_enabled
- day_90_enabled
- email_enabled
- whatsapp_enabled
- sms_enabled
- quiet_hours_start
- quiet_hours_end

## Automation behavior

When a qualifying work item is completed, create an idempotent follow-up plan. The scheduler claims due tasks safely so multiple application instances cannot send the same message.

Use a transactional outbox/queue where practical:

```text
Business event
 -> DB transaction
 -> outbox event
 -> worker/queue
 -> notification provider
 -> delivery result
 -> CRM interaction
```

Use retry with exponential backoff and a dead-letter queue. A provider failure must not duplicate a customer task.

## Customer communication

Preferred channels:

1. WhatsApp Business Platform/API when explicit customer consent exists.
2. Email when available and consented.
3. SMS only where configured/legally appropriate.

Every automated message must include opt-out/unsubscribe handling appropriate to the channel and applicable regulations. Do not send marketing messages without required consent.

## Smart follow-up examples

### 30 days

> Hi {{customerName}}, how is your {{serviceName}} work going? Is there anything we can help you with?

### 60 days

> Your {{serviceName}} was completed about two months ago. Would you like to schedule maintenance or another related service?

### 90 days

> It's been around 3 months since your Reno project. Need another service, inspection, or repair? We can help.

Templates must be editable per contractor/service and should not claim warranties or guarantees unless configured.

## CRM dashboard

Contractor view:

- New Leads
- Active Projects
- Follow-ups Due Today
- Follow-ups Upcoming
- 30/60/90-day pipeline
- Contact history
- Last communication
- Next action
- Conversion/repeat-job metrics
- Opt-out/consent status

## REST API contract

```text
GET    /api/v1/crm/contacts
GET    /api/v1/crm/contacts/{id}
POST   /api/v1/crm/contacts
GET    /api/v1/crm/contacts/{id}/timeline
GET    /api/v1/crm/follow-ups?status=DUE
POST   /api/v1/crm/follow-ups/{id}/complete
POST   /api/v1/crm/follow-ups/{id}/reschedule
POST   /api/v1/crm/follow-ups/{id}/skip
GET    /api/v1/crm/preferences
PUT    /api/v1/crm/preferences
```

## Security

- Contractor can access only their own contacts/projects.
- Customer identity must come from authenticated context, not an arbitrary customer ID supplied by the browser.
- Audit automated and manual interactions.
- Protect PII and restrict exports.
- Encrypt secrets and provider credentials.

## Performance

- Index `next_follow_up_at`, `status`, `contractor_id`, `contact_id`.
- Fetch due tasks in batches.
- Avoid loading all CRM contacts into memory.
- Use pagination for timeline/history.
- Process notifications asynchronously.
- Use idempotency keys for provider sends.

## Product extensions

Future CRM intelligence can score customers based on recency, frequency, service history and engagement, then suggest the next best action. This must be explainable and must not make sensitive inferences.

## Implementation status

This document defines the approved CRM feature contract and automation behavior. Runtime integration should be implemented against the actual Spring Boot modules, database migrations, notification providers and Next.js UI, followed by unit/integration/concurrency tests and CI verification.

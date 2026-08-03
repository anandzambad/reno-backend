# Reno Dispute Management

## Objective

Provide a controlled, evidence-based process for customer/contractor disputes while protecting both parties and giving Reno a clear escalation and resolution workflow.

## Dispute lifecycle

```text
Issue reported
  -> Case created
  -> Evidence collection
  -> Initial triage
  -> Parties notified
  -> Contractor response
  -> Customer response
  -> Mediation / support review
  -> Resolution proposal
  -> Accept / Reject
  -> Escalation if unresolved
  -> Final decision
  -> Refund / adjustment / rework / closure
```

## Dispute categories

- Service quality
- Work not completed
- Work delayed
- Wrong service
- Quotation/price disagreement
- Invoice/payment disagreement
- Property/material damage
- Contractor no-show
- Customer cancellation
- Misrepresentation
- Safety incident
- Suspected fraud
- Harassment/threats
- Other

Safety, threats, suspected fraud and potentially criminal matters require immediate escalation and should not be treated as ordinary commercial disputes.

## Severity

| Level | Example | Response |
|---|---|---|
| P1 Critical | safety threat, serious property damage, suspected fraud | immediate support escalation |
| P2 High | major financial loss, severe service failure | priority human review |
| P3 Normal | quality, delay, invoice disagreement | standard workflow |
| P4 Low | minor issue / information request | normal support |

## Case data model

### disputes

- id
- case_number
- booking_id
- project_id
- customer_id
- contractor_id
- category
- severity
- status
- amount_disputed
- currency
- description
- resolution_type
- resolution_amount
- assigned_team
- assigned_agent_id
- opened_at
- response_due_at
- resolved_at
- closed_at
- created_at
- updated_at

### dispute_evidence

- id
- dispute_id
- uploaded_by
- evidence_type
- storage_key
- checksum
- description
- created_at

### dispute_messages

- id
- dispute_id
- sender_type
- sender_id
- message
- created_at

### dispute_events

- id
- dispute_id
- event_type
- actor_id
- metadata_json
- created_at

### dispute_resolutions

- id
- dispute_id
- resolution_type
- amount
- currency
- explanation
- proposed_by
- accepted_by
- accepted_at
- created_at

## Evidence requirements

Allow evidence such as:

- quotation
- estimate
- invoice
- payment receipt
- work order
- photographs
- videos
- customer/contractor messages
- delivery/material records
- completion proof

Preserve original timestamps/metadata where appropriate and calculate a checksum for stored files. Never allow one party to edit or delete the other party's evidence.

## Financial protection

If Reno holds or routes customer funds, dispute policy must define when funds can be paused, refunded, partially refunded or released. Do not implement a blanket automatic refund rule. Financial actions must be authorized, auditable and idempotent.

For payment-provider integrations, use provider-supported payment/refund APIs and reconcile results asynchronously.

## Resolution types

- No action / information provided
- Contractor rework
- Customer rework appointment
- Partial refund
- Full refund
- Credit/voucher
- Price adjustment
- Cancellation
- Escalation to external/legal process where appropriate
- Case closed — no fault established

## SLA and escalation

Create configurable SLAs by severity. Example defaults:

- P1: immediate human escalation
- P2: same-business-day response target
- P3: 1 business-day response target
- P4: 2 business-day response target

These are product defaults, not legal guarantees. Actual terms should be reviewed for the markets in which Reno operates.

## Anti-abuse controls

- rate-limit dispute creation
- detect duplicate disputes against the same booking
- require booking/project association
- maintain immutable audit trail
- prevent evidence tampering
- monitor repeated false/abusive claims
- protect agents and users from abusive content
- do not expose private evidence to unauthorized users

Do not automatically label a customer or contractor as fraudulent based only on a score. Human review is required for consequential decisions.

## REST API contract

```text
POST   /api/v1/disputes
GET    /api/v1/disputes
GET    /api/v1/disputes/{id}
POST   /api/v1/disputes/{id}/messages
POST   /api/v1/disputes/{id}/evidence
GET    /api/v1/disputes/{id}/timeline
POST   /api/v1/disputes/{id}/respond
POST   /api/v1/disputes/{id}/propose-resolution
POST   /api/v1/disputes/{id}/accept-resolution
POST   /api/v1/disputes/{id}/escalate
POST   /api/v1/disputes/{id}/close
```

## Roles

- Customer: open case, provide evidence, respond, accept/reject proposal.
- Contractor: respond, provide evidence, propose rework/resolution.
- Support agent: triage, communicate, request evidence, propose resolution.
- Dispute manager: review escalated cases and authorize defined financial outcomes.
- Admin: policy/configuration/audit access; should not bypass audit controls.

## Worst-condition playbook

For severe cases:

1. Freeze only the affected workflow/financial action where technically and legally appropriate.
2. Preserve evidence and audit logs.
3. Escalate to a human reviewer immediately.
4. Separate customer and contractor communication if direct contact is unsafe.
5. If there is a safety threat, suspected crime or immediate danger, advise the affected person to contact appropriate emergency/law-enforcement services; Reno should not attempt to adjudicate criminal matters itself.
6. For payment disputes, reconcile with the payment provider and follow applicable payment/refund rules.
7. Document the decision and evidence relied upon.
8. Notify both parties of the decision and available appeal/escalation path.

## Appeal

Allow one controlled appeal for defined case categories. Appeals must be assigned to a different reviewer where feasible and retain the complete original audit trail.

## Implementation status

This document defines the approved dispute-management contract and worst-case operating model. Runtime implementation must be integrated into the actual Spring Boot/Next.js modules with migrations, authorization, secure evidence storage, payment reconciliation, tests and CI verification before production use.

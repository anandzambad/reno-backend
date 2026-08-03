# Reno Document & Communication

## Scope

Contractors can create and manage Estimates, Quotations, Work Orders, Invoices, Payment Receipts and Warranty/Completion documents. Documents are rendered as PDF and can be delivered by Email or WhatsApp, with a secure customer-facing view.

## Lifecycle

```text
Estimate -> Quotation -> Customer Approval -> Work Order -> Invoice -> Payment Receipt -> Completion/Warranty
```

## Delivery channels

- PDF download
- Email with secure document link and PDF attachment
- WhatsApp Business Platform/API transactional template

## Delivery states

`CREATED`, `QUEUED`, `SENT`, `DELIVERED`, `VIEWED`, `FAILED`, `REVOKED`, `EXPIRED`

## Security requirements

- Never expose sequential database IDs in public document URLs.
- Use cryptographically random, expiring access tokens.
- Validate contractor/customer authorization on every API call.
- Do not put secrets or private provider credentials in frontend code.
- WhatsApp credentials remain server-side.
- Record audit events for creation, sending, viewing, approval, rejection and revocation.

## Suggested REST API

```text
POST   /api/v1/documents/estimates
GET    /api/v1/documents/{id}
PUT    /api/v1/documents/{id}
POST   /api/v1/documents/{id}/send/email
POST   /api/v1/documents/{id}/send/whatsapp
GET    /api/v1/documents/{id}/deliveries
POST   /api/v1/documents/{id}/approve
POST   /api/v1/documents/{id}/reject
POST   /api/v1/documents/{id}/convert-to-quotation
POST   /api/v1/documents/{id}/convert-to-work-order
POST   /api/v1/documents/{id}/convert-to-invoice
GET    /api/v1/public/documents/{token}
```

The exact route names should follow the existing Reno backend API conventions when implementation is integrated.

## Data model

### documents

- id
- document_number
- type
- status
- customer_id
- contractor_id
- project_id
- parent_document_id
- subtotal
- discount
- tax
- total
- currency
- valid_until
- public_token_hash
- public_token_expires_at
- created_at
- updated_at

### document_items

- id
- document_id
- description
- quantity
- unit
- unit_price
- tax_rate
- line_total

### document_versions

- id
- document_id
- version
- snapshot_json
- pdf_storage_key
- created_by
- created_at

### document_deliveries

- id
- document_id
- channel
- recipient
- provider_message_id
- status
- sent_at
- delivered_at
- viewed_at
- failure_reason
- created_at

### document_events

- id
- document_id
- event_type
- actor_id
- metadata_json
- created_at

## Provider architecture

Use adapters so the core domain does not depend directly on a provider:

```text
DocumentService
  ├── PdfRenderer
  ├── EmailNotificationProvider
  └── WhatsAppNotificationProvider
```

The WhatsApp implementation must use an approved WhatsApp Business provider/API and approved templates for transactional messages.

## Reliability

Sending must be asynchronous. Persist the delivery request before publishing a job. Use an outbox/queue pattern, retries with exponential backoff, idempotency keys and a dead-letter queue. A provider outage must not roll back document creation.

## Customer UX

The customer-facing secure document page should show:

- contractor/business identity
- document number and date
- project/site
- line items
- taxes/discounts
- total
- validity
- Accept / Reject / Request Changes where applicable
- PDF download
- audit timestamp

## Contractor UX

The contractor dashboard should show:

- Draft / Sent / Viewed / Accepted / Rejected / Expired
- email/WhatsApp delivery status
- resend
- copy secure link
- download PDF
- conversion to next document type
- reminder scheduling

## Implementation status

This is the approved feature contract and integration design. The existing repositories did not expose an indexed document/quotation/invoice implementation that could safely be modified automatically, so this commit intentionally does not claim that the runtime feature is fully integrated. Implementation should be done against the actual existing Spring Boot and Next.js package/module structure, followed by tests and CI verification.

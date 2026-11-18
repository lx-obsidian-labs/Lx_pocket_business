# 03 — Data specification
2026-11-18 | Proposed Room relational schema; not implemented

## Entities
All business records use UUID primary keys, business_id where relevant, created_at/updated_at UTC; local civil dates use ISO yyyy-MM-dd. Scope unique indexes by business. One business active in V1; design supports future separation.
| Entity | Core fields and constraints |
|---|---|
| Business | name, address, contact, registration_no?, vat_no?, vat_registered, currency, logo_attachment?, numbering policy |
| Customer | business_id, name, phone?, email?, address?, vat_no?, archived_at? |
| CatalogueItem | business_id, name, sku?, unit, unit_price_decimal, tax_category, archived_at? |
| TaxRate | category, rate_decimal, effective_from, effective_to?, jurisdiction; immutable history |
| Document | type QUOTE/INVOICE/CREDIT, status, customer_id, number?, issue_date?, due_date?, expiry?, currency, supplier_snapshot, customer_snapshot, source_quote_id?, correction_of_id?, net_minor, tax_minor, gross_minor, notes |
| DocumentLine | doc_id, position, item_id?, description snapshot, quantity_decimal, unit_price_decimal, discount_decimal, tax_category, tax_rate snapshot, net_minor, tax_minor, gross_minor |
| Payment | invoice_id, amount_minor positive, date, method, reference?, reversal_of_id?, created_at |
| Expense | supplier, date, category_id, currency, net_minor?, tax_minor?, gross_minor, memo?, attachment_id? |
| ExpenseCategory | business_id, label, archived_at? |
| Attachment | owner type/id, local relative path, MIME, bytes, sha256 |
| Sequence | business_id, document_type, prefix, next_integer, unique scope |
| AuditEvent | entity_id/type, action, reason?, timestamp, minimal change metadata; no sensitive logs outside DB |
| Settings | local preferences; DataStore for UI and legal preference versions |

Relations: customer 1:N documents; doc 1:N lines; invoice 1:N payments and credits; quote 0:1 converted invoice; attachment owner restricted; no cascade deletion of posted records. DB transactions enforce issuance/sequence and quote uniqueness; FK constraints and migrations tested. Draft number null. Snapshot historical names, VAT details, line pricing and tax so edits do not alter issued documents.
## Money
Never Float/Double. Persist money in integer minor units with currency; arbitrary-precision BigDecimal for quantity/unit price/rate; canonical decimal text or scaled integer. Proposed rounding: line net = qty × unit price − discount rounded HALF_UP to currency scale; tax = net × rate rounded per line; total is sum of rounded line amounts. Inclusive tax: derive net from gross/(1+rate), tax = gross−net; define rounding residual once, test against approved invoice examples. V1 one currency ZAR; no cross-currency totals. Discounts cannot yield negative net. Credit notes carry positive amounts with negative reporting sign; separate reversals. Validate bounds and overflow. 
## State transitions
Quote DRAFT → SENT → ACCEPTED/REJECTED/EXPIRED; accepted → CONVERTED; manual acceptance; prevent duplicate invoice creation. Invoice DRAFT → ISSUED; payment status derived from adjusted total minus posted payments/reversals; overdue if outstanding >0 and due_date before local today. Corrections use linked credit, never deletion; full credit voids outstanding subject to payment/refund rules. MVP disallows credit exceeding remaining invoice amount or creating unhandled refund balance; define refund workflow before relaxing. 
## Reports
Issued sales = invoice gross minus credited gross by issuance date. Collected cash = posted payments minus reversals by payment date. Expenses by expense date, not proof of actual payment; net cash movement label only if expenses are confirmed paid (otherwise show separately). Ageing by due date buckets current, 1–30, 31–60, 61–90, >90; totals reconcile to underlying outstanding. No profit or tax-return guarantee.
## South Africa
SARS requires specified tax-invoice information, full invoice above R5,000, abridged at/below threshold and special rule at R50 or less; VAT vendor issue within 21 days of supply. Prefer full template and add supply date field/validation. Non-vendor invoices must not charge VAT or claim tax invoice status. Standard VAT 15% per SARS, effective-date config and release recheck required; distinguish zero-rated, exempt, not applicable. Keep records/export for legal retention; no automatic purge. Sources: https://www.sars.gov.za/businesses-and-employers/government/tax-invoices/ ; https://www.sars.gov.za/types-of-tax/value-added-tax/ ; https://www.sars.gov.za/client-segments/record-keeping/
## Test cases
R100 × 2 less R20 = R180 net; 15% = R27; R207 gross. Inclusive R115 at 15% → R100 net/R15 tax. R207 invoice minus R100 payment = R107 due. Double issue/conversion attempts create exactly one record. Archive customer leaves PDF unchanged. Failed migration/restore preserves previous database. Test fractional qty, rounding ties, 0%, large values, reversal, credit, leap day and timezone boundaries.

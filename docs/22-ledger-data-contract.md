# 22 — Daily-first ledger data contract
2026-11-18 | Proposed technical design | M01–M09, U10. Supersedes invoice-only Payment design in doc 03 where inconsistent. Not implemented or accountant-approved.

## Model and canonical sources
Business (id, name, sector?, legal fields?, vatStatus UNCONFIRMED/NON_VENDOR/VENDOR, currency ZAR, logoAssetId?); Customer (id, businessId, displayName, normalizedPhone?, email?, billing fields?, archivedAt?); CatalogueItem and Document/DocumentLine as doc 03; Attachment immutable content hash and private relative path; Debt (id, businessId, customerId, origin MANUAL/INVOICE, invoiceId? unique, principalMinor, incurredDate, dueDate?, status derived); CashEvent (id, businessId, type RECEIPT/EXPENSE/SETTLEMENT/OWNER_IN/OWNER_OUT/REFUND, amountMinor positive, civilDate, method, customerId?, expenseId?, allocationId?, reversalOf? unique, requestId unique, createdAt); DebtAdjustment (id, debtId, amountMinor positive, kind CREDIT/REVERSAL, documentId? unique, reason); SettlementAllocation (id, debtId, amountMinor, cashEventId unique, reversalOf? unique). Expense (id, supplier?, category, incurredDate, grossMinor, paid state derived from linked cash events; MVP single full payment, no partial expense payment unless approved); Task/Schedule/Occurrence per doc 15; AuditEvent minimal metadata; Sequence per document type; legal preferences in DataStore plus accepted version record.
All entities scoped to one workspace for MVP. Foreign keys restrict deletion of posted financial rows. Money Long minor units, decimal quantity and tax calculation via BigDecimal, checked arithmetic and validated bounds. Civil dates independent from UTC audit timestamps. Index business/date, customer, outstanding debt, normalized phone, document number; archive instead of delete where referenced. Never use floating point for amounts.

## Write transactions
| Command | Atomic result |
|---|---|
| recordReceipt | One RECEIPT event, no debt or invoice required |
| recordExpense | Expense row plus one EXPENSE cash event only if confirmed paid |
| recordDebt | Debt row, no cash event |
| issueInvoice | Allocate immutable serial; create debt OR link eligible existing debt (same customer, currency, principal; no conflicting invoice); never a cash event |
| settleDebt | Validate 0 < amount <= current balance; one allocation and one linked SETTLEMENT event in same transaction; update reminder event |
| reverseSettlement | One linked reversal for allocation and cash event; reopen debt if appropriate; no delete |
| creditInvoice | Issue linked credit document, add adjustment; reject if amount exceeds outstanding or would require unsupported refund workflow |
| ownerTransfer | One OWNER_IN or OWNER_OUT event, excluded from operating receipt/spend totals |
| confirmExpensePaid | Create one linked cash event; unique active payment; reject duplicate |
| convertQuote | One invoice draft linked to accepted quote, does not post debt until issuance |
Every command carries unique requestId generated at user action; retry returns existing result. UI never posts amounts itself. Transactions serialize balance/sequence validation. After rollback no partial financial effect. Outbox events processed idempotently; no external communication or auto-money recording.

## Reports
Net cash movement = operating receipts + debt settlements − confirmed paid business expenses − actual recorded refunds, with reversals signed by original type. Owner transfers reported separately. Invoiced value and debts are not cash. Outstanding as of date = principal incurred by date − allocations effective by date − credit adjustments effective by date + applicable reversals; no negative debt, no future events. Historical balances reconstructed from event dates, not current status. Due date unknown = separate undated ageing bucket. Reports use half-open date intervals [start,endExclusive), signed event amounts and reconciled drill-down. No profit claim. Standalone receipts are not added again when an invoice is issued; paid-at-issue invoice would need a single atomic settlement path, not standalone receipt plus settlement. MVP forbids retroactively relinking arbitrary cash receipts until dedicated reconciliation design.

## Illustrative acceptance fixture (test only)
Nov 1 cash sale R850; Nov 2 paid expense R300; Nov 3 debt R1200; Nov 4 linked invoice for R1200; Nov 5 settlement R400; Nov 6 owner adds R500. As of Nov 6: receipts R1250, spent R300, operating movement R950, debt R800, owner contribution R500. As of Nov 3: receipts R850, debt R1200. Reverse Nov 5 settlement on Nov 7: debt R1200 and cumulative operating movement R550. Neither invoice nor quote adds cash. Unique link and request ID prevent duplicates. Negative, overpayment, overflow, stale balance, repeated worker and interrupted write tests mandatory.

## Restore and migration
Version Room schema and export schemas to source; migrations from every supported version tested with fictional fixtures. Disable destructive migration fallback. Restore into staged DB, validate schema, hashes, FK, allocations, serials and balance invariants; atomic replacement plus rollback. Restore older backup warns of lost changes and possible serial reuse outside app; require review of sequence floor before issuing new docs. Do not pretend local-only device can detect numbers issued elsewhere. No live sample seed. See 05/15/21. Exact schema, crypto implementation and accounting review pending.

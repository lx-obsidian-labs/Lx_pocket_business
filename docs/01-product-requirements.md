# 01 — Product requirements
2026-11-18 | Proposed research baseline | No app implemented

## Mission
A free Android business recordkeeping and document productivity tool for South African freelancers, tradespeople and microbusinesses. All core workflows work in airplane mode; online advertising is optional to the workflow, not to the business model. No account, paid API, bank access or cloud dependency for V1. Android phone and tablet; English, ZAR first.

## Personas and jobs
- Freelancer: quote work, invoice on acceptance, record EFT, see overdue debt.
- Tradesperson: add a customer on-site, price labour/materials, issue a PDF without network, attach expense receipt.
- Small retailer: record sales and expenses, maintain catalogue, reconcile money received; full stock control is deferred.
- Owner/bookkeeper: export period records, reconcile balances, retain copies and transfer to a replacement device.

## Functional requirements and acceptance
| ID | Workflow | Release acceptance |
|---|---|---|
| P01 | Onboarding/profile | Local business name, logo, contact, address, optional VAT/registration number, ZAR, numbering prefix; no account; complete offline. |
| P02 | Dashboard | Period filters, issued revenue, collected cash, unpaid/overdue totals, expenses; distinguish cash and accrual figures; drill through. |
| P03 | Customers | Create/edit/search/archive, contact and billing address, optional VAT ID; linked history, no cascading document deletion. |
| P04 | Catalogue | Product/service, SKU optional, unit, price, tax category; archive without changing historical prices. |
| P05 | Quotes | Draft, quantity, price, discount, tax, expiry, terms, preview, PDF, share; record sent/accepted/rejected; convert exactly once to linked invoice. Status manual, not verified remotely. |
| P06 | Invoices | Draft and edit, issue immutable serial and snapshots, due date, PDF, share, filter. Credit note instead of silently changing issued records. |
| P07 | Payments | Manual cash/EFT/other, date, reference, partial payments, balance, reversals; reject accidental overpayment in MVP; no card processing. |
| P08 | Expenses | Date, supplier, category, amount, optional tax and receipt; local attachments, filters, CSV. |
| P09 | Reports | Sales issued, payments collected, ageing, expenses, simple net cash movement; CSV/PDF; not formal profit or tax return. |
| P10 | Search/documents | Search by number/customer/date/status, preview and share with Android chooser; no automatic messages. |
| P11 | Backup/restore | User-initiated encrypted portable backup; validation, preview and replace-only restore with rollback; warn before uninstall/data loss. |
| P12 | Settings | Business profile, tax, numbering, appearance, optional biometric lock, legal/privacy choices, export/delete local records, support. |
| P13 | Ads | Consent-aware banners on dashboard/reports only; no ads on invoices, export, entry, onboarding or sensitive screens; offline/no-fill never blocks use. |
| P14 | Accessibility | TalkBack labels, 48dp targets, font scaling, contrast, tablet layout, no colour-only statuses. |

## Scope exclusions
No payroll, tax filing, automated bank feeds, payment collection, marketplace, multi-user sync, automated legal advice, inventory valuation, recurring invoices or AI. Consider these only after user validation, licensing and cost review. No mandatory advertising clicks or premium feature paywalls.

## Workflow rules
Drafts editable/deletable; issue is atomic and idempotent; issued numbers never recycled; quote conversion preserves source; payment balance derived from posted payments and adjustments. Use credit notes/reversals with reason. Sharing does not mark customer acceptance or actual delivery. Records remain on device except explicit export/backup and separately disclosed ad SDK traffic.

## Nonfunctional and release gates
Offline test for every core use case; migration/restore preserves data; no business data transmitted to ads; no production ad IDs in debug; no false financial claims. Target initial accessibility and crash-free usability; test on low-memory phone, tablet and Android API 26/36 and 16KB image. Performance goals: cold start under 3s and 10k-record local search under 500ms on a documented representative device; measure, do not claim until tested.

## Validation before implementation
Interview at least 5 target owners; observe invoicing and payment tracking, verify willingness to use local backups and accept ads; prototype usability with realistic fictional records. Validate whether stock control, credit notes, multi-currency and recurring documents are launch blockers. No invented interview results.

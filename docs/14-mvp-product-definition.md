# 14 — Evidence-led MVP product definition
2026-11-18 | Proposed scope for validation; overrides prior invoice-first emphasis only after owner review.
## Problem
Business owners without consistent records lack a dependable picture of cash received, spending and money owed. Existing free software already addresses many functions, but whether a daily-first workflow changes adoption is unproven (B01–B13, doc 13).
## Mission and promise
Record once, know what happened, know what needs attention next. Free, ad-supported, offline-first business assistant with no subscription, required account or document-count paywall. A business owner can start with a single cash transaction before setting up formal invoicing.
## Persona and example
Independent electrician: R850 cash job, R300 materials, R1,200 unpaid job. Enter receipt/spend in seconds, save customer once, create credit sale or invoice without double-counting, schedule follow-up; later record R400 settlement and see R800 remaining. Month close shows cash receipts R1,250, confirmed outflow R300, net cash movement R950, debt R800. Illustrative figures, not profit.
## Traceable MVP requirements
| ID | Problem(s) | Capability and acceptance |
|---|---|---|
| M01 | B01 B02 B12 | Start offline; choose simple Money In, Money Out, Someone Owes Me; optional demo; only business display name needed for ledger, formal profile deferred until issuing document |
| M02 | B03 B04 | Customer inline creation with display name required; phone/email optional, selected-contact import with explicit choice; detect likely duplicates, user decides; reuse history and common items |
| M03 | B01 B03 B11 | Cash entry amount, date, type, payment method, category optional, note/customer optional; save in one tap; undo via reversal; owner transfer separate; no invoice required |
| M04 | B04 B08 | Debt entry customer/amount/date/due date optional; partial settlement/receipt, balance recalculated, linked invoice if present; no double counting; zero debt removes reminders |
| M05 | B03 B08 | Quote/invoice draft, catalogue, reusable terms and discount settings, preview, issue immutable serial, PDF/share; accept manually, convert once; link existing debt or create one, never both |
| M06 | B05 B04 U02 | Today action queue, configurable due/reminder schedule, quote expiry/follow-up, promised payment date, snooze/complete; local notifications optional, next-launch catchup, no auto outbound messages |
| M07 | B05 U02 | Weekly/monthly recurring invoice draft template; owner approves each issue; recurring expense reminder creates review task not a fictitious payment; pause/edit/skip; stable occurrence ID |
| M08 | B01 B09 B11 | Daily/weekly/monthly overview and monthly pack: recorded receipts, paid expenses, transfers, debts/ageing, missing-data warnings, transaction CSV and PDF narrative; no AI or network needed |
| M09 | B06 B12 | Backup/restore with encryption, integrity checks, actual last verified timestamp, backup reminder; test new-device restore and failed restore rollback |
| M10 | B07 B10 B12 U01 | Consent-aware banner only Home/Reports; core unaffected; help, privacy choices, export, legal and data controls; no live ad IDs in development |
| M11 | B02 B03 | Accessible typography, TalkBack and keyboard focus, large controls, responsive layout, plain-language help; test under font scaling |
## Prioritization / scope cut
P0 pilot: M01–04, M06 basic due queue, M08 basic period view, M09, M11; P0 public release adds M05, monthly export, M10 and compliance/financial correction flows. P1 candidate: M07 recurring drafts and richer automation after prototype demonstrates demand (explicit owner request tracked, not rejected). No cloud AI, automatic WhatsApp/email, bank feeds, full double-entry accounting, tax submission, payroll, inventory valuation, signatures or payment gateways in initial release; investigate after evidence. Free access is nonnegotiable; AI narrative later only with production-authorized inference, consent and cost plan (doc12).
## Success and stop criteria
Pilot with 15–20 owners. Target >=80% complete first entry unaided within 20s, >=80% correctly identify cash vs debt, >=70% voluntary capture on 5 of 7 days; compare median time/errors against Shap Pocket, Cashbook and Zoho in same tasks. Recurring-draft and reminders task success >=80%, zero duplicate financial effects. These are proposed thresholds, not results. Proceed only if daily-first approach measurably outperforms closest rival on an important workflow or adoption; otherwise pivot or stop. No implementation authorization yet.

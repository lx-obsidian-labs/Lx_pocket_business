# 23 — Documents and monthly business pack
2026-11-18 | M04 M05 M08 U02 U10 | Proposed print specification. No PDF assets generated; tax templates require accountant review.

## Shared print system
A4 portrait; 16mm margins proposed; white paper, dark readable text, restrained olive/lime accent (not a dark app background); optional actual business logo top left, fit without distortion, text fallback business name. Title and identifier top right; issuer and recipient sections, clear item table, right-aligned amounts, footer page x of y. Use deterministic source values, stable serial/date, currency ZAR, Unicode font and no app advertisements/watermark. For drafts, label DRAFT. Snapshot issuer/customer/logo/line/tax and template version at issue; retain old logo and exact issued PDF or reproducible rendering. Preview before share/print. Print and export errors never change issue/payment status; retry uses same issued record. No generated customer names or transactions in live app.

| Output | Fields and rule |
|---|---|
| Quote | Quote number, issue/expiry date, issuer, customer, description, quantity/unit price, discount, applicable tax, totals, terms; not proof of sale/payment |
| Invoice | Invoice number, issue/supply/due dates, supplier and customer fields, detailed lines and totals, payment instructions, linked quote if any; non-vendor does not charge VAT or claim tax invoice |
| Credit note | Unique number/date, supplier/recipient, original invoice ref, reason, affected items, value/tax adjustment; immutable; legal field review |
| Payment receipt | Receipt ref/date, customer, invoice/debt ref, payment method/reference if provided, amount received, remaining debt; not a tax invoice; reversal visible |
| Customer statement | Customer, period, opening balance, debt/invoice, payments/credits, closing balance and dated transactions; reconcile to debt ledger |
| Monthly report | Business, period and generation time, recorded receipts/spend/net cash, owner transfers, unpaid balances as-of period end, ageing, categories, outstanding actions, data limitations and transaction count |
| Transaction CSV | UTF-8, documented headers: id,date,type,customer_ref,category,amount_minor,currency,status,reference; quote/escape all text; neutralize formula-like strings, distinguish signed amounts; no visual logo in CSV |

## VAT controls
SARS full tax invoice rules include supplier name/address/VAT number, recipient name/address and VAT number where recipient is a vendor, unique serial/date, description, quantity and value/tax/total. Full invoice for consideration >R5,000; abridged permitted <=R5,000; at <=R50 special evidence rule; issue within 21 days of supply for applicable vendor. Use full template by default, enforce applicable fields before issue, distinguish exempt/zero-rated/non-vendor, verify rates effective on supply and rounding with accountant. Source reviewed: https://www.sars.gov.za/businesses-and-employers/government/tax-invoices/ . Credit note and record retention details still require specialist review; do not assert tax certification.

## Monthly pack workflow
Home/Money → Reports → select month or custom period → calculate consistent database snapshot → show summaries and drill-down → preview PDF/CSV → owner chooses share/save/print. Optional local month-end task prepares a summary, never invents entries. Label completeness: “Based on the records you entered”; flag uncategorized amounts, missing due dates, unresolved unpaid expense and no activity. Plain English deterministic narrative e.g. “You recorded more spending than receipts this month” only if true. Do not call net cash profit, estimate tax or give unsupported financial advice. Compare previous period only if both periods have recorded data; avoid division by zero. Optional AI later per doc 12, not required.
## Acceptance
A4 long invoice >100 rows paginates, repeats table headings, totals only once; no clipping/overlap with logo or long business names; no ads; ZAR formatting, inclusive/exclusive/zero VAT; old invoice unchanged after profile edit; preview/share uses actual data; CSV row totals reconcile, malicious formula fixture neutralized; month-end leap date, empty month, partial debt and reversed payment tested. Printer failure and picker cancel do not mutate ledger. Exact typography and artwork after visual approval. Platform reading: https://developer.android.com/reference/android/graphics/pdf/PdfDocument ; https://developer.android.com/training/printing/custom-docs ; https://developer.android.com/training/data-storage/shared/documents .

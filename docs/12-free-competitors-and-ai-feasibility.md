# 12 — Free competitors and AI feasibility
2026-11-18 | Discovery addendum | No implementation authorized

## User constraint
No monthly subscription for LX Pocket Business. Keep core record capture, documents and reports free and offline. Advertising is the proposed monetization channel, not a condition of access. AI is a candidate enhancement, not a dependency for correct financial records.

## Verified competitor claims
| Product | Public pricing/features | Implication |
|---|---|---|
| Zoho Invoice ZA | R0; 500 invoices/year, 2 users, 3 projects; invoicing, expenses, reports and reminders | Free invoicing is established; avoid claiming Zoho requires payment |
| Zoho Books ZA | R0 up to R1m annual revenue; 1,000 invoices and 1,000 expenses/year, 1 user + 1 accountant, P&L and balance sheet | Free reporting already available; VAT tracking listed in paid Standard |
| RUMELA | R0/month, unlimited invoices/quotes/expenses, AI bookkeeping in six SA languages, forecasting and management reports; card fee 3.7% excl VAT, annual statement pack R599 once-off | Direct local free AI competitor; do not claim AI reports or multilingual AI are unique |
| GoBooks | R0/month with AI finance advisor and 50 AI document scans/month; optional R79/month for 500 scans | Free AI finance help already marketed locally; quotas may be a friction point |
| Anchor | R0 hosted, unlimited users/invoices/organisations, double-entry reports, VAT, reconciliation and API/MCP | No subscription or usage caps alone is not a defensible USP |
| Cashbook: Ledger + AI Chatbot | Play listing claims free offline transaction entry, PDF/Excel, AI assistant and reminders; store labels Contains ads despite description saying no ads | Direct adjacent daily cashbook competitor; contradictory ad claims require hands-on verification |
| Shap Pocket | Previous research identifies free offline no-account local business management | Test daily cash/debt workflow against closest offline competitor |

These are vendor/listing claims, not device-tested observations. Verify availability, limits, data handling, offline modes and AI behavior before making comparative marketing claims. Sources: https://www.zoho.com/za/invoice/pricing/ ; https://www.zoho.com/za/books/pricing/ ; https://rumela.co.za/pricing ; https://gobooks.co.za/ ; https://anchorapp.co.za/pricing ; https://play.google.com/store/apps/details?hl=en_ZA&id=gn.cashbook ; https://www.openworks.co.za/shap/

## Revised attraction hypothesis
Target people who do not yet keep records, not only owners priced out of accounting products. Test immediate feedback: record a cash sale or expense in seconds, see daily money in/out and outstanding debt, then turn a month of entries into an understandable report and next actions. Avoid calling cash movement profit; track business vs personal withdrawals and cash vs credit separately. The prior SESE findings (doc 11) indicate perceived relevance/scale is a larger stated barrier than time or skills; affordability is plausible but not established as a primary reason. Compare prototype with RUMELA, Shap Pocket and Cashbook rather than only Zoho. No unique feature claim until tests.

## Monthly business pack proposal
Deterministic local generation, no AI needed: period summary, sales/collections breakdown, expense categories, outstanding debtor ageing, owner contributions/withdrawals, and transaction register; export PDF/CSV; optional narrative generated from computed aggregates. Flag missing data, reconcile each total, show period and definitions. Do not label outputs formal financial statements, profit or tax advice unless appropriate accounting data/model and professional review exist. No hallucinated entries or AI-generated financial totals. A plain-language monthly summary may use fixed templates offline; optional AI explains patterns or suggests questions, with preview and user approval.

## NVIDIA NIM decision
Official FAQ states free NVIDIA Developer Program NIM is for research/prototyping/development/testing only. Serving real end-users is production use and requires NVIDIA AI Enterprise licensing according to the FAQ; the 90-day enterprise trial is not a permanent free entitlement. Source: https://docs.api.nvidia.com/nim/docs/product
Do not embed a shared provider key in an APK or assume free credits are production rights. Evaluate separately licensed on-device models (device performance, model license, accuracy) or production-authorized inference through secure proxy with quotas and cost budget; no business data sent without explicit informed choice and POPIA review. Use only necessary aggregates, not names or receipts. If no sustainable permitted provider, ship deterministic reports without AI. No new paid dependency authorized.

## Next validation gate
Test monthly pack versus plain cashbook with 15–20 owners, compare time-to-first-value and 7-day repeat use, measure report comprehension and perceived relevance. Ask willingness to use an ad-supported free app versus existing free competitors. Benchmark direct alternatives, confirm app name availability and decide go/pivot/stop. No field tests or production AI integration completed.

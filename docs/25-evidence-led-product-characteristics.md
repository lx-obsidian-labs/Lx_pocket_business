# 25 — Evidence-led product characteristics
2026-11-18 | Design principles proposed for owner review | No implementation authorized.

## Problem before design
Many small business owners do not consistently keep financial records and consequently may lack clarity about cash movement and debts. In SESE 2023, 71.8% of non-VAT-registered business operators kept no financial records (E01). In a historical 2017 survey analysis, 47.0% of reason respondents saw no need and 40.9% considered their business too small (E02). These are not evidence that any particular app solves adoption. Zoho, Shap Pocket, Cashbook and RUMELA already solve overlapping tasks; free access, AI and reminders individually are not a unique selling point.

## Product characteristics and design consequences
| ID | Characteristic | Problem/evidence | Design response and proof target |
|---|---|---|---|
| C01 | Immediate usefulness | B01 / E01 E02 | Home emphasizes money received, spent and owed; no mandatory invoice or accounting setup to record a sale. Test first entry and correct interpretation. |
| C02 | Plain-language and accessible | B02 B03 / E02 U07 | Common words, large targets, clear contrast, labels with icons, no unnecessary jargon; progressive disclosure of tax and formal fields. Test unaided completion and TalkBack. |
| C03 | Fast, low-effort and reusable | B03 / E02 E03 E04 U02 | Business name only required for setup; inline customer creation, recent items, reusable logo/terms, sensible defaults. Target first money entry within 20 seconds in usability tests, not a measured claim. |
| C04 | Financially clear and accurate | B04 B11 / E04 E01 inference | Show outstanding balance and partial payments; separate cash from invoice value, debt and owner transfers. Reconcile reports to canonical ledger; no double-counting, no fictional figures. |
| C05 | Proactive without loss of control | B05 B04 / E07 U02 | Today task queue, due-date reminders, promised-payment follow-ups, optional recurring drafts; owner confirms communications and postings. No silent sends, fake payments or guaranteed precise notification time. Recurring drafts remain P1 candidate. |
| C06 | Trustworthy and resilient | B06 / E05 E08 | Show last successfully verified backup, restore preview, recovery, historical document snapshots. Test wrong password, corrupted archive, interrupted restore and new-device recovery. |
| C07 | Free and predictable | B07 / E03 E06 U01 | No subscription, no document-count paywall; subtle consent-aware ads in Home/Reports only, never interrupt forms/export or offline tasks. Free rivals mean ads may hurt competitiveness; test acceptance. |
| C08 | Professional and personalized | B08 / E03 E04 U10 | Optional real business logo, reusable terms, preview, branded PDF quotes/invoices/receipts/statements/reports, editable draft. Historical issued records unchanged. Do not infer all competitors lack these capabilities. |
| C09 | Informative and actionable | B09 B01 B11 / E05 E02 U02 | Today/week/month/custom, drilldown and monthly business pack with plain-English deterministic explanation; no AI dependency, no profit claim on cash data. Test reconciliation and comprehension. |
| C10 | Offline-first and private | B12 B06 / E08 E10 U01 U05 | No required account/network; local records, user-controlled export and backups; separate ad SDK traffic; no Firebase setup in base. Test airplane mode. |
| C11 | Transparent and supported | B10 B06 / E06 U09 | Accessible help, clear errors, legal and privacy choices, visible recovery actions; no invented support SLA. |
| C12 | Consistent, calm and premium | U04 U06 U07 U08 U09 U10; owner requirement, not competitor evidence | Dark olive gradient, lime accents, original art, pulse/spin splash, truthful percentage, onboarding/terms flow, clean empty workspace. Motion must not block use or accessibility. |

## Evidence-to-design rules
1. Every new feature links to B problem and E evidence or U explicit owner requirement, then M requirement, C characteristic, S screen and T test. A review complaint is not proof of a missing competitor feature; e.g. Zoho's developer identified existing settings, so discoverability may be the issue.
2. Positive reviews are benchmarks: E03 praises ease, reliability and professional consistent output; E05 praises free interface. Preserve those qualities instead of treating all competitors as poor.
3. Do not add fields because they exist in competing accounting products. A field must support an actual task, legal requirement or explicit owner direction; defer it until required where practical.
4. Empty workspace is production default (U10). Illustrations on welcome may communicate benefits but never pretend to be financial records.
5. Do not present this characteristic list as established differentiation. Validate C01–C04/C09 with 15–20 owners, competing daily workflows and proposed repeat-use thresholds in doc 14. Review sample is preliminary; field interviews, systematic review sampling, competitor device tests, legal review and compatibility spike remain open.

## Primary sources
See [doc 13](13-problem-register-review-evidence.md) for E01–E10 and dated reviews, [doc 14](14-mvp-product-definition.md) for M01–M11, [doc 16](16-mvp-screen-specification.md) for routes, and [docs 17–21](21-business-setup-home-and-navigation.md) for owner UI refinements. Key review pages: https://apps.apple.com/za/app/zoho-invoice-maker-app/id413017364?platform=iphone&see-all=reviews ; https://apps.apple.com/za/app/invoice-generator-zoho/id1061863220 ; https://play.google.com/store/apps/details?hl=en_ZA&id=gn.cashbook ; https://play.google.com/store/apps/details?hl=en-ZA&id=com.aadhk.woinvoice .

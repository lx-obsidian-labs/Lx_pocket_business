# 13 — Problem register and review evidence
2026-11-18 | MVP discovery baseline | Read with 11 and 12; no field validation yet.
## Method and limitations
Read publicly surfaced dated reviews from Zoho Invoice, Zoho Invoice Generator, Cashbook and Invoice Simple, including positive counterexamples and developer replies. Review themes are observations, not representative frequency estimates. Vendor statements establish claimed features, not independent quality. No review authors' personal details retained. Capture date 2026-11-18. Expand to stratified 30 reviews/product and direct user interviews before scope signoff.
## Source register
- E01 Stats SA SESE 2023, p.6 and p.19: https://www.statssa.gov.za/publications/P0276/P02762023.pdf — 71.8% did not keep financial records; 74.4% lacked business bank account. National scale, not demand for our app.
- E02 Jonck & Nwosu (2022), 2017 SESE secondary analysis: https://jefjournal.org.za/index.php/jef/article/view/681/1374 — reasons: no perceived need 47%, business too small 40.9%, skills 6.8%, time 5.3% of 1,302 reason responses. Historical, not current causal evidence.
- E03 Zoho Invoice iOS ZA: https://apps.apple.com/za/app/zoho-invoice-maker-app/id413017364?platform=iphone&see-all=reviews — Sep 22 2025 praises simplicity, reliability and trust after other tools; Oct 12 2024 praises consistent documents after manual design; Jan 10 2024 requests mobile logo sizing, default terms and overall discount (developer explains existing settings and web-only logo adjustment); Dec 1 2023 requests phone-number sharing; Dec 1 2025 praises lack of subscription surprises. Some desired functions already exist but discoverability matters.
- E04 Zoho Invoice Generator iOS ZA: https://apps.apple.com/za/app/invoice-generator-zoho/id1061863220 — Aug 6 2025 requests customer phone fields and payment receipts; developer directs to full Zoho Invoice; Jul 28 review requests visible partial payments/balance (year not displayed).
- E05 Cashbook Play: https://play.google.com/store/apps/details?hl=en_US&id=gn.cashbook — Aug 15 2026 reports losing four days of data despite claimed daily backup; developer requested details, cause not established. Sep 5 2026 requests monthly filters across accounts and amount sorting; Aug 13 2026 praises free interface. ZA listing release notes describe Android 16 crash fixes and improved backup/import; not independently tested.
- E06 Invoice Simple Play ZA: https://play.google.com/store/apps/details?hl=en-ZA&id=com.aadhk.woinvoice — Aug 16 2026 reports repeated price increases and limits; Aug 8 2026 alleges duplicate subscription charges, developer promises investigation; Sep 14 2026 requests customer signatures and easier support. These are user allegations, not independently verified events.
- E07 Zoho reminders documentation: https://www.zoho.com/za/invoice/help/settings/reminders.html — automatic customer/owner reminders already available by invoice due or expected payment date.
- E08 Shap Pocket: https://www.openworks.co.za/shap/ — vendor claims free/no ads/no account/offline, backup reminders, tax invoice, PDF share and till. Team Shap additionally offers recurring invoice sending and job costing; do not attribute these team features to Pocket without testing.
- E09 RUMELA: https://rumela.co.za/pricing and https://rumela.co.za/product — vendor claims free AI bookkeeping, plain-language insights and alerts; payment processing and annual statement pack fees. AI alone not unique.
- E10 Cashbook ZA: https://play.google.com/store/apps/details?hl=en-ZA&id=gn.cashbook — claims local offline ledger, reminders and reports; store ad label conflicts with description. Must test.
- U01 Owner explicit: no subscriptions; free app with ads; Android/website distribution.
- U02 Owner explicit: effortless customer entry, automatic schedules/triggers, monthly business documents, traceable planning before code.
- U03 Owner explicit: every page/workflow documented, every document separate commit.
## Problem register (stable IDs)
| ID | Problem | Evidence | Response / success hypothesis |
|---|---|---|---|
| B01 | Owners see little value in keeping records at small scale | E01 E02 | Immediate cash/debt clarity from first entry; test comprehension and repeat use |
| B02 | Accounting jargon and lack of skills discourage adoption | E02 E09 | Plain-language labels, optional examples, progressive disclosure; test unaided tasks |
| B03 | Admin takes time and repeating customer/items feels wasteful | E02 E03 E04 U02 | Quick capture, one-time customer/item reuse, inline add, recent items; measure seconds |
| B04 | Money owed and partial settlements are hard to follow | E04 E07 | Customer debt timeline, partial payments, receipts, due reminders; reconcile every amount |
| B05 | Forgetting follow-ups/recurring tasks risks missed business action | E07 U02 | Local action queue, due reminders and recurring drafts; measure tasks completed; actual prevalence unknown |
| B06 | Losing records destroys trust | E05 E08 | Verified portable backup, freshness status, restore test and warnings; no silent overwrite |
| B07 | Subscription limits/pricing surprises deter some existing users | E03 E06 U01 | No subscription, no document-count paywall; ads never interrupt records |
| B08 | Professional documents and settings can be cumbersome | E03 E04 E06 | Reusable terms/logo, clear discounts, receipts, PDF preview; signature demand later validation |
| B09 | Period filtering and sorting affect month-end usability | E05 U02 | Monthly/weekly/custom period, category drilldown, export with reconciled totals |
| B10 | Customer support difficult or slow | E06 | In-app help and verified support route; no promised response SLA |
| B11 | Cash vs business/personal movement may be unclear | E01 E02; inference | Separate owner transfers from business receipts/spend; test whether users distinguish cash and profit |
| B12 | Internet or paid infrastructure could interrupt free workflows | E08 E10 U01; design constraint | Offline core, optional connectivity for sharing/ads; test airplane mode |
| B13 | Existing free competitors already meet many needs | E07 E08 E09 E10 | Benchmark actual user tasks; no unique feature claim; go/pivot/stop gate |
## Research backlog
Recruit 15–20 target owners including non-recordkeepers; test closest competitor devices, 30 recent reviews per product across ratings, document counterexamples, no real customer data. Record sample, date, app version, task time/errors, consent and whether pain is frequent. Do not equate national percentages with demand or review counts with prevalence.

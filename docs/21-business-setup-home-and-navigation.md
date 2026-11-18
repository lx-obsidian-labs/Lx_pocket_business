# 21 — Business setup, Home and navigation
2026-11-18 | Owner requirement U10 | M01 M03 M04 M05 M06 M08 M11 | Design specification, not implementation.

## Owner direction
After welcome, onboarding and terms acknowledgement, allow users to create their actual business profile, optionally upload their logo, choose a sector and add formal business information. Business name alone is sufficient to enter Home. No fake or placeholder financial records in the live workspace. Use the dark olive gradient and lime accents from doc 19. Produce a professional usable product, not a demo.

## S02 — Business setup
One coherent scrollable setup flow with progressive disclosure; suggested sections below. Only business display name is mandatory to continue. Save local draft across rotation, back and process death.
| Section | Fields and interaction | Rules |
|---|---|---|
| Identity | Business name, optional trading/legal name distinction in formal details | Trim name; reject blank; show name in Home and on documents. One workspace in MVP. |
| Branding | Optional logo; tap Add logo to select a local image, preview, replace, remove | No full storage/gallery permission; use system picker. Copy to private storage; validate image MIME, decode bounds and dimensions; preserve aspect ratio; strip metadata where appropriate. Proposed PNG/JPEG/WebP, max 10 MB input and 2048px normalized long edge; confirm limits in device testing. Never upload automatically. |
| Sector | Suggested options: retail, food/catering, construction/trades, beauty/personal care, professional services, transport, agriculture, education, other; optional custom text | Optional; no assumptions about tax status based on sector. May suggest editable expense categories; does not disable features. |
| Formal details | Expandable legal/trading name, business registration no., billing address, phone, email, VAT status and VAT number if applicable, default document terms and optional payment instructions | Optional during setup. Required applicable issuer/recipient and tax fields validated before formal document issue. Default currency ZAR; default VAT status unconfirmed, do not charge VAT until owner explicitly configures valid status. No bank account verification or sensitive banking credentials. |
| Finish | Lime Continue to business action | Enable once nonblank business name; atomically create exactly one workspace and mark onboarding complete; route directly to Home. No login, network, ads, or forced logo. |

### Logo/document behavior
Use uploaded logo consistently in every customer-facing printed/exported quote, invoice, credit note, receipt, statement and business PDF report; no LX advertising or platform watermark in those documents. CSV is machine-readable and not a visual logo format; use branded PDF for printing. Fit within a consistent header bounding box; preserve proportions, transparent backgrounds and legibility on white paper. If missing, render business name as a clean text header, no broken image or fictitious logo. Persist logo asset reference/content hash in issued document snapshot and preserve historic logo even after profile edit or removal. Historical issued PDFs must remain reproducible; draft/new reports use current logo. Backup contains referenced logo assets. Print via Android print/share facilities; validate PDF and test actual paper sizes. Review formal invoice compliance separately.

## S03 — Home dashboard
### Layout hierarchy
1. Top safe-area header: business logo if provided, otherwise business-name initials or neutral icon (not a fictitious logo); business name; optional settings entry.
2. Period control: Today, This week, This month, Custom; suggested default Today for daily capture. Show exact selected date range, local civil dates; week starts Monday (proposed). Period changes refresh summaries from local database.
3. Four compact summary metrics: Money received (actual recorded incoming business cash), Money spent (confirmed outgoing business cash), Net movement (received minus spent, not profit), Customers owe you (outstanding at end of period, an as-of balance rather than a period flow; label date). Owner contributions/withdrawals excluded from business operating movement and visible separately in ledger; unsettled expenses excluded from cash spend.
4. Primary quick actions: Money in, Money out, Someone owes me. Open corresponding forms with selected type; default transaction date today; save returns Home and refreshes real totals. No customer required for anonymous cash receipt.
5. Today: due customer follow-ups, quote expiry and other enabled tasks. Always based on today's date, independent of selected financial period; no false or generated tasks.
6. Recent activity: only actual entries, tap to inspect; empty state if none. Backup reminder/status only when applicable.
7. Optional consent-compliant banner after content or in a reserved safe position, never near quick actions or obscuring navigation; no ad during onboarding, no fake blank banner when offline/no-fill.

### Empty-state requirements
Fresh setup has zero financial rows, zero customer rows, zero documents, zero schedules and no demo transactions. Show R0.00 for computed zero aggregates only, with text "No activity yet" and prompt "Record your first sale or expense." Today: "Nothing to follow up today." Recent activity: "Your activity will appear here." Do not confuse zeros with missing/failed queries: show error/retry for actual storage failure. No fictional graph, customer, transaction, debt, invoice, logo or sample business in production workspace. Design fixtures may exist only in isolated preview/test source, never seeded into a release database. Optional demo in older docs is deferred; owner explicitly requests clean live workspace.

## Proposed bottom navigation
Five persistent destinations, icon + plain-English label, lime active indicator and accessible contrast. Preserve selected tab and back stacks; hide navigation on focused onboarding/full-screen document preview where appropriate. Tablet uses navigation rail. No ads over navigation.
| Tab | Destination | Contents |
|---|---|---|
| Home | /home | Overview, quick capture, Today actions, recent activity |
| Customers | /customers | Search, add, customer history, debt, follow-up |
| Money | /money | Receipts, expenses, debts, settlements, period filters, reports/monthly pack |
| Documents | /documents | Quotes, invoices, credit notes, receipts and document search |
| More | /more | Business profile, catalogue, reports shortcut, backups, settings, legal, help |
Order proposed by design, not explicitly approved: Money precedes Documents to emphasize daily-first purpose, superseding earlier order. Today accessible on Home; do not force a separate sixth tab. Final iconography/layout approval pending.

## Acceptance cases
T-SET01 name only → Home offline; no optional fields block. T-SET02 logo picker cancel leaves setup intact. T-SET03 valid logo copied locally, preview retained after reboot; invalid/oversize image rejected safely. T-SET04 repeated Continue creates one workspace. T-SET05 sector custom/skip, formal fields optional. T-SET06 missing applicable invoice information prompts before issue only. T-SET07 logo appears in all supported printed PDFs; remove logo and new PDF uses name, old issued invoice unchanged. T-SET08 restore restores historical logo references. T-H01 first-run database empty; all four metrics zero and no fabricated cards. T-H02 record actual R850 receipt, R300 expense and R1200 unpaid debt → received R850, spent R300, movement R550, debt R1200; settle R400 → received R1250, movement R950, debt R800 (test fixture only). T-H03 period boundaries and debt as-of match source rows; Today independent. T-H04 failed query not shown as zero. T-H05 bottom navigation, back stack, accessibility, small screens and offline/no-fill. T-H06 draft survives navigation/process death; no duplicate entries.

## Handover
Implement after scope and visual design approval. No app code, logo art or generated wireframes produced here. The sector list, picker limits, period default, and tab order are proposals. Actual document templates, exact screen spacing and dependency compatibility remain to be designed/tested. See docs 03, 14, 16, 18–20 for linked requirements; where inconsistent, U10 controls setup and live empty-state behavior.

# 09 — Delivery and test plan
2026-11-18 | no implementation or deployment
## Phase gates
| Gate | Deliverable | Exit evidence |
|---|---|---|
| G0 Research | PRD, market evidence, data, architecture, UX, security, distribution, legal, source register | Owner scope review; interviews and competitor scenario checks outstanding |
| G1 Design | Clickable flows, sample quote/invoice/credit, schema and rounding decisions, verified package ID, dependency spike | prototype completion tests, accountant template review, reproducible compatibility build |
| G2 Foundation | Kotlin project, Room, migrations, navigation, CI, private storage | unit and instrumented tests, offline startup, no secrets |
| G3 Core | customers/catalogue, quote-to-invoice, payments, expenses, reports, PDF/CSV | traceability for P01–P10, financial reconciliation and long-PDF tests |
| G4 Resilience | backup/restore, app lock, settings, accessibility | restore adversarial cases, migration from prior version, offline/no-network tests |
| G5 Monetization/legal | AdMob test integration, UMP, actual privacy/terms, website pages | network review, no business data in ads, consent tests, legal approval |
| G6 Release | signed AAB/APK, store listing, tester evidence, policy forms | API/16KB checks, pre-launch report, signature cross-update, publisher review, checksum |
| G7 Maintenance | bug/support process and release records | versioned schema/policies, regression tests, incident response |
## Test matrix
Unit: rounding and precision, tax modes, discounts, negative/overflow bounds, issue idempotence, quote conversion, payment reversal and ageing date boundary. DB: FK/unique constraints, transaction rollback, archive, migrations preserving issued snapshots. UI: first-run offline, process death/draft restore, long text, TalkBack, font scale, tablet, navigation, no ads/no-fill. Security: malicious imports, permissions, backup wrong password/tamper, file URI expiry, ad traffic with synthetic records. End-to-end: create customer, accepted quote, invoice, partial payment, expense, credit, report reconciliation, PDF/CSV, restore on new device. Release: debug/test ads only, production consent, signed artifact verification, website URL/404/SEO checks and store declarations.
## CI and change control
Every implementation PR links requirement ID and tests; no unreviewed deps. Gradle wrapper lint/test/build, instrumented suite, dependency/SBOM checks; store redacted logs only. No keystore, passwords, production keys or real customer records in Git. Separate debug/release config; versionCode increments. Record evidence, not promises. Tag release only after gates; update docs on feature/schema/policy changes.
## Handover
README index is the entry point. Track ADRs, open decisions, change log, release status and test evidence. Future contributors read PRD and data rules before code. Do not build until owner approves scope and research blockers resolved.

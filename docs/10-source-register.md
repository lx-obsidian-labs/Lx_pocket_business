# 10 — Source register and verification
Research date: 2026-11-18. Primary source claims below rechecked on this date; other documents contain implementation reading candidates, not evidence of tested compatibility.

| ID | Official source | Confirmed finding |
|---|---|---|
| S01 | [Android architecture](https://developer.android.com/topic/architecture/recommendations) | Layered architecture, repositories, Compose, single activity, Navigation 3 and lifecycle-aware state recommended. |
| S02 | [Play target API](https://support.google.com/googleplay/android-developer/answer/11926878) | New mobile apps and updates target API 36+ from 2026-08-31. |
| S03 | [SARS tax invoices](https://www.sars.gov.za/businesses-and-employers/government/tax-invoices/) | Full invoice above R5,000; abridged allowed at/below R5,000; at/below R50 special rule; vendor tax invoice within 21 days. Supplier/recipient details as applicable, serial/date, description, quantity, value and VAT fields. |
| S04 | [Mobile Ads data disclosure](https://developers.google.com/admob/android/privacy/play-data-disclosure) | Legacy SDK documentation version 25.5.0 describes automatic IP, interactions, diagnostic and device/account identifier collection and sharing. Recheck exact selected SDK. |
| S05 | [UMP setup](https://developers.google.com/admob/android/privacy) | Refresh consent each launch, show required form, expose privacy choices where required; debug geography/test devices. |

## Open verification
Before design approval: competitor hands-on scenario tests, interviews, accountant approval of VAT/credit notes/rounding, full dependency/toolchain compatibility matrix and license audit. Before release: actual package ID ownership, Play account eligibility, financial feature classification, 16 KB test, signed universal APK matching certificate, privacy and POPIA legal review, test ad/consent/network validation and website domain/AdMob publisher verification. Never claim completed items without evidence.

## Change management
When adding a source record URL, publisher, date accessed, requirement affected, exact finding in paraphrase and verification level (desk source / experiment / production evidence). No personal data or secrets in this public repository. Recheck time-sensitive policy and library versions before release.

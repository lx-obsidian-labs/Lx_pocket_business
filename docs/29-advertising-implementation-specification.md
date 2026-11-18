# 29 — Advertising Implementation Specification (ADS)
2026-11-18 | U01, M10, C07/C10 | Proposed release architecture; not implemented.

## Scope and placements
Free core, no subscription, no document limits. Native Android only. One adaptive banner per eligible destination: Home and Reports, separated from financial controls and bottom navigation, labelled as advertising where applicable. No ads on splash, welcome, onboarding, terms, business setup, customer screens, capture/payment forms, reminders, backup, authentication/lock, PDF preview, documents, exports, print or share. No interstitials, app-open ads, rewarded ads, forced clicks, countdown or incentives. On no-fill/offline/error collapse reserved space without covering controls or disrupting scroll. Keep ads off until onboarding complete and app unlocked. Users who decline personalized advertising retain all features; show ads only in a mode permitted by consent and applicable policy.

## Architecture and integration
AdsGateway interface exposes eligible placement state, initialize once, load/dispose and privacy options. No financial/customer fields in its inputs, logs, custom targeting or analytics. No ad call in splash startup path. Select stable Google Mobile Ads SDK variant and UMP after dependency spike; do not mix incompatible APIs. Add correct owner AdMob application ID manifest/config and separate banner unit IDs for Home/Reports at release; debug uses official sample IDs/test devices, not production requests. Ad IDs are identifiers, not signing secrets; owner manages account and production configuration. Use SDK-supported adaptive banner integration with lifecycle disposal on navigation and no duplicate request on Compose recomposition. Reserve space only while eligible, avoid accidental clicks and use normal SDK refresh behavior; no artificial impressions or repeated retries.

## Consent state machine
After onboarding and safe destination: requestConsentInfoUpdate per app launch; loadAndShowConsentFormIfRequired; expose Settings > Privacy options when required by SDK; use showPrivacyOptionsForm. Check canRequestAds before requesting, prevent redundant initialization/requests across callbacks. On error, conservative default is no ads unless current SDK permits and reviewed policy supports prior consent. Never use locally invented consent status or conflate terms acknowledgement with advertising choices. No core feature gated on consent. If consent changes, dispose/re-evaluate ad views; no silent reactivation outside SDK rules. Audit SDK automatic initialization, manifest permissions, actual data traffic and regional consent configuration before release.

## Test matrix
ADS-01 new install/offline: no ad, all tasks work. ADS-02 consent required/declined/accepted and not required: allowed requests only, no core gate. ADS-03 expired consent, SDK failure, privacy options change: no unsafe request. ADS-04 navigation, rotation, background/foreground and recomposition: no duplicate loads or leaked activity. ADS-05 no-fill: no blocked UI. ADS-06 debug uses test IDs; never click live ads. ADS-07 synthetic financial records never enter ad parameters/logs/network payload. ADS-08 accessibility, font scaling and banner separation. ADS-09 APK/AAB actual manifest and Data safety disclosure consistent; verify app-ads.txt on developer website and AdMob app readiness. ADS-10 signed website distribution and Play listing compatibility, supported-store verification requirements rechecked; no guaranteed fill/revenue.

## Official references
https://developers.google.com/admob/android/privacy
https://developers.google.com/admob/android/banner
https://developers.google.com/admob/android/test-ads
https://developers.google.com/admob/android/privacy/play-data-disclosure
https://support.google.com/admob/answer/9363762
Final SDK versions, account setup, production ad IDs, privacy review and testing outstanding. No live ads or account created.

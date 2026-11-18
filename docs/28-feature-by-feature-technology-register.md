# 28 — Feature-by-feature technology register
2026-11-18 | Proposed base stack, desk research | M01–M11, U04–U12, S00–S27. No code, installed dependencies or successful build yet.

## Platform scope U12
Owner explicitly requires Android only for now. Native Kotlin Android application; Android phones/tablets, Google Play AAB and signed website APK. No iOS, Xcode, Apple signing, cross-platform port or App Store release in scope. Android signing remains a separate owner-controlled release gate; see 06 and official signing guide. Never commit signing keys. Firebase may be integrated by owner after handover, but base must build without it.

## Selection principles
Prefer maintained Android platform/AndroidX tooling, offline execution, no per-user API charges, minimal permissions, stable dependencies and portable data. Free SDK is not free hosted service. Exact compatibility, dependency licence inventory and costs must be verified before production. See 24 for candidate versions, not locked dependencies.

| Feature/screens | Proposed tools | Alternative and reason | Acceptance gate |
|---|---|---|---|
| Foundation | Kotlin, Android Studio, Gradle/AGP, JDK17 | Native rather than Flutter/React Native; owner preference and platform integrations | clean build, compatible compiler/KSP, version lock |
| UI/navigation all screens | Jetpack Compose, Material3, Navigation3, ViewModel, coroutines/Flow | XML/Navigation2 possible; Compose and Navigation3 align with Android architecture guidance | responsive UI, process death, tab state, large text |
| Splash S00 | Core SplashScreen, Compose Animatable/graphicsLayer, progress | Lottie/Rive unnecessary for pulse/spin | real milestone percentage, reduced motion, startup benchmark |
| Welcome/onboarding/setup/terms S01/S02/S27 | Compose, local strings, DataStore and atomic Room workspace setup | no Firebase Auth/Firestore; SharedPreferences less suitable for reactive state | offline first run, terms version and no placeholder data |
| Business logo S02/S19 | Activity Result Photo Picker, SAF fallback, image decoding, private files | no broad gallery permission or cloud upload | validated size, aspect, historical snapshot |
| Customers S05–S07 | Room SQL, Compose form, selected-contact intent where supported | no full address-book access; manual fallback | duplicate warning and inline creation |
| Money/debt/expense S04/S08/S09/S16/S25 | Room/SQLite transactions, Kotlin Long minor units, BigDecimal, java.time, UUID | no floats or cloud dependency | atomic posting, reconciliation, reversal, idempotency |
| Catalogue and documents S10–S13/S18/S26 | Room snapshots, Kotlin document domain, PdfDocument/Canvas, PdfRenderer candidate | WebView printing/third-party PDF engine possible; native avoids extra licence, requires pagination | multipage/Unicode/logo/tax checks |
| Export/print/share | PrintDocumentAdapter, PrintManager, SAF ACTION_CREATE_DOCUMENT, FileProvider, Sharesheet | no direct WhatsApp API or broad filesystem permission | share only by user, correct PDF, no ledger mutation |
| Today/reminders/recurrence S14/S15 | WorkManager unique work, NotificationCompat, Room outbox, java.time | no exact alarm or cloud scheduler; ordinary tasks need no exact timing | retries, denied permission, launch reconciliation, no duplicates |
| Monthly pack S17 | Room aggregates, pure Kotlin calculations/templates, native PDF and UTF-8 CSV | external BI/AI not necessary; charts only when useful and accessible | signed totals, period/as-of debt, no fictitious data |
| Backup/restore S20 | SAF, versioned archive, authenticated encryption (AES-GCM candidate), reviewed password KDF, integrity manifest | avoid cloud requirement; Keystore device-bound keys cannot decrypt on new phone | independent crypto review, tamper/rollback/portable restore |
| Local lock S22 | AndroidX BiometricPrompt/device credential, Android Keystore where appropriate | no Firebase Auth; UI lock alone is not DB encryption | API26+ behavior, enrollment changes, recovery |
| Settings/help/legal S21–S24 | DataStore, bundled text, browser/email intents | no remote CMS needed | offline policy/help and explicit external actions |
| Monetization S03/S17 | Google Mobile Ads + UMP, isolated AdsGateway, adaptive banners | no interstitial/rewarded ads; compare SDK generations at build spike | consent, demo IDs, no sensitive data, offline/no-fill, 16KB |
| AI future | deterministic Kotlin monthly narratives now; production-authorized inference only if feasible | NIM developer entitlement not a production licence (doc12); local models candidate | licence, cost, privacy, accuracy, device feasibility |
| Firebase future | interface boundaries, optional owner-managed integration | no plugin, config or SDK required now | debug/release base builds without Firebase |
| Search/filter | indexed Room queries, FTS only if needed | no hosted search | latency, normalized customer details, date boundaries |
| Accessibility | Compose semantics, TalkBack, scalable sp and system animation settings | no custom accessibility layer | contrast, 48dp targets, 200% reflow, focus order |

## Design, QA, and distribution tools
- Design: editable SVG or layout source; Inkscape candidate for genuine vector reconstruction; original generated illustrations with high-resolution masters, PNG/WebP exports. No invented editable vector source from raster. Deliverables and filenames in 26–27.
- CI: GitHub Actions, Gradle wrapper, version catalog, dependency verification, Android lint, JUnit, AndroidX test/Compose UI test, Macrobenchmark. Emulator and actual Android devices; no claim tests executed.
- Packaging: Gradle assemble/bundle, Android Studio signing or owner-controlled secure CI signing, apksigner verification, bundletool, SHA-256 checksum. Play App Signing key plan must preserve signature compatibility with website APK before first release; never use upload key as if identical to Play app signing key.
- Documentation: Markdown in docs/, downloadable PDF/ZIP design and research packages after creation and validation. Website remains official distribution surface.
- Security/licensing: audit AndroidX and transitive OSS notices, Gradle dependency graph, SDK terms, native binaries/16KB, ad/privacy disclosures and any artwork rights. Most AndroidX components are Apache 2.0; verify per artifact, do not assume every SDK shares licence. No paid service needed for base core; ad network, distribution and future inference have separate terms.

## Primary official reference register (desk review 2026-11-18)
- https://developer.android.com/topic/architecture/recommendations — Compose, Navigation3, repositories, Flow.
- https://developer.android.com/training/data-storage/room — SQLite abstraction and migrations; current examples Room3, earlier Room2 candidate in doc24 must be compared before pinning.
- https://developer.android.com/topic/libraries/architecture/datastore/
- https://developer.android.com/develop/background-work/background-tasks/persistent
- https://developer.android.com/develop/ui/views/launch/splash-screen
- https://developer.android.com/training/data-storage/shared/photopicker
- https://developer.android.com/training/data-storage/shared/documents-files
- https://developer.android.com/reference/android/graphics/pdf/PdfDocument
- https://developer.android.com/training/printing/custom-docs
- https://developer.android.com/reference/androidx/core/content/FileProvider
- https://developer.android.com/privacy-and-security/keystore
- https://developer.android.com/identity/sign-in/biometric-auth
- https://developers.google.com/admob/android/banner
- https://developers.google.com/admob/android/privacy
- https://developers.google.com/admob/android/test-ads
- https://developer.android.com/studio/publish/app-signing
- https://developer.android.com/guide/app-bundle/faq
Additional technical sources in docs 15, 17, 18, 23, 24. Final tool versions, exact backup KDF and formats, PDF pagination feasibility, test evidence and legal approval remain open. No application or artwork produced.

# 24 — Implementation blueprint and dependency verification
2026-11-18 | Desk-verified candidate versions, NOT a resolved dependency graph or tested build.

## Candidate build matrix
Official AGP 9.3 release notes specify Gradle 9.5.0, JDK 17, build tools 36.0.0, max API 37. Select AGP 9.3.x stable patch after checking current releases; wrapper 9.5.0 candidate, minSdk 26 and compile/target 36 subject to Play release check. AGP 9.x built-in Kotlin configuration must be checked before applying separate Kotlin plugin; align Compose compiler and KSP with selected Kotlin version. Do not pin incompatible versions from different release trains.
| Component | Candidate | Evidence/status |
|---|---|---|
| Compose BOM | 2026.09.00 | Official setup and BOM docs list it; BOM coordinates do not include compiler |
| Room | 2.8.5 | AndroidX stable list 2026-09-09; choose Room 2 over migration to Room3 for base |
| Navigation3 | 1.2.1 | AndroidX stable list 2026-11-04; evaluate saved-state |
| Benchmark | 1.5.0 | stable list 2026-09-09; separate benchmark module |
| Lifecycle, DataStore, Core SplashScreen, WorkManager, Activity, Biometric, coroutines, KSP | stable versions after matrix check | Exact versions unresolved; verify minSdk and compiler |
| Mobile Ads SDK + UMP | choose supported stable SDK | verify disclosure, consent, minSdk and 16KB native compatibility; isolate |
| PDF/printing, SAF, photo picker, FileProvider | platform/AndroidX | no third-party PDF SDK proposed |
Sources checked: https://developer.android.com/build/releases/agp-9-3-0-release-notes ; https://developer.android.com/jetpack/androidx/versions/stable-channel ; https://developer.android.com/develop/ui/compose/bom ; https://developer.android.com/develop/ui/compose/setup-compose-dependencies-and-compiler ; https://developers.google.com/admob/android/banner ; https://developers.google.com/admob/android/test-ads .
## Proposed source layout
app/src/main/.../ui/{theme,navigation,splash,onboarding,home,customers,money,documents,settings}; domain/{model,money,usecase,report,validation}; data/{db,dao,entity,migration,repository,storage,backup}; platform/{notifications,print,share,picker,ads}; core/{clock,ids,result}; resources/{drawable,values,raw}; app/src/test, app/src/androidTest, benchmark; docs for traceability. Begin single app module with package boundaries; split only when justified. Manual DI composition root injects Clock, repository interfaces, AdsGateway, OnboardingRepository, scheduler and printer. Firebase not required; extension boundary documented in doc18.
## Contracts
ViewModel immutable UiState and one-time effects; lifecycle-aware collection, saved state for drafts. Repositories expose Flow reads and suspend commands; command results typed Validation/Conflict/Storage/Permission/Success. Domain money pure functions, no Android dependencies. Room single source of truth, queries on appropriate dispatcher; no main-thread IO. WorkManager jobs reconcile idempotent event outbox. Export/backup uses consistent read snapshot, private temp files and user-selected destination. Ads initialized after consent, no sensitive payload, absent network not an error for core. Debug build uses official demo IDs (banner ca-app-pub-3940256099942544/9214589741); production IDs from owner config, no live click tests.
## Build spike gate (not run)
1. Confirm package ID and supported device range, select AGP patch/Kotlin/KSP compatibility and pin Gradle wrapper checksum/version catalog.
2. Generate clean project in implementation phase; resolve dependencies, inspect transitive graph and licences, vulnerability scan and check native 16KB compatibility; document exact version and SHA.
3. Run assembleDebug, lint, unit tests, instrumented API26/36, clean offline core launch; verify no Firebase config; AAB release dry-run only once signing decision made.
4. Record actual CI logs, dependency verification metadata and SBOM, test startup with Macrobenchmark StartupTimingMetric. No success claims before execution.
Android quality docs recommend 48dp targets and 4.5:1 small-text / 3:1 large-text contrast: https://developer.android.com/develop/adaptive-apps/quality-guidelines/core-app-quality ; benchmark: https://developer.android.com/topic/performance/benchmarking/macrobenchmark-overview .

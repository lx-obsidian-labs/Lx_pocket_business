# 06 — Distribution and monetization
2026-11-18 | research baseline, no publishing performed
## Android and signing
New phone apps target Android 16/API 36 or higher since 2026-08-31; min API 26 proposed. Test edge-to-edge, adaptive layouts, runtime permissions and 16 KB memory page sizes including any native libraries. Android docs require 16 KB compatibility for API 35+ on 64-bit Play devices; current docs mention update enforcement February 2027; design for it now. Use one permanent package ID (proposed za.co.lxobsidianportal.pocketbusiness; verify ownership/availability). AAB for Play; signed universal APK for official website. Prefer Play-generated signed universal APK if available; verify cert SHA-256 and package ID against Play installs, otherwise decide signing ownership before enrollment. Never distribute upload-key APK as if equivalent to app-signing key. Use apksigner verification and monotonic versionCode; test cross-channel upgrade. No in-app sideload updater in Play build. Keep keys out of repo.
## Google Play gates
Create listing, developer verification, contact and website, privacy URL, Data safety, ads declaration, financial features declaration, content rating, target audience, app access and store assets. Review SDK/permission policies and pre-launch report. Personal accounts created after 2023-11-13 need at least 12 opted-in closed testers continuously 14 days before applying for production access; confirm account type. Android developer verification initially applies in select countries since 2026-09-30; broader rollout 2027; register package in Play Console for dual distribution. Recheck policies before actual release.
## Website
Add routes /pocket-business, /download, /privacy, /terms, /advertising, /licenses, /support, /docs and /changelog under product prefix on www.lxobsidianportal.co.za. Keep existing routes, SEO and sitemap intact; additive links only. Product screenshots from actual build, system requirements, download checksum, signing fingerprint, version/date, changelog, Play link when live. Host APK as HTTPS release artifact with correct MIME, no misleading download ads. Root /app-ads.txt shared across products: preserve existing authorized seller lines; publisher snippet from actual AdMob account. Verify crawler and readiness review; no invented publisher ID. 
## AdMob
Free core with subtle adaptive banners on dashboard/reports, no full-screen or rewarded ads in V1. No ads in input/payment/invoice/export/backup/legal screens; reserve safe spacing; no accidental click inducement. Offline, consent denied, no-fill, load failure => content works normally. UMP refresh consent at launch and offer privacy options when required; ad requests only when SDK says permitted and legal review approves. Consider disabling ad ID if feasible; verify resulting ad behavior. No personalized targeting based on business content. Use test ads and debug geography during development, never click live ads; production IDs via release config. SDK collects/shares IP, interactions, diagnostics and identifiers by default per Google disclosure; validate exact SDK version, actual configuration, legal bases and Data safety categories before submission. No revenue promise; model impressions × fill × eCPM/1000 using observed metrics after release.
## Sources checked
https://support.google.com/googleplay/android-developer/answer/11926878
https://developer.android.com/guide/practices/page-sizes
https://support.google.com/googleplay/android-developer/answer/14151465
https://support.google.com/googleplay/android-developer/answer/10144311
https://developer.android.com/developer-verification
https://developers.google.com/admob/android/privacy
https://developers.google.com/admob/android/privacy/play-data-disclosure
https://support.google.com/admob/answer/14538460
https://support.google.com/admob/answer/10564477
Additional release checklist reading: https://developer.android.com/studio/publish/app-signing ; https://support.google.com/googleplay/android-developer/answer/13849271 ; verify exact signing/export procedure, current financial declaration and ad policies in Console.

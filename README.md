# LX Pocket Business

Offline-first Android recordkeeping for South African freelancers and small businesses. Core use requires no account, internet connection or paid API. Android only for the current release.

## Current status
The app is implemented with onboarding, local business profile, dashboard, customers, money ledger, documents, settings, local app lock and local Room persistence. Release packaging is configured for an upload-signed Android App Bundle.

## Documentation
| Document | Purpose |
|---|---|
| [01 — Product requirements](docs/01-product-requirements.md) | Feature scope, personas, acceptance criteria, exclusions |
| [02 — Market and validation](docs/02-market-and-validation.md) | Alternatives and discovery tests |
| [03 — Data and financial rules](docs/03-data-and-financial-rules.md) | Entities, state transitions, monetary invariants, tax |
| [04 — Architecture and dependencies](docs/04-architecture-dependencies.md) | Android design and candidate libraries |
| [05 — Security and privacy](docs/05-security-privacy.md) | Threat model, backup, storage, data boundaries |
| [06 — Play, website and ads](docs/06-play-website-ads.md) | Dual distribution, signing, AdMob and compliance |
| [07 — Legal policy specification](docs/07-legal-policy-specification.md) | Terms, privacy, licences and review gates |
| [08 — UX workflows](docs/08-ux-workflows.md) | Navigation, screen and accessibility requirements |
| [09 — Delivery and test plan](docs/09-delivery-and-test-plan.md) | Development phases, tests, release gates |
| [10 — Source register](docs/10-source-register.md) | Confirmed official references and remaining checks |
| [11 — Problem evidence and review audit](docs/11-problem-evidence-and-review-audit.md) | National evidence, local alternatives and differentiation hypothesis |
| [12 — Free competitors and AI feasibility](docs/12-free-competitors-and-ai-feasibility.md) | Free pricing, AI reporting and NVIDIA production constraints |
| [13 — Problem register and review evidence](docs/13-problem-register-review-evidence.md) | Stable B/E/U identifiers, dated review observations and explicit owner requirements |
| [14 — MVP product definition](docs/14-mvp-product-definition.md) | Revised daily-first scope, measurable criteria and prioritization |
| [15 — Automation and customer workflows](docs/15-automation-and-customer-workflows.md) | Customer entry, trigger/action rules, scheduling and tests |
| [16 — MVP screen specification](docs/16-mvp-screen-specification.md) | Proposed screens, routes, actions, states and workflow map |
| [17 — Splash screen and motion design](docs/17-splash-screen-motion-design.md) | Logo pulse, edge glow, spin and exit transition |
| [18 — Welcome, onboarding and Firebase handover](docs/18-welcome-onboarding-and-firebase-handover.md) | First-run flow, local state, no mandatory login |
| [19 — Brand colour and welcome visuals](docs/19-brand-colour-and-welcome-visuals.md) | Lime accents, dark olive gradient, numeric progress and artwork direction |
| [20 — Terms acknowledgement and first-run flow](docs/20-terms-acknowledgement-and-first-run-flow.md) | Scrollable terms, explicit acceptance, local record and first-run gate |
| [21 — Business setup, Home and navigation](docs/21-business-setup-home-and-navigation.md) | Business setup, empty dashboard, bottom tabs and document branding |
| [22 — Daily-first ledger data contract](docs/22-ledger-data-contract.md) | Canonical cash/debt events, atomic writes, reconciliation and migration |
| [23 — Documents and monthly business pack](docs/23-document-templates-and-monthly-pack.md) | Branded print templates, VAT rules, monthly reports and export tests |
| [24 — Implementation blueprint and dependency verification](docs/24-toolchain-and-implementation-blueprint.md) | Toolchain, code boundaries and compatibility gates |
| [25 — Evidence-led product characteristics](docs/25-evidence-led-product-characteristics.md) | Product characteristics linked to evidence and owner requirements |
| [26 — Visual design system and brand board brief](docs/26-visual-design-system-and-brand-board-brief.md) | Colour tokens, layout, typography, components and asset inventory |
| [27 — Downloadables and delivery manifest](docs/27-downloadables-and-delivery-manifest.md) | Design assets, archives, signed release packages and exports |
| [28 — Feature-by-feature technology register](docs/28-feature-by-feature-technology-register.md) | Native Android tools, alternatives and validation gates |
| [29 — Advertising Implementation Specification](docs/29-advertising-implementation-specification.md) | AdMob placements, consent, SDK boundaries and acceptance tests |
| [30 — Authentication and Access Control Guidelines](docs/30-authentication-and-access-control-guidelines.md) | Local lock, account-free base and future owner-managed authentication |
| [31 — Home dashboard implementation](docs/31-home-dashboard-implementation.md) | Dashboard hierarchy, financial rules and verification record |

## Build and release
Use the bundled JDK:

```powershell
.\gradlew.bat --no-daemon "-Dorg.gradle.java.home=C:\Users\KAIRO VILANE NATHAN\.jdks\openjdk-24.0.2+12-54" :app:assembleDebug :app:testDebugUnitTest :app:lintDebug --console=plain
.\gradlew.bat --no-daemon "-Dorg.gradle.java.home=C:\Users\KAIRO VILANE NATHAN\.jdks\openjdk-24.0.2+12-54" :app:bundleRelease :app:assembleRelease :app:lintVitalRelease --console=plain
```

Release output:

- App Bundle: `app/build/outputs/bundle/release/app-release.aab`
- APK: `app/build/outputs/apk/release/app-release.apk`

Signing is configured through a git-ignored `keystore.properties` file at the repository root. The upload key is stored outside the repository. Do not commit keystores, passwords, ad IDs or production credentials.

Before upload to Play Console:

1. Privacy policy: https://www.lxobsidianportal.co.za/pocket-business-privacy
2. Terms and conditions: https://www.lxobsidianportal.co.za/pocket-business-terms
3. Support email: `support@lxobsidianportal.co.za`
4. In Data safety, declare that the app uses the Advertising ID because AdMob is integrated.
5. Verify the merged release manifest contains `com.google.android.gms.permission.AD_ID`.
6. Complete content rating, target audience, financial features and app access declarations.
7. Add store listing text, screenshots and feature graphic.
8. Upload `app-release.aab` as a new release.
9. Keep the upload keystore and `keystore.properties` backed up securely.

## Advertising
AdMob and UMP are integrated. Production ad unit IDs come from git-ignored `local.properties`; debug builds use Google test ad units. Ads appear only in approved non-critical spaces: Home and Documents banners, occasional interstitials after successful save actions, and a voluntary rewarded supporter badge. Ads never appear on onboarding, locked screens, capture forms, legal/security screens or during document review. Unavailable ad slots collapse rather than reserve blank space. The app declares `com.google.android.gms.permission.AD_ID` for Android 13+ advertising ID use.

Maintained by [LX Obsidian Labs](https://www.lxobsidianportal.co.za/).

# 18 — Welcome, onboarding and Firebase handover
2026-11-18 | Owner requirement U05 | Design only, not implemented

## Owner direction
After the animated splash, first-time users see a Welcome screen followed by onboarding. The base app must not require login, registration, Firebase credentials or a remote service. The owner will download the repository, customize the application and integrate their own Firebase project after handover. This is a deliberately extensible, functional foundation, not a mandate to create or configure Firebase resources on the owner's behalf.

## Proposed user flow
Cold launch → splash (doc 17) → resolve local first-run state → Welcome (new user) → onboarding introduction → minimal business setup → Home. Returning user with completed setup → Home directly after splash (or optional local app lock). Restore is available as an alternative to new setup. An interrupted onboarding resumes at the last saved step; completed users can revisit guidance in Help without resetting data. If a profile is restored, reconcile onboarding state from restored business identity. No network or ads during first-run onboarding.

## Screen and interaction proposal (copy/layout provisional)
| Step | Layout and behavior |
|---|---|
| Welcome S01 | Brand logo, concise value proposition, Get started primary CTA, Restore backup secondary action, terms/privacy access. No login button or sign-up wall. |
| Intro A | Explain daily money capture (Money In / Money Out), illustrative visual and Next. |
| Intro B | Explain customer debt and reminders, illustrative visual and Next. |
| Intro C | Explain monthly business pack and offline ownership, Finish/Continue. |
| Setup S02 | Business display name required, sector optional; formal billing fields later. Show progress; save locally. |
| Home S03 | Empty-state guidance for first transaction; no invented balances; optional demo kept separate. |

Owner has not approved onboarding copy, illustration style, number of slides, exact layout or animation. These are discussion defaults only. Provide Back and Skip introduction actions; Skip does not bypass minimal setup or necessary terms acceptance. Privacy and advertising consent are separate from terms; no ad consent required for core use.

## State and integration boundary
Proposed OnboardingRepository interface exposes observeState, saveProgress, complete and resetGuidance; local implementation backed by AndroidX DataStore for small preferences (version, current step, completion flag, accepted terms version). Room remains business-record source of truth. Reconcile completion with persisted business profile after process death or restore; never allow duplicate workspace creation. ViewModel and Compose routes depend on the interface, not a Firebase class. No Firebase Gradle plugin applied, SDK initialized, google-services.json required or cloud data transmitted in the base. No fictitious authentication UI or placeholder secrets. Build and startup must succeed without any Firebase account/config.

## Owner integration handover
Document permanent application ID once approved, project/module structure, dependency version catalog, onboarding state contract and extension points in README. Owner may register the Android application under their own Firebase project, install configuration file and Google services plugin, choose Firebase BoM and individual products only when required. Keep default build local and working; consider a separate opt-in flavor or adapter if adding remote functionality. Do not automatically enable Analytics, Auth, Firestore, Crashlytics or Messaging. Any later integration changes network behavior, data map, privacy policy and Play Data safety disclosure; access rules and consent must be evaluated before production. Firebase config identifiers are not secret credentials, but service account keys and signing keys must never enter the repo. No Firebase work performed now.

## Acceptance and handover
- Fresh install in airplane mode: splash → welcome → onboarding → local setup → Home.
- Relaunch after completion: Home, no repeated onboarding; resume interrupted progress; restore path works.
- Back/skip, font scaling, rotation, TalkBack, process death and reduced motion verified.
- Debug and release build do not require Firebase config; no network requests from onboarding; no account screen or ads.
- Source and documentation explain how to customize onboarding screens and integrate Firebase without replacing local core.
- Confirm package ID, exact user-facing copy, artwork and visual layout with owner before implementation.

## Official resources checked 2026-11-18
- https://firebase.google.com/docs/android/setup — registration, app configuration, Gradle plugin, BoM and product selection.
- https://firebase.google.com/docs/android/google-services-plugin-and-file — configuration and plugin details.
- https://developer.android.com/topic/libraries/architecture/datastore/ — persistent local preferences with coroutines/Flow, use Room for relational business data.


## Owner refinement U07 — plain-English welcome and graphical content
Welcome must communicate practical benefits without accounting, developer or financial jargon. Proposed copy for review (not yet approved):
- Heading: "Know where your money goes."
- Supporting line: "Keep track of money you receive, money you spend, and customers who still owe you."
- Benefit 1: "See what came in and what went out."
- Benefit 2: "Remember who owes you and when to follow up."
- Benefit 3: "Create quotes, invoices and monthly reports."
- Primary action: "Get started"; secondary action: "Restore a backup."
- Trust line: "Free to use. Works offline." (core features only; ads and external sharing may require connectivity.)

Graphic direction: original, friendly illustrations demonstrating real small-business situations, with visually consistent colours and simple shapes. Proposed Welcome hero: owner using a phone with three recognizable visual cues for money received, money spent and money owed. Subsequent graphics may depict a customer reminder and a monthly summary; all labels rendered as accessible UI text, not embedded in artwork. Avoid misleading money/profit claims or fabricated testimonials. Prepare separate illustration assets after owner confirms visual direction; no artwork generated yet. Use plain-language review with representative first-time users; ask them to explain each benefit in their own words. Preserve no-login, offline first-run and owner-managed Firebase boundaries.

## Owner refinement U08 — welcome artwork and theme
Follow [doc 19](19-brand-colour-and-welcome-visuals.md): dark earthy olive gradient, lime accents, beautiful original hero picture and plain-English benefits. Artwork and precise layout remain to be approved.

## Owner refinement U09 — required terms acknowledgement
The welcome/onboarding sequence includes the dedicated scrollable terms screen S27 before Get started and business setup. Explicit agreement is required; scrolling alone does not count. Follow [doc 20](20-terms-acknowledgement-and-first-run-flow.md) for the local acceptance record, accessibility, privacy separation and reacceptance behavior. Final terms wording remains pending legal review.

## Owner refinement U10 — business setup and clean live dashboard
See [doc 21](21-business-setup-home-and-navigation.md) for the expanded S02 and S03 contracts. Business name alone is sufficient; logo, sector and formal details are optional at setup. Customer-facing PDFs use the actual business logo if supplied, otherwise a name-only header. No sample financial data or seeded customers/documents in the live workspace. Bottom navigation proposed as Home, Customers, Money, Documents, More. This refinement supersedes conflicting earlier defaults; sample demo deferred.

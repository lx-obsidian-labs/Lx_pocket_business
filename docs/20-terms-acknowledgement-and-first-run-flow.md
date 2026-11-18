# 20 — Terms acknowledgement and first-run flow
2026-11-18 | Owner requirement U09 | Design specification only; legal text and implementation pending.

## Owner direction
The first-run Welcome experience explains the app's benefits in plain English, includes original artwork, presents the terms and conditions in a scrollable reading experience, and requires explicit acknowledgement before the user starts using the app. No mandatory login or Firebase configuration. Lime accents and dark olive gradient follow doc 19.

## Proposed sequence
Splash (S00) → Welcome (S01, benefits and artwork) → onboarding benefit panels → Terms and conditions (S27, part of the welcome/onboarding flow) → Get started → minimal business setup (S02) or restore → Home (S03). The exact number and order of benefit panels can be refined by the owner. Existing users with accepted current terms and valid workspace bypass first-run flow; a material terms update may require renewed acknowledgement without deleting their records. No ad on these screens.

## S27 — Terms and conditions
- Heading: "Before you get started".
- Plain-English introduction: "Please read the terms for using LX Pocket Business. They explain what the app does, your responsibilities and how to protect your records."
- Scrollable, versioned full terms (bundled locally); visible date/version; headings and accessible text. Provide link to the full public website version and separate privacy policy. A summary is not a substitute for the complete terms.
- Show an explicit, initially unchecked checkbox: "I have read and agree to the Terms and Conditions."
- Primary lime action "Get started" remains disabled until acknowledgement is selected. Scroll progress can guide reading and reveal the acknowledgement at the end, but scrolling alone is not agreement. Make the full text and checkbox accessible to TalkBack, keyboard and large-text users; don't require a particular touch gesture or imply that reaching the bottom proves comprehension.
- Back returns to previous onboarding step without losing position. If declining, allow exit without acceptance; provide privacy/help access. Do not silently create a workspace. Returning owners who decline updated terms must have a safe route to export existing records and seek support, subject to legal review.
- Privacy policy is a notice, not blanket consent to all processing. Advertising/privacy choices are a separate flow; declining personalized ads must not block core use.

## Local state and acceptance record
Store terms version, content digest, UTC acceptance time (device clock, not independently verified), and explicit acceptance flag locally. No account ID or personal information needed. Save transactionally before continuing; handle process death, rotation, interrupted onboarding and failed storage. Never pre-check or infer agreement. Reconcile restore without treating a restored record as acceptance by a different person; if needed request current acknowledgement. In Settings → Legal, display accepted version, bundled current terms and privacy links. Reaccept material changes only after reviewed version policy; no forced agreement on ordinary launches. Owner's future Firebase integration must not be required for this flow.

## Test cases
T-L01 fresh install offline: read terms, unchecked button disabled, explicit checkbox enables; complete setup. T-L02 scroll to end without checkbox: cannot continue. T-L03 back/rotate/process death: progress retained, no false acceptance. T-L04 screen reader and 200% font size: full terms and acknowledgement accessible. T-L05 decline: no workspace created; user can return or exit. T-L06 existing accepted version: no repeat onboarding. T-L07 changed material terms: prompt once, preserve records, export/support route. T-L08 local save failure: stay on page with retry. T-L09 ad consent rejected: core works. T-L10 website unavailable: local terms still readable. T-L11 restore: validate acceptance independently.

## Release dependencies
Final terms, privacy policy, consumer-law/POPIA review, verified developer identity and contact, versioning and public website routes remain outstanding. This document is the interaction specification, not final legal wording or proof of compliance. No legal page deployed or app code written.

# 30 — Authentication and Access Control Guidelines (AUTH)
2026-11-18 | U05/U12, M09/M10 | Proposed; base account-free, owner-managed future Firebase. No authentication implementation or Firebase resources created.

## Scope and distinction
Base release: no sign-in, sign-up, email verification, cloud identity or mandatory account. First run splash → welcome → onboarding → terms → local business setup → Home. Optional local app lock protects access to records on a shared device; it is not server authentication, multi-user authorization or standalone database encryption. No sign-in screen placeholder. Local access and future cloud identity are separate interfaces.

## Local app lock
Use AndroidX BiometricPrompt and device credential where supported; check BiometricManager capabilities and Android version (API 26 minimum proposed; authenticator combinations differ on API 29 and below). Offer opt-in under Settings > Security, show unavailable/enrollment state and supported system credential fallback; never create a weak custom PIN storage mechanism. Prompt after cold launch and configurable inactivity/background threshold, default proposed immediate lock after background or screen lock; no sensitive content before unlock. No ads on locked screen. Protect deep links, notifications and recent-app previews; generic notifications. Cancellation stays locked; user can retry, use supported device credential or access non-sensitive help. If recovery impossible, explain safe restore/reset choices with explicit data-loss confirmation. Android Keystore for device-bound key material when appropriate; portable encrypted backup has independent recovery password. Never store biometric templates or raw credentials. Lock cannot prevent all attacks on rooted or compromised devices.

## Future Firebase integration (owner only, optional)
1. Register final Android application ID in owner-controlled Firebase project; separate dev/prod projects, install current Firebase BoM, google-services plugin/config in opt-in build flavor or adapter. Do not commit service account keys or admin credentials. Android configuration identifiers are not themselves secrets; apply API restrictions where appropriate.
2. Choose providers deliberately: Credential Manager for Google sign-in, or Firebase email/password with policy, email enumeration protection and verification; support password reset and generic errors. No provider assumed approved. Evaluate passkeys/MFA if needed and supported by chosen plan; review charges and quotas.
3. Expose AuthRepository observing session, sign-in/out, reauthentication, recovery and deletion state. Default LocalOnly implementation remains usable offline. Never write passwords or refresh tokens into Room/logs; let official SDK handle sessions. Do not silently convert guest/local data to cloud records.
4. On explicit opt-in, show which records will sync, conflict/duplicate handling and account ownership before migration. Require user confirmation and verified backup. Preserve local workspace if sign-in fails or is cancelled. Do not equate a Firebase UID with permission to read every business. If adding Firestore/Storage, deny by default, enforce per-workspace membership and least privilege in security rules; test negative cases with Emulator Suite. Backend verifies ID tokens and authorization; no admin key in APK. App Check is complementary, not identity authorization.
5. On sign-out, stop sync/listeners, revoke local cloud session and protect cached records; do not erase local business records without explicit confirmation. Provide reauth for sensitive account changes, account deletion and export. If accounts are introduced, reassess Play in-app and web account-deletion requirements, actual server data deletion, retention exceptions, POPIA notices and Data safety. No promise of cloud deletion in current local-only base.

## Threats and test gates
AUTH-01 fresh offline install works without Firebase configuration or network. AUTH-02 optional lock supports enrolled biometrics/device credential and unavailable cases. AUTH-03 cancelled prompt/deep link/notification never exposes protected records. AUTH-04 lifecycle/background/process death re-locks as configured; test API26–36. AUTH-05 credential/enrollment change, failed authentication and device reset do not silently delete records. AUTH-06 backup password independent of device lock. Future only: AUTH-07 expired/revoked token and offline sessions; AUTH-08 sign-out/cancel keeps local data; AUTH-09 cross-workspace denial and security-rule tests; AUTH-10 account deletion, export and documented retention; AUTH-11 no secrets in source or diagnostics.

## Official sources
https://developer.android.com/identity/sign-in/biometric-auth
https://developer.android.com/identity/sign-in/credential-manager
https://developer.android.com/privacy-and-security/keystore
https://firebase.google.com/docs/android/setup
https://firebase.google.com/docs/auth/android/password-auth
https://firebase.google.com/docs/auth/android/google-signin
https://firebase.google.com/docs/rules
https://firebase.google.com/docs/emulator-suite
https://support.google.com/googleplay/android-developer/answer/13327111
Provider choices, session policy, backend rules, pricing and legal review are deferred to owner integration. Local lock policy is proposed, not tested.

# 05 — Security, privacy and backup
Status: design proposal | 2026-11-18
## Data boundaries
Business profiles, invoices, payment records, contacts, receipts and reports remain in private app storage by default. No business content in ads, telemetry, crash reports or URLs. User-initiated sharing/export transfers a copy outside our control. AdMob/UMP separately process online SDK data; never claim zero collection. Disable cloud autobackup/device-transfer for sensitive files unless deliberately reviewed and disclosed. Verify manifest and Android backup rules on actual devices.
## Threat model
| Risk | Control | Evidence |
|---|---|---|
| Lost/shared phone | Android sandbox, device encryption, optional biometric/PIN gate and auto-lock; no claim of independent DB encryption | screen-lock and restart tests |
| Malicious backup | Versioned format, authenticated encryption, bounded file sizes, strict schema/paths, reject unknown versions, staged restore | tamper, wrong password, zip-slip and oversized tests |
| Leaked PDF | FileProvider content URI, least privilege temporary grant, private temporary directory, no public auto-export | permission and chooser test |
| Ads exfiltration | Separate adapter, no business fields or custom targeting, inspect traffic and dependencies | network capture with synthetic records |
| Corrupt migration or power loss | transactions, migration tests, staged backup restore, rollback | migration and interrupted-write tests |
| Build supply chain | pinned deps, SBOM, licence and vulnerability review, least-privilege CI, protected signing credentials | CI artifacts |
| CSV injection | escape formula-leading text for spreadsheet export, RFC-compatible quoting | hostile-input fixtures |
## Backup specification
User chooses destination via SAF. Versioned manifest, schema version, checksum of each payload and attachment; use a reviewed authenticated encryption implementation (AES-GCM with unique nonce, password-derived key with salt and documented KDF parameters). Do not invent own crypto. Password independent of Android Keystore so portable; user informed lost password cannot be recovered. Export from consistent transaction snapshot. Restore validates version, integrity, bounds and relationships in temporary storage; display business/date/count summary and require destructive replace confirmation; take local rollback copy, swap only after success. No merge V1; warn about duplicates and sequence reuse after restoring old backup. Keep existing data on any failure. Never upload backup automatically.
## Privacy workflow
Local deletion of all data requires explicit confirmation and retention warning; uninstall can destroy local data. Issued documents retained unless owner explicitly clears workspace; offer export before deletion. Explain third-party ad records are separate; privacy options available in settings. No support logs containing customer records unless user independently chooses to send redacted information. No contact/location permissions for MVP; photo picker or SAF for images. Audit Android backup behavior, SDK data disclosure and network traffic before legal text freeze.
## References
https://developer.android.com/privacy-and-security/security-best-practices
https://developer.android.com/identity/data/autobackup
https://developer.android.com/privacy-and-security/keystore
https://mas.owasp.org/MASVS/
https://www.gov.za/documents/protection-personal-information-act
https://developers.google.com/admob/android/privacy/play-data-disclosure
First four implementation reading candidates; last two reviewed. No security certification implied.

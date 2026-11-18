package com.obsidianlabs.lxpocketbusiness.data.model

import java.security.MessageDigest

object LegalLinks {
    const val PRIVACY_URL = "https://www.lxobsidianportal.co.za/pocket-business-privacy"
    const val TERMS_URL = "https://www.lxobsidianportal.co.za/pocket-business-terms"
    const val SUPPORT_EMAIL = "support@lxobsidianportal.co.za"
    const val GENERAL_EMAIL = "info@lxobsidianportal.co.za"
}

object LocalTerms {
    const val VERSION = "1.0"
    const val TEXT = """LX Pocket Business — Terms and Conditions
Version 1.0 · 18 November 2026 · LX Pocket Business for Android

1. Agreement and provider
These terms govern use of LX Pocket Business, provided by LX Obsidian Labs, South Africa. By accepting the terms and using the app you agree to them. If you do not agree, do not use the app. Mandatory consumer rights remain unaffected.

2. Service and eligibility
The app is intended for adult business users to record financial activity, manage customers and prepare business documents. You must have authority to act for the business whose records you enter. The app is free and supported by advertising; internet or carrier charges may apply.

3. Licence and ownership
We grant you a non-exclusive licence to use the app for lawful personal or business purposes, subject to applicable distribution terms. LX Obsidian Labs retains rights in its software and branding; third-party components remain subject to their own licences. You retain rights in your business records and documents.

4. Responsible use
Do not use the app for fraud, unlawful processing, harassment, malicious activity or infringement of others' rights. You are responsible for having a lawful basis to record customer information and for maintaining accurate records and appropriate access controls.

5. Financial and tax limitations
The app is a recordkeeping tool, not a bank, payment processor, accountant or tax adviser. Cash movement is not necessarily profit. Verify all amounts, tax settings, document numbering and legal requirements before issuing documents or filing returns. The app does not certify SARS compliance or replace professional advice.

6. Records and backups
You are responsible for maintaining suitable backups, checking exported records and preserving legally required records. Uninstalling, clearing storage, losing a device or restoring a backup can cause data loss. Review documents before sending them and protect exported customer information.

7. Advertising and third parties
Advertisements may be supplied by Google AdMob and advertising partners. Third-party offers and websites are governed by their own terms. Do not manipulate ad impressions or clicks. Our Privacy Policy explains relevant data handling and choices.

8. Updates and availability
Features may change to improve security, usability or legal compliance. We do not guarantee uninterrupted availability or compatibility with every device. Obtain updates from authorized distribution channels.

9. Liability and consumer rights
To the extent permitted by law, the app is supplied without guarantees beyond those required by law. We are not responsible for losses caused by inaccurate user entries, unauthorized sharing or failure to maintain backups, except where liability cannot lawfully be excluded. Nothing in these terms excludes liability for fraud, gross negligence or any non-excludable statutory obligation, or limits rights under applicable consumer protection legislation.

10. Ending use and disputes
You may stop using the app at any time. Export records you need before deleting it. South African law applies subject to mandatory protections in your jurisdiction. Contact us first to seek a resolution; this does not restrict your right to approach a competent court or regulator.

11. Changes and contact
Material changes will be communicated as required by law. Contact support@lxobsidianportal.co.za for support or questions about these terms.

Online terms: https://www.lxobsidianportal.co.za/pocket-business-terms
Privacy policy: https://www.lxobsidianportal.co.za/pocket-business-privacy"""

    val digest: String = MessageDigest.getInstance("SHA-256")
        .digest(TEXT.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }
}

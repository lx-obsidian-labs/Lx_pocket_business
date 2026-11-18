# Android release pipeline
LX Obsidian Labs — LX Pocket Business

The manually triggered Android release workflow builds the signed AAB and APK, runs release unit tests and lint, checks signatures and publishes checksums and build artifacts. It does not publish to Google Play or create a public GitHub release.

## Owner setup
In repository Settings > Secrets and variables > Actions, add repository secrets:
- ANDROID_KEYSTORE_BASE64: base64 of the existing owner-controlled upload keystore (never commit it).
- ANDROID_STORE_PASSWORD
- ANDROID_KEY_ALIAS
- ANDROID_KEY_PASSWORD

Production AdMob identifiers are configured in the release workflow. They are public application identifiers, not signing secrets. Configuring an ad unit does not itself enable its display.

On Windows PowerShell, encode the existing keystore using [Convert]::ToBase64String([IO.File]::ReadAllBytes("C:\\path\\upload.jks")). Keep the output private. If no upload key exists, generate one in Android Studio via Build > Generate Signed Bundle / APK, then securely back it up. Do not replace an existing registered upload key.

## Build
Open Actions > Android release > Run workflow on main. The workflow installs Android SDK platform 37.0 as required by the project dependencies.

Download the lx-pocket-business-release artifact from the successful run. Extract it and upload app-release.aab to the intended Play testing track. The APK is signed with the upload key, not necessarily the Google Play app signing key; installations from different signing keys cannot update one another.

## Release gate
Verify the actual release on a device, including AdMob consent, app lock, backups, legal links, document exports, and data safety declarations. Confirm versionCode increases for each subsequent Play upload. Never click live ads during testing. Keep signing secrets private. The pipeline is not a substitute for device testing or Play review.

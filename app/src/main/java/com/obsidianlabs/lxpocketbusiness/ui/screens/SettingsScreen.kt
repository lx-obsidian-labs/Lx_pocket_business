package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.data.model.LegalLinks
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxBusinessAvatar
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSettingRow
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxError
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType

private enum class SettingsInfo {
    APPEARANCE,
    REMINDERS,
    BACKUP,
    HELP,
    PRIVACY,
    TERMS,
    ADS,
    LICENSES,
    ABOUT
}

@Composable
fun SettingsScreen(
    business: Business?,
    appLockEnabled: Boolean,
    deviceLockAvailable: Boolean,
    remindersEnabled: Boolean,
    adsEnabled: Boolean,
    isSupporter: Boolean,
    onBackClick: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenCatalogue: () -> Unit,
    onOpenReports: () -> Unit,
    onOpenReminders: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenLegal: () -> Unit,
    onToggleAppLock: (Boolean) -> Unit,
    onToggleReminders: (Boolean) -> Unit,
    onToggleAds: (Boolean) -> Unit,
    onResetAdConsent: () -> Unit,
    onOpenSupport: () -> Unit,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    var info by remember { mutableStateOf<SettingsInfo?>(null) }

    if (info != null) {
        SettingsInfoScreen(
            info = info!!,
            adsEnabled = adsEnabled,
            isSupporter = isSupporter,
            onBackClick = { info = null }
        )
        return
    }

    LxAppBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(LxSpacing.lg)) {
            LxScreenHeader(
                title = "Settings",
                subtitle = "Business tools, preferences and security",
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LxTextPrimary)
                    }
                }
            )
            Spacer(Modifier.height(LxSpacing.lg))

            LxCard(Modifier.fillMaxWidth(), onClick = onOpenProfile) {
                Row(Modifier.padding(LxSpacing.lg), verticalAlignment = Alignment.CenterVertically) {
                    LxBusinessAvatar(business, 48.dp)
                    Spacer(Modifier.padding(horizontal = LxSpacing.md))
                    Column(Modifier.weight(1f)) {
                        Text(business?.name ?: "Business profile", style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1)
                        Text(business?.sector?.ifBlank { "Tap to complete your profile" } ?: "Tap to complete your profile", style = LxType.caption, color = LxTextSecondary)
                    }
                    Text("Edit", style = LxType.button, color = LxLime)
                }
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxGroupTitle("Business tools")
            LxCard(Modifier.fillMaxWidth()) {
                LxSettingRow(Icons.Default.Category, "Catalogue & services", "Items, prices and VAT categories", onClick = onOpenCatalogue)
                LxSettingRow(Icons.Default.Description, "Monthly reports", "Review actual records for a period", onClick = onOpenReports)
                LxSettingRow(Icons.Default.Schedule, "Payment reminders", "Due dates and follow-up actions", onClick = onOpenReminders)
                LxSettingRow(Icons.Default.Business, "Business profile", "Contact, tax, branding and numbering", onClick = onOpenProfile)
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxGroupTitle("Preferences")
            LxCard(Modifier.fillMaxWidth()) {
                LxSettingRow(Icons.Default.Palette, "Appearance", "Dark olive theme", onClick = { info = SettingsInfo.APPEARANCE })
                LxToggleRow(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "In-app and system reminder prompts",
                    checked = remindersEnabled,
                    onCheckedChange = onToggleReminders
                )
                LxToggleRow(
                    icon = Icons.Default.Payment,
                    title = "Show ads",
                    subtitle = "Consent-based ads keep this app free",
                    checked = adsEnabled,
                    onCheckedChange = onToggleAds
                )
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxGroupTitle("Privacy and security")
            LxCard(Modifier.fillMaxWidth()) {
                LxToggleRow(
                    icon = Icons.Default.Fingerprint,
                    title = "App lock",
                    subtitle = if (deviceLockAvailable) "Require your device screen lock on launch" else "Set a device screen lock first",
                    checked = appLockEnabled,
                    enabled = deviceLockAvailable,
                    onCheckedChange = onToggleAppLock
                )
                LxSettingRow(Icons.Default.Save, "Backup and restore", "Local encrypted backup", onClick = { info = SettingsInfo.BACKUP; onOpenBackup() })
                LxSettingRow(Icons.Default.Shield, "Data export", "Reports and structured exports", onClick = { info = SettingsInfo.BACKUP })
                LxSettingRow(Icons.AutoMirrored.Filled.Logout, "Sign out", "Lock this device; local records stay here", onClick = onSignOut, tint = LxError)
            }

            Spacer(Modifier.height(LxSpacing.lg))
            LxGroupTitle("Support and legal")
            LxCard(Modifier.fillMaxWidth()) {
                LxSettingRow(Icons.AutoMirrored.Filled.HelpOutline, "Help center", "Offline guidance and diagnostics", onClick = { info = SettingsInfo.HELP })
                LxSettingRow(Icons.Default.Shield, "Privacy policy", "How local records are handled", onClick = { info = SettingsInfo.PRIVACY })
                LxSettingRow(Icons.AutoMirrored.Filled.HelpOutline, "Online privacy policy", "Open the published policy", onClick = { openUrl(context, LegalLinks.PRIVACY_URL) })
                LxSettingRow(Icons.Default.Gavel, "Terms and conditions", "Read the current bundled terms", onClick = onOpenLegal)
                LxSettingRow(Icons.Default.Gavel, "Online terms", "Open the published terms", onClick = { openUrl(context, LegalLinks.TERMS_URL) })
                LxSettingRow(Icons.Default.Favorite, "Support LX Obsidian Labs", if (isSupporter) "Supporter badge is active" else "Watch a voluntary ad for a badge", onClick = onOpenSupport, tint = LxLime)
                LxSettingRow(Icons.Default.Payment, "Advertising privacy choices", "Current advertising status", onClick = { info = SettingsInfo.ADS })
                LxSettingRow(Icons.Default.Settings, "Reset ad choices", "Review consent again", onClick = onResetAdConsent)
                LxSettingRow(Icons.Default.Info, "Open-source licenses", "Dependency notices", onClick = { info = SettingsInfo.LICENSES })
                LxSettingRow(Icons.Default.Settings, "About", "Version and developer information", onClick = { info = SettingsInfo.ABOUT })
            }

            Spacer(Modifier.height(LxSpacing.xl))
            Text("Version ${versionName(context)}", style = LxType.caption, color = LxTextSecondary)
            Text(
                "Developed by LX Obsidian Labs",
                style = LxType.caption,
                color = LxLime,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { openWebsite(context) }
            )
            Spacer(Modifier.height(LxSpacing.xl))
        }
    }
}

@Composable
private fun LxGroupTitle(title: String) {
    Text(title, style = LxType.sectionTitle, color = LxTextPrimary)
    Spacer(Modifier.height(LxSpacing.sm))
}

@Composable
private fun LxToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = LxSpacing.lg, vertical = LxSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = LxLime)
        Spacer(Modifier.padding(horizontal = LxSpacing.md))
        Column(Modifier.weight(1f)) {
            Text(title, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1)
            Text(subtitle, style = LxType.caption, color = LxTextSecondary, maxLines = 2)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedThumbColor = LxLime, checkedTrackColor = LxLime.copy(alpha = 0.35f))
        )
    }
}

@Composable
private fun SettingsInfoScreen(
    info: SettingsInfo,
    adsEnabled: Boolean,
    isSupporter: Boolean,
    onBackClick: () -> Unit
) {
    val content = when (info) {
        SettingsInfo.APPEARANCE -> "Appearance" to listOf(
            "LX Pocket Business uses a fixed dark olive theme with electric lime accents.",
            "Financial values use tabular numerals and remain readable at increased system font sizes."
        )
        SettingsInfo.REMINDERS -> "Reminders" to listOf(
            "Reminders appear in Needs attention on Home from real tasks, due debts and open quotes.",
            "Notification permission is requested only after reminders are enabled."
        )
        SettingsInfo.BACKUP -> "Backup and data export" to listOf(
            "Encrypted local backup and structured export are not implemented in this build.",
            "Records are stored locally in this app's private workspace. Uninstalling the app may remove them."
        )
        SettingsInfo.HELP -> "Offline help" to listOf(
            "Record money received or spent from Home or Money.",
            "Use Record debt for customer balances. A debt becomes cash only after a settlement is recorded.",
            "Create quotes and invoices from Documents. Templates are not SARS-certified."
        )
        SettingsInfo.PRIVACY -> "Privacy" to listOf(
            "Core use requires no account, internet connection or Firebase sign-in.",
            "Business records stay in local app storage. Notifications contain no customer names or amounts by default.",
            "The app lock uses your device screen lock through Android BiometricPrompt; no biometric template or raw credential is stored by this app.",
            "Ads use Google AdMob and UMP. No customer names, document values or financial records are sent for advertising.",
            "Published policy: ${LegalLinks.PRIVACY_URL}"
        )
        SettingsInfo.TERMS -> "Terms and conditions" to listOf(
            "Open the bundled Terms screen from the main Settings screen to read and acknowledge the current version."
        )
        SettingsInfo.ADS -> "Advertising privacy choices" to listOf(
            "Ads are ${if (adsEnabled) "enabled" else "disabled"} on this device.",
            "Google AdMob and UMP manage consent. No customer names, document values or financial records are sent for advertising.",
            if (isSupporter) "Your supporter badge was unlocked by a rewarded ad." else "A voluntary rewarded ad can unlock a supporter badge."
        )
        SettingsInfo.LICENSES -> "Open-source licenses" to listOf(
            "This build uses AndroidX, Jetpack Compose, Room, DataStore, Gson and related Kotlin dependencies.",
            "The repository dependency lock is the authoritative list for exact versions and licence review."
        )
        SettingsInfo.ABOUT -> "About" to listOf(
            "LX Pocket Business is an offline-first recordkeeping tool for South African freelancers and small businesses.",
            "Net cash movement is not profit. Templates have not been certified by SARS."
        )
    }

    InfoScreen(content.first, content.second, onBackClick)
}

@Composable
fun InfoScreen(
    title: String,
    paragraphs: List<String>,
    onBackClick: () -> Unit,
    subtitle: String = "LX Pocket Business"
) {
    LxAppBackground {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(LxSpacing.lg)) {
            LxScreenHeader(
                title = title,
                subtitle = subtitle,
                actions = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = LxTextPrimary)
                    }
                }
            )
            Spacer(Modifier.height(LxSpacing.lg))
            LxCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(LxSpacing.lg)) {
                    paragraphs.forEachIndexed { index, paragraph ->
                        if (index > 0) Spacer(Modifier.height(LxSpacing.md))
                        Text(paragraph, style = LxType.body, color = LxTextSecondary)
                    }
                }
            }
        }
    }
}

private fun versionName(context: Context): String = runCatching {
    context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "unknown"
}.getOrDefault("unknown")

private fun openWebsite(context: Context) {
    openUrl(context, "https://www.lxobsidianportal.co.za/")
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

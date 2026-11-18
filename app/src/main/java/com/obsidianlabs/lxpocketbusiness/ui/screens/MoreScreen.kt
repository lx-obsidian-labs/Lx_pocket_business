package com.obsidianlabs.lxpocketbusiness.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxBusinessAvatar
import com.obsidianlabs.lxpocketbusiness.ui.components.LxCard
import com.obsidianlabs.lxpocketbusiness.ui.components.LxScreenHeader
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSettingRow
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.components.LxStatusPill
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxError
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType

@Composable
fun MoreScreen(
    business: Business?,
    appLockEnabled: Boolean,
    remindersEnabled: Boolean,
    isSupporter: Boolean,
    onOpenProfileClick: () -> Unit,
    onOpenSettingsClick: () -> Unit,
    onOpenCatalogueClick: () -> Unit,
    onOpenReportsClick: () -> Unit,
    onOpenRemindersClick: () -> Unit,
    onOpenBackupClick: () -> Unit,
    onOpenLegalClick: () -> Unit,
    onOpenHelpClick: () -> Unit,
    onOpenSupportClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    LxAppBackground {
        Column(Modifier.fillMaxSize().padding(horizontal = LxSpacing.lg, vertical = LxSpacing.lg)) {
            LxScreenHeader("More", "Your business, preferences and tools")

            Spacer(Modifier.height(LxSpacing.lg))
            LxCard(Modifier.fillMaxWidth(), onClick = onOpenProfileClick) {
                Row(Modifier.padding(LxSpacing.lg)) {
                    LxBusinessAvatar(business, 52.dp)
                    Spacer(Modifier.padding(horizontal = LxSpacing.md))
                    Column(Modifier.weight(1f)) {
                        business?.name?.let {
                            androidx.compose.material3.Text(it, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1)
                        }
                        androidx.compose.material3.Text(
                            business?.sector?.ifBlank { "Tap to complete your business profile" }
                                ?: "Tap to complete your business profile",
                            style = LxType.caption,
                            color = LxTextSecondary,
                            maxLines = 2
                        )
                        Spacer(Modifier.height(LxSpacing.sm))
                        LxStatusPill("Profile ${profileCompletion(business)}% complete", LxLime)
                    }
                }
            }

            Spacer(Modifier.height(LxSpacing.lg))
            androidx.compose.material3.Text("Quick access", style = LxType.sectionTitle, color = LxTextPrimary)
            Spacer(Modifier.height(LxSpacing.sm))
            LxCard(Modifier.fillMaxWidth()) {
                LxSettingRow(Icons.Default.Settings, "Settings", "Preferences, security, legal and support", trailing = "Open", onClick = onOpenSettingsClick)
                LxSettingRow(Icons.Default.Business, "Business profile", "Branding, tax and document numbering", onClick = onOpenProfileClick)
                LxSettingRow(Icons.Default.Category, "Catalogue & services", "Items, prices and VAT categories", onClick = onOpenCatalogueClick)
                LxSettingRow(Icons.Default.Description, "Monthly reports", "Actual records for a chosen period", onClick = onOpenReportsClick)
                LxSettingRow(Icons.Default.Schedule, "Reminders", if (remindersEnabled) "On" else "Off", onClick = onOpenRemindersClick)
                LxSettingRow(
                    icon = Icons.Default.Favorite,
                    title = if (isSupporter) "LX Obsidian Labs supporter" else "Support LX Obsidian Labs",
                    subtitle = if (isSupporter) "Thank you for supporting the project" else "Voluntary rewarded ad for a badge",
                    trailing = if (isSupporter) "Active" else "Watch",
                    tint = LxLime,
                    onClick = onOpenSupportClick
                )
            }

            Spacer(Modifier.height(LxSpacing.lg))
            androidx.compose.material3.Text("Privacy and data", style = LxType.sectionTitle, color = LxTextPrimary)
            Spacer(Modifier.height(LxSpacing.sm))
            LxCard(Modifier.fillMaxWidth()) {
                LxSettingRow(
                    icon = Icons.Default.Shield,
                    title = "App lock",
                    subtitle = if (appLockEnabled) "Device screen lock required on launch" else "Off",
                    trailing = if (appLockEnabled) "On" else "Off",
                    tint = if (appLockEnabled) LxMoneyInGreen else LxWarning,
                    onClick = onOpenSettingsClick
                )
                LxSettingRow(Icons.Default.Save, "Backup & data export", "Encrypted local backup", onClick = onOpenBackupClick)
                LxSettingRow(Icons.AutoMirrored.Filled.HelpOutline, "Offline help", "Workflows and app information", onClick = onOpenHelpClick)
                LxSettingRow(Icons.AutoMirrored.Filled.Logout, "Sign out", "Lock this device; local records stay here", tint = LxError, onClick = onSignOutClick)
            }
            Spacer(Modifier.height(LxSpacing.xl))
        }
    }
}

private fun profileCompletion(business: Business?): Int {
    if (business == null) return 0
    val values = listOf(
        business.name, business.legalName, business.sector, business.registrationNumber,
        business.billingAddress, business.phone, business.email, business.defaultTerms,
        business.paymentInstructions, business.invoicePrefix, business.quotePrefix, business.creditNotePrefix
    )
    return values.count { it.isNotBlank() } * 100 / values.size
}

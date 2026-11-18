package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.data.model.LegalLinks
import com.obsidianlabs.lxpocketbusiness.data.model.LocalTerms
import com.obsidianlabs.lxpocketbusiness.ui.theme.*

@Composable
fun TermsScreen(
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    readOnly: Boolean = false
) {
    var isAgreed by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        TextButton(onClick = onBackClick) { Text("Back") }
        Text(if (readOnly) "Terms & Privacy" else "Before you get started",
            fontSize = 24.sp, fontWeight = FontWeight.Bold, color = LxTextPrimary)
        Spacer(Modifier.height(12.dp))
        Text("Please read the terms for using LX Pocket Business. They explain what the app does, your responsibilities and how to protect your records.",
            color = LxTextSecondary)
        Spacer(Modifier.height(16.dp))
        Card(Modifier.weight(1f).fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = LxCardBg)) {
            Text(LocalTerms.TEXT, modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                color = LxTextPrimary, fontSize = 16.sp)
        }
        if (readOnly) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = { openUrl(context, LegalLinks.TERMS_URL) }) { Text("Online terms") }
                TextButton(onClick = { openUrl(context, LegalLinks.PRIVACY_URL) }) { Text("Privacy policy") }
            }
        }
        if (!readOnly) {
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isAgreed, onCheckedChange = { isAgreed = it })
                Text("I have read and agree to the Terms and Conditions.", color = LxTextPrimary,
                    modifier = Modifier.weight(1f))
            }
            Button(onClick = onContinueClick, enabled = isAgreed,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp)) {
                Text("Get started", fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    }
}

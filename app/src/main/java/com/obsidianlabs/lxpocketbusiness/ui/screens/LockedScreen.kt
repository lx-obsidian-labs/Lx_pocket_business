package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.obsidianlabs.lxpocketbusiness.security.LocalLockManager
import com.obsidianlabs.lxpocketbusiness.ui.components.LxAppBackground
import com.obsidianlabs.lxpocketbusiness.ui.components.LxLogoLarge
import com.obsidianlabs.lxpocketbusiness.ui.components.LxPrimaryButton
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSecondaryButton
import com.obsidianlabs.lxpocketbusiness.ui.components.LxSpacing
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxError
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType

@Composable
fun LockedScreen(onUnlocked: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val lockManager = remember { LocalLockManager(context) }
    var error by remember { mutableStateOf<String?>(null) }

    LxAppBackground {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = LxSpacing.xl, vertical = LxSpacing.xxl),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LxLogoLarge(size = 132.dp)
            Spacer(Modifier.height(LxSpacing.xl))
            Text("App locked", style = LxType.screenTitle, color = LxTextPrimary)
            Spacer(Modifier.height(LxSpacing.sm))
            Text(
                "Local records stay on this device. Confirm your device screen lock to continue.",
                style = LxType.body,
                color = LxTextSecondary
            )
            error?.let {
                Spacer(Modifier.height(LxSpacing.md))
                Text(it, style = LxType.caption, color = LxError)
            }
            Spacer(Modifier.height(LxSpacing.xl))
            LxPrimaryButton(
                text = "Unlock",
                onClick = {
                    if (activity == null) {
                        error = "Unlock is not available from this screen."
                    } else {
                        lockManager.requestUnlock(
                            activity = activity,
                            onSuccess = onUnlocked,
                            onError = { error = it }
                        )
                    }
                }
            )
            if (!lockManager.isDeviceCredentialAvailable()) {
                Spacer(Modifier.height(LxSpacing.md))
                LxSecondaryButton(
                    text = "Open Android security settings",
                    onClick = {
                        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
                    }
                )
            }
            Spacer(Modifier.height(LxSpacing.xxl))
            Text("powered by LX Obsidian Labs", style = LxType.caption, color = LxTextSecondary)
        }
    }
}

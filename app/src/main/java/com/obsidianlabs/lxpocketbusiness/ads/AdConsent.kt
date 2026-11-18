package com.obsidianlabs.lxpocketbusiness.ads

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberAdConsent(enabled: Boolean, refreshKey: Int = 0, resetKey: Int = 0): Boolean {
    var canRequestAds by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(enabled, refreshKey, resetKey) {
        if (!enabled) {
            canRequestAds = false
            return@LaunchedEffect
        }
        val activity = context as? Activity ?: return@LaunchedEffect
        AdMobManager.initialize(activity)
        val onConsentResult: (Boolean) -> Unit = { allowed ->
            canRequestAds = allowed
            if (allowed) {
                InterstitialAdManager.load(activity)
                RewardedAdManager.load(activity)
            }
        }
        if (resetKey > 0) {
            AdMobManager.resetConsent(activity, onConsentResult)
        } else {
            AdMobManager.requestConsent(activity, onConsentResult)
        }
    }

    return enabled && canRequestAds
}

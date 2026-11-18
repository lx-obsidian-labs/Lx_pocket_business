package com.obsidianlabs.lxpocketbusiness.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

@Composable
fun LxAdBanner(enabled: Boolean, unitId: String) {
    if (!enabled || unitId.isBlank()) {
        Spacer(Modifier.height(0.dp))
        return
    }

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var adViewRef = remember { mutableStateOf<AdView?>(null) }
    val loaded = remember { mutableStateOf(false) }

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                adViewRef.value?.resume()
            }

            override fun onPause(owner: LifecycleOwner) {
                adViewRef.value?.pause()
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            adViewRef.value?.destroy()
            adViewRef.value = null
        }
    }

    key(enabled, unitId) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (loaded.value) 50.dp else 0.dp),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    this.adUnitId = unitId
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            loaded.value = true
                        }

                        override fun onAdFailedToLoad(error: LoadAdError) {
                            loaded.value = false
                        }
                    }
                    adViewRef.value = this
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

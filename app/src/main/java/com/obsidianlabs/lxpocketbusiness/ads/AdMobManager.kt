package com.obsidianlabs.lxpocketbusiness.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.obsidianlabs.lxpocketbusiness.BuildConfig

private const val TEST_BANNER_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
private const val TEST_INTERSTITIAL_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
private const val TEST_REWARDED_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
private const val INTERSTITIAL_COOLDOWN_MS = 5 * 60 * 1000L

object AdIds {
    val banner: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_UNIT_ID else BuildConfig.ADMOB_BANNER_UNIT_ID

    val interstitial: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL_UNIT_ID else BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID

    val rewarded: String
        get() = if (BuildConfig.DEBUG) TEST_REWARDED_UNIT_ID else BuildConfig.ADMOB_REWARDED_UNIT_ID
}

object AdMobManager {
    @Volatile
    private var initialized = false

    fun initialize(context: Context) {
        if (initialized) return
        MobileAds.initialize(context.applicationContext) { initialized = true }
    }

    fun requestConsent(activity: Activity, onComplete: (Boolean) -> Unit) {
        val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
        val params = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { _ ->
                    onComplete(consentInformation.canRequestAds())
                }
            },
            { _ -> onComplete(consentInformation.canRequestAds()) }
        )
    }

    fun resetConsent(activity: Activity, onComplete: (Boolean) -> Unit) {
        UserMessagingPlatform.getConsentInformation(activity).reset()
        requestConsent(activity, onComplete)
    }
}

object InterstitialAdManager {
    private var ad: InterstitialAd? = null
    private var lastShownAt = 0L

    val isReady: Boolean get() = ad != null

    fun load(context: Context) {
        if (ad != null || AdIds.interstitial.isBlank()) return
        InterstitialAd.load(
            context,
            AdIds.interstitial,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(loadedAd: InterstitialAd) {
                    ad = loadedAd
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    ad = null
                }
            }
        )
    }

    fun showIfReady(activity: Activity, onClosed: () -> Unit = {}): Boolean {
        val current = ad ?: return false
        val now = System.currentTimeMillis()
        if (now - lastShownAt < INTERSTITIAL_COOLDOWN_MS) return false
        ad = null
        lastShownAt = now
        current.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onClosed()
                load(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                onClosed()
                load(activity)
            }
        }
        current.show(activity)
        return true
    }
}

object RewardedAdManager {
    private var ad: RewardedAd? = null

    val isReady: Boolean get() = ad != null

    fun load(context: Context) {
        if (ad != null || AdIds.rewarded.isBlank()) return
        RewardedAd.load(
            context,
            AdIds.rewarded,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(loadedAd: RewardedAd) {
                    ad = loadedAd
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    ad = null
                }
            }
        )
    }

    fun show(activity: Activity, onReward: () -> Unit, onClosed: () -> Unit): Boolean {
        val current = ad ?: return false
        ad = null
        current.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                onClosed()
                load(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                onClosed()
                load(activity)
            }
        }
        current.show(activity, OnUserEarnedRewardListener { onReward() })
        return true
    }
}

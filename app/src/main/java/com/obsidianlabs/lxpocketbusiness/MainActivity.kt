package com.obsidianlabs.lxpocketbusiness

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.obsidianlabs.lxpocketbusiness.data.repository.AppRepository
import com.obsidianlabs.lxpocketbusiness.ui.LxPocketBusinessApp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxPocketBusinessTheme

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val repository = AppRepository.getInstance(applicationContext)

        setContent {
            LxPocketBusinessTheme {
                LxPocketBusinessApp(repository)
            }
        }
    }
}

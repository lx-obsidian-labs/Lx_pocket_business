package com.obsidianlabs.lxpocketbusiness.ui.screens

import android.animation.ValueAnimator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ui.components.LxLogoLarge
import com.obsidianlabs.lxpocketbusiness.ui.theme.*

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit = {},
    completedMilestones: Int = 4,
    isReady: Boolean = true
) {
    val exit = remember { Animatable(0f) }
    val pulse = remember { Animatable(1f) }
    val onFinished by rememberUpdatedState(onSplashFinished)
    val animationsEnabled = ValueAnimator.areAnimatorsEnabled()
    val progress = completedMilestones.coerceIn(0, 4) / 4f

    LaunchedEffect(isReady) {
        if (isReady) {
            if (animationsEnabled) exit.animateTo(1f, tween(250))
            onFinished()
        }
    }
    LaunchedEffect(animationsEnabled) {
        if (animationsEnabled) {
            pulse.animateTo(1.06f, tween(250))
            pulse.animateTo(1f, tween(250))
        }
    }
    Box(
        Modifier.fillMaxSize()
            .graphicsLayer { alpha = 1f - exit.value; translationY = -size.height * 0.05f * exit.value }
            .background(Brush.linearGradient(listOf(LxBackgroundOlive, LxBackgroundSecondary, LxBackground)))
            .padding(24.dp)
    ) {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size(190.dp)
                    .background(Brush.radialGradient(listOf(LxLime.copy(alpha = 0.15f), androidx.compose.ui.graphics.Color.Transparent))),
                contentAlignment = Alignment.Center
            ) {
                LxLogoLarge(Modifier.graphicsLayer {
                    rotationZ = 360f * exit.value
                    scaleX = pulse.value * (1f - 0.3f * exit.value)
                    scaleY = scaleX
                }, size = 150.dp)
            }
            Text("LX Pocket\nBusiness", color = LxTextPrimary, fontSize = 30.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(24.dp))
            Text(when (completedMilestones) {
                0 -> "Getting things ready…"
                1 -> "Opening your business…"
                2 -> "Loading your records…"
                3 -> "Almost ready"
                else -> "Ready"
            }, color = LxTextSecondary)
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(0.85f), verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.weight(1f), color = LxLime)
                Spacer(Modifier.width(12.dp))
                Text("${(progress * 100).toInt()}%", color = LxTextPrimary)
            }
            Text("${completedMilestones.coerceIn(0, 4)} of 4 startup steps", color = LxTextSecondary, fontSize = 12.sp)
        }
        Text("powered by LX Obsidian Labs", color = LxTextSecondary, fontSize = 13.sp,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 16.dp))
    }
}

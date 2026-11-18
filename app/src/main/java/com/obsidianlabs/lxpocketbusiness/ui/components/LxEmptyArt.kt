package com.obsidianlabs.lxpocketbusiness.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxOutline
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxWarning

private fun artModifier() = Modifier
    .size(124.dp)
    .background(Brush.linearGradient(listOf(LxCardBg, Color(0xFF1B241B))), RoundedCornerShape(20.dp))
    .border(1.dp, LxOutline, RoundedCornerShape(20.dp))

@Composable
fun CustomersEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawCircle(LxLime.copy(alpha = 0.12f), w * 0.26f, Offset(w * 0.32f, h * 0.34f))
            drawCircle(LxLime.copy(alpha = 0.18f), w * 0.20f, Offset(w * 0.68f, h * 0.40f))
            drawLine(LxLime, Offset(w * 0.38f, h * 0.55f), Offset(w * 0.62f, h * 0.55f), 4f)
            drawLine(LxLime, Offset(w * 0.50f, h * 0.55f), Offset(w * 0.50f, h * 0.78f), 4f)
            drawLine(LxWarning, Offset(w * 0.16f, h * 0.82f), Offset(w * 0.84f, h * 0.82f), 3f)
            drawRoundRect(
                color = LxOutline,
                topLeft = Offset(w * 0.10f, h * 0.12f),
                size = Size(w * 0.80f, h * 0.76f),
                cornerRadius = CornerRadius(14f, 14f),
                style = Stroke(2f)
            )
        }
    }
}

@Composable
fun LedgerEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawRoundRect(LxLime.copy(alpha = 0.16f), Offset(w * 0.18f, h * 0.10f), Size(w * 0.64f, h * 0.80f), CornerRadius(10f, 10f))
            drawLine(LxOutline, Offset(w * 0.30f, h * 0.26f), Offset(w * 0.70f, h * 0.26f), 3f)
            drawLine(LxOutline, Offset(w * 0.30f, h * 0.40f), Offset(w * 0.70f, h * 0.40f), 3f)
            drawLine(LxOutline, Offset(w * 0.30f, h * 0.54f), Offset(w * 0.58f, h * 0.54f), 3f)
            drawLine(LxLime, Offset(w * 0.28f, h * 0.74f), Offset(w * 0.48f, h * 0.64f), 5f)
            drawLine(LxLime, Offset(w * 0.48f, h * 0.64f), Offset(w * 0.62f, h * 0.70f), 5f)
            drawLine(LxLime, Offset(w * 0.62f, h * 0.70f), Offset(w * 0.78f, h * 0.48f), 5f)
        }
    }
}

@Composable
fun DocumentsEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawRoundRect(LxOutline, Offset(w * 0.22f, h * 0.20f), Size(w * 0.52f, h * 0.66f), CornerRadius(10f, 10f), style = Stroke(2f))
            drawRoundRect(LxLime.copy(alpha = 0.18f), Offset(w * 0.30f, h * 0.12f), Size(w * 0.52f, h * 0.66f), CornerRadius(10f, 10f))
            drawLine(LxTextMuted, Offset(w * 0.38f, h * 0.36f), Offset(w * 0.72f, h * 0.36f), 3f)
            drawLine(LxTextMuted, Offset(w * 0.38f, h * 0.48f), Offset(w * 0.72f, h * 0.48f), 3f)
            drawLine(LxLime, Offset(w * 0.38f, h * 0.60f), Offset(w * 0.60f, h * 0.60f), 3f)
            val check = Path().apply {
                moveTo(w * 0.58f, h * 0.80f)
                lineTo(w * 0.68f, h * 0.90f)
                lineTo(w * 0.86f, h * 0.68f)
            }
            drawPath(check, LxLime, style = Stroke(5f))
        }
    }
}

@Composable
fun ReportsEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawRoundRect(LxOutline, Offset(w * 0.14f, h * 0.18f), Size(w * 0.72f, h * 0.64f), CornerRadius(12f, 12f), style = Stroke(2f))
            for (i in 0..3) {
                drawRoundRect(
                    LxLime.copy(alpha = 0.35f + i * 0.15f),
                    Offset(w * (0.26f + i * 0.14f), h * (0.66f - i * 0.08f)),
                    Size(w * 0.08f, h * (0.16f + i * 0.08f)),
                    CornerRadius(4f, 4f)
                )
            }
        }
    }
}

@Composable
fun SecurityEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawRoundRect(LxLime.copy(alpha = 0.16f), Offset(w * 0.30f, h * 0.44f), Size(w * 0.40f, h * 0.36f), CornerRadius(10f, 10f))
            drawRoundRect(LxLime, Offset(w * 0.36f, h * 0.24f), Size(w * 0.28f, h * 0.30f), CornerRadius(14f, 14f), style = Stroke(4f))
            drawCircle(LxLime, 5f, Offset(w * 0.50f, h * 0.58f))
            drawLine(LxLime, Offset(w * 0.50f, h * 0.60f), Offset(w * 0.50f, h * 0.70f), 4f)
        }
    }
}

@Composable
fun ErrorEmptyArt() {
    Box(artModifier(), Alignment.Center) {
        Canvas(Modifier.size(96.dp)) {
            val w = size.width
            val h = size.height
            drawCircle(LxWarning.copy(alpha = 0.18f), w * 0.38f, Offset(w * 0.50f, h * 0.50f))
            drawCircle(LxWarning, w * 0.38f, Offset(w * 0.50f, h * 0.50f), style = Stroke(4f))
            drawLine(LxWarning, Offset(w * 0.50f, h * 0.34f), Offset(w * 0.50f, h * 0.56f), 5f)
            drawCircle(LxWarning, 4f, Offset(w * 0.50f, h * 0.68f))
        }
    }
}

package com.obsidianlabs.lxpocketbusiness.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLimeDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary

/**
 * Large stylized pocket logo with document & chart as shown in the design.
 */
@Composable
fun LxLogoLarge(
    modifier: Modifier = Modifier,
    size: Dp = 140.dp
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            // 1. Draw floating Document graphic on top
            val docWidth = w * 0.42f
            val docHeight = h * 0.32f
            val docLeft = w * 0.29f
            val docTop = h * 0.05f

            // Document body background
            drawRoundRect(
                color = Color.White.copy(alpha = 0.95f),
                topLeft = Offset(docLeft, docTop),
                size = Size(docWidth, docHeight),
                cornerRadius = CornerRadius(8f, 8f)
            )

            // Document fold corner top right
            val pathFold = Path().apply {
                moveTo(docLeft + docWidth - 12f, docTop)
                lineTo(docLeft + docWidth, docTop + 12f)
                lineTo(docLeft + docWidth - 12f, docTop + 12f)
                close()
            }
            drawPath(path = pathFold, color = LxLimeDark)

            // Document lines inside
            val lineYStart = docTop + docHeight * 0.35f
            for (i in 0..2) {
                val lineY = lineYStart + (i * docHeight * 0.2f)
                drawLine(
                    color = Color(0xFF263226),
                    start = Offset(docLeft + 12f, lineY),
                    end = Offset(docLeft + docWidth - (if (i == 2) 24f else 12f), lineY),
                    strokeWidth = 4f
                )
            }

            // 2. Draw Pocket container
            val pocketLeft = w * 0.15f
            val pocketTop = h * 0.28f
            val pocketWidth = w * 0.70f
            val pocketHeight = h * 0.65f
            val cornerR = 24f

            val pocketPath = Path().apply {
                moveTo(pocketLeft, pocketTop)
                lineTo(pocketLeft + pocketWidth, pocketTop)
                lineTo(pocketLeft + pocketWidth, pocketTop + pocketHeight - cornerR)
                quadraticTo(
                    pocketLeft + pocketWidth, pocketTop + pocketHeight,
                    pocketLeft + pocketWidth - cornerR, pocketTop + pocketHeight
                )
                lineTo(pocketLeft + cornerR, pocketTop + pocketHeight)
                quadraticTo(
                    pocketLeft, pocketTop + pocketHeight,
                    pocketLeft, pocketTop + pocketHeight - cornerR
                )
                close()
            }

            // Outer pocket glow / dark fill
            drawPath(path = pocketPath, color = Color(0xFF141C14))
            
            // Pocket border
            drawPath(
                path = pocketPath,
                color = LxLime,
                style = Stroke(
                    width = 5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                )
            )

            // 3. Bar Chart inside pocket (right side top of pocket)
            val barW = pocketWidth * 0.18f
            val chartBottom = pocketTop + pocketHeight * 0.40f

            // Bar 1 (Short)
            drawRoundRect(
                color = LxLime.copy(alpha = 0.7f),
                topLeft = Offset(pocketLeft + pocketWidth * 0.38f, chartBottom - 20f),
                size = Size(barW, 20f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Bar 2 (Medium)
            drawRoundRect(
                color = LxLime.copy(alpha = 0.85f),
                topLeft = Offset(pocketLeft + pocketWidth * 0.58f, chartBottom - 35f),
                size = Size(barW, 35f),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Bar 3 (Tall)
            drawRoundRect(
                color = LxLime,
                topLeft = Offset(pocketLeft + pocketWidth * 0.78f, chartBottom - 50f),
                size = Size(barW, 50f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }

        // Center "LX" text inside pocket
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 36.dp)
        ) {
            Text(
                text = "LX",
                color = LxTextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Compact LX Logo for Top Bars
 */
@Composable
fun LxLogoSmall(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(LxCardBg)
                .border(1.dp, LxLime, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(24.dp)) {
                val w = size.width
                val h = size.height
                
                // Draw miniature pocket outline
                drawRoundRect(
                    color = LxLime,
                    topLeft = Offset(w * 0.1f, h * 0.15f),
                    size = Size(w * 0.8f, h * 0.75f),
                    cornerRadius = CornerRadius(6f, 6f),
                    style = Stroke(width = 2.5f)
                )

                // Mini bar chart
                drawRect(
                    color = LxLime,
                    topLeft = Offset(w * 0.55f, h * 0.3f),
                    size = Size(w * 0.12f, h * 0.4f)
                )
                drawRect(
                    color = LxLime,
                    topLeft = Offset(w * 0.70f, h * 0.2f),
                    size = Size(w * 0.12f, h * 0.5f)
                )
            }

            Text(
                text = "LX",
                color = LxLime,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 6.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "LX Pocket Business",
            color = LxTextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

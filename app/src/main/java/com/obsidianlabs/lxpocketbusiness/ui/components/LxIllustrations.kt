package com.obsidianlabs.lxpocketbusiness.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyInGreen
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxMoneyOutRed
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary

/**
 * Custom illustration of South African entrepreneur woman in store for Welcome Screen
 */
@Composable
fun EntrepreneurIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF3B281B),
                        Color(0xFF1E1710),
                        Color(0xFF12100E)
                    ),
                    radius = 500f
                )
            )
            .border(1.dp, Color(0xFF3D2F22), RoundedCornerShape(20.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Shop Shelves background
            drawRect(
                color = Color(0xFF2A1C12),
                topLeft = Offset(0f, 0f),
                size = Size(w, h * 0.65f)
            )

            // Wooden shelf lines
            drawLine(
                color = Color(0xFF4A3222),
                start = Offset(0f, h * 0.22f),
                end = Offset(w, h * 0.22f),
                strokeWidth = 10f
            )
            drawLine(
                color = Color(0xFF4A3222),
                start = Offset(0f, h * 0.45f),
                end = Offset(w, h * 0.45f),
                strokeWidth = 10f
            )

            // Products on shelves (colorful jars & boxes)
            // Shelf 1 products
            val shelf1Colors = listOf(
                Color(0xFFD97706), Color(0xFF059669), Color(0xFFDC2626),
                Color(0xFFD97706), Color(0xFF2563EB)
            )
            shelf1Colors.forEachIndexed { i, color ->
                val jarW = w * 0.10f
                val jarH = h * 0.12f
                val jarX = w * (0.08f + i * 0.18f)
                drawRoundRect(
                    color = color,
                    topLeft = Offset(jarX, h * 0.22f - jarH),
                    size = Size(jarW, jarH),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                // Jar lid
                drawRect(
                    color = Color(0xFFF59E0B),
                    topLeft = Offset(jarX - 2f, h * 0.22f - jarH - 8f),
                    size = Size(jarW + 4f, 8f)
                )
            }

            // Shelf 2 products
            val shelf2Colors = listOf(
                Color(0xFF059669), Color(0xFFDC2626), Color(0xFFF59E0B),
                Color(0xFF7C3AED), Color(0xFF059669)
            )
            shelf2Colors.forEachIndexed { i, color ->
                val jarW = w * 0.11f
                val jarH = h * 0.14f
                val jarX = w * (0.06f + i * 0.18f)
                drawRoundRect(
                    color = color,
                    topLeft = Offset(jarX, h * 0.45f - jarH),
                    size = Size(jarW, jarH),
                    cornerRadius = CornerRadius(6f, 6f)
                )
            }

            // 2. Character: Female South African Entrepreneur
            val headX = w * 0.65f
            val headY = h * 0.38f
            val headR = w * 0.18f

            // Torso / Body (Dark brown shirt)
            val torsoPath = Path().apply {
                moveTo(headX - headR * 1.3f, h)
                lineTo(headX - headR * 0.8f, headY + headR * 0.8f)
                lineTo(headX + headR * 0.8f, headY + headR * 0.8f)
                lineTo(headX + headR * 1.5f, h)
                close()
            }
            drawPath(path = torsoPath, color = Color(0xFF5C3317))

            // Apron (Striped Green)
            val apronPath = Path().apply {
                moveTo(headX - headR * 0.7f, headY + headR * 1.1f)
                lineTo(headX + headR * 0.7f, headY + headR * 1.1f)
                lineTo(headX + headR * 1.0f, h)
                lineTo(headX - headR * 1.0f, h)
                close()
            }
            drawPath(path = apronPath, color = Color(0xFF1E3A20))
            // Apron stripes
            for (s in -2..2) {
                drawLine(
                    color = Color(0xFF2E5E32),
                    start = Offset(headX + s * 25f, headY + headR * 1.1f),
                    end = Offset(headX + s * 35f, h),
                    strokeWidth = 8f
                )
            }

            // Face & Neck (Skin tone)
            val skinTone = Color(0xFF7C4A27)
            drawCircle(
                color = skinTone,
                center = Offset(headX, headY),
                radius = headR
            )

            // Warm Smile
            val smilePath = Path().apply {
                moveTo(headX - headR * 0.4f, headY + headR * 0.2f)
                quadraticTo(
                    headX, headY + headR * 0.6f,
                    headX + headR * 0.4f, headY + headR * 0.2f
                )
            }
            drawPath(
                path = smilePath,
                color = Color.White,
                style = Stroke(width = 8f)
            )

            // Eyes
            drawCircle(
                color = Color(0xFF26180E),
                center = Offset(headX - headR * 0.35f, headY - headR * 0.1f),
                radius = 6f
            )
            drawCircle(
                color = Color(0xFF26180E),
                center = Offset(headX + headR * 0.35f, headY - headR * 0.1f),
                radius = 6f
            )

            // African Pattern Headwrap / Headscarf (Doek)
            val wrapPath = Path().apply {
                moveTo(headX - headR * 1.05f, headY - headR * 0.2f)
                quadraticTo(
                    headX, headY - headR * 1.6f,
                    headX + headR * 1.05f, headY - headR * 0.2f
                )
                quadraticTo(
                    headX, headY - headR * 0.4f,
                    headX - headR * 1.05f, headY - headR * 0.2f
                )
                close()
            }
            drawPath(path = wrapPath, color = Color(0xFF047857))

            // Headwrap pattern highlights (Yellow/Orange African patterns)
            drawCircle(
                color = Color(0xFFF59E0B),
                center = Offset(headX - headR * 0.4f, headY - headR * 0.8f),
                radius = 12f
            )
            drawCircle(
                color = Color(0xFFF59E0B),
                center = Offset(headX + headR * 0.3f, headY - headR * 0.9f),
                radius = 14f
            )
        }
    }
}

/**
 * Custom Ledger / Accounting Notebook Illustration for Carousel Screen
 */
@Composable
fun LedgerNotebookIllustration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2B1F17),
                        Color(0xFF1E1610),
                        Color(0xFF14100C)
                    )
                )
            )
            .border(1.dp, Color(0xFF3B2E24), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Desktop elements & Notebook canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Desk background texture / highlights
            drawLine(
                color = Color(0xFF3D2C20),
                start = Offset(0f, h * 0.1f),
                end = Offset(w, h * 0.15f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0xFF3D2C20),
                start = Offset(0f, h * 0.85f),
                end = Offset(w, h * 0.9f),
                strokeWidth = 3f
            )

            // 2. Calculator on left side
            val calcLeft = w * 0.08f
            val calcTop = h * 0.35f
            val calcW = w * 0.22f
            val calcH = h * 0.48f

            drawRoundRect(
                color = Color(0xFF2A342A),
                topLeft = Offset(calcLeft, calcTop),
                size = Size(calcW, calcH),
                cornerRadius = CornerRadius(8f, 8f)
            )
            // Calc screen
            drawRoundRect(
                color = Color(0xFF6B8E23),
                topLeft = Offset(calcLeft + 6f, calcTop + 8f),
                size = Size(calcW - 12f, calcH * 0.22f),
                cornerRadius = CornerRadius(4f, 4f)
            )
            // Calc buttons grid
            for (row in 0..2) {
                for (col in 0..2) {
                    drawRect(
                        color = Color(0xFF1B231B),
                        topLeft = Offset(
                            calcLeft + 8f + col * (calcW / 3.4f),
                            calcTop + calcH * 0.36f + row * (calcH / 4.5f)
                        ),
                        size = Size(calcW / 4.2f, calcH / 5.5f)
                    )
                }
            }

            // 3. African Pattern Coffee Mug (top right)
            val mugX = w * 0.82f
            val mugY = h * 0.30f
            val mugR = w * 0.11f

            // Mug handle
            drawCircle(
                color = Color(0xFFD97706),
                center = Offset(mugX + mugR * 0.9f, mugY),
                radius = mugR * 0.5f,
                style = Stroke(width = 8f)
            )
            // Mug body
            drawCircle(
                color = Color(0xFF15803D),
                center = Offset(mugX, mugY),
                radius = mugR
            )
            // Mug coffee surface
            drawCircle(
                color = Color(0xFF3B2516),
                center = Offset(mugX, mugY),
                radius = mugR * 0.75f
            )
            // Steam curves rising
            val steamPath = Path().apply {
                moveTo(mugX - 10f, mugY - mugR - 5f)
                quadraticTo(mugX, mugY - mugR - 20f, mugX - 8f, mugY - mugR - 35f)
                moveTo(mugX + 10f, mugY - mugR - 10f)
                quadraticTo(mugX + 20f, mugY - mugR - 25f, mugX + 12f, mugY - mugR - 40f)
            }
            drawPath(
                path = steamPath,
                color = Color.White.copy(alpha = 0.5f),
                style = Stroke(width = 4f)
            )
        }

        // 4. Center Notebook Card Component
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFFFFBEA)) // Light cream notepad paper
                .border(1.dp, Color(0xFFE2D8B3), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Entry 1: Money In
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Money In",
                        color = LxTextDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = "Money In",
                        tint = LxMoneyInGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                    drawLine(Color(0xFFCBD5E1), Offset(0f, 0f), Offset(size.width, 0f), strokeWidth = 2f)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Entry 2: Money Out
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Money Out",
                        color = LxTextDark,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Money Out",
                        tint = LxMoneyOutRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                    drawLine(Color(0xFFCBD5E1), Offset(0f, 0f), Offset(size.width, 0f), strokeWidth = 2f)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Entry 3: Balance
                Text(
                    text = "Balance",
                    color = LxTextDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Canvas(modifier = Modifier.fillMaxWidth().height(1.dp)) {
                    drawLine(Color(0xFFCBD5E1), Offset(0f, 0f), Offset(size.width, 0f), strokeWidth = 2f)
                }
            }
        }
    }
}

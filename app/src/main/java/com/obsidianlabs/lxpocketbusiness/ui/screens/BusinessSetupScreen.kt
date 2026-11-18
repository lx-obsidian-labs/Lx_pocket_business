package com.obsidianlabs.lxpocketbusiness.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceBorder
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

@Composable
fun BusinessSetupScreen(
    onBackClick: () -> Unit = {},
    onCreateBusinessClick: (String, String) -> Unit = { _, _ -> },
    initialName: String = "",
    initialSector: String = ""
) {
    var businessName by rememberSaveable { mutableStateOf(initialName) }
    var selectedSector by rememberSaveable { mutableStateOf(initialSector) }
    var isSectorDropdownExpanded by remember { mutableStateOf(false) }

    val sectors = listOf(
        "Retail / Spaza Shop",
        "Services & Crafts",
        "Food & Catering",
        "Hair & Beauty",
        "Transport & Logistics",
        "Agriculture",
        "Other"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LxBackground)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Top Bar: Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LxTextPrimary
                )
            }

            // Headline & Subtitle
            Text(
                text = "Set up your business",
                color = LxTextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tell us about your business to get started.",
                color = LxTextSecondary,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Field 1: Business name *
            Text(
                text = buildAnnotatedString {
                    append("Business name ")
                    withStyle(style = SpanStyle(color = Color(0xFFFF9800))) {
                        append("*")
                    }
                },
                color = LxTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                placeholder = {
                    Text(
                        text = "Enter your business name",
                        color = LxTextMuted,
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = LxCardBg,
                    unfocusedContainerColor = LxCardBg,
                    focusedBorderColor = LxLime,
                    unfocusedBorderColor = LxSurfaceBorder,
                    focusedTextColor = LxTextPrimary,
                    unfocusedTextColor = LxTextPrimary
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Field 2: Business logo (optional)
            Text(
                text = "Business logo (optional)",
                color = LxTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LxCardBg)
                    .border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dashed add logo box
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF161E16))
                            .border(1.dp, LxLime.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Add logo",
                                tint = LxLime,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Add logo",
                                color = LxLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Use your business logo, such as your shop, service or brand.",
                        color = LxTextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Field 3: Business sector (optional)
            Text(
                text = "Business sector (optional)",
                color = LxTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LxCardBg)
                        .border(1.dp, LxSurfaceBorder, RoundedCornerShape(12.dp))
                        .clickable { isSectorDropdownExpanded = true }
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (selectedSector.isNotEmpty()) selectedSector else "Select a sector",
                            color = if (selectedSector.isNotEmpty()) LxTextPrimary else LxTextMuted,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = LxTextSecondary
                        )
                    }
                }

                DropdownMenu(
                    expanded = isSectorDropdownExpanded,
                    onDismissRequest = { isSectorDropdownExpanded = false },
                    modifier = Modifier
                        .background(LxCardBg)
                        .border(1.dp, LxSurfaceBorder)
                ) {
                    sectors.forEach { sector ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = sector,
                                    color = LxTextPrimary,
                                    fontSize = 14.sp
                                )
                            },
                            onClick = {
                                selectedSector = sector
                                isSectorDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom CTA Button: "Create Business →"
            Button(
                onClick = { onCreateBusinessClick(businessName.trim(), selectedSector) },
                enabled = businessName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LxLime,
                    contentColor = LxTextDark
                ),
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Create Business",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

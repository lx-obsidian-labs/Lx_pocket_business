package com.obsidianlabs.lxpocketbusiness.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.obsidianlabs.lxpocketbusiness.data.model.Business
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackground
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackgroundOlive
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxBackgroundSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxCardBg
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxOutline
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceVariant
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextDark
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextMuted
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextPrimary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxType

object LxSpacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
}

object LxShapes {
    val small = RoundedCornerShape(8.dp)
    val medium = RoundedCornerShape(12.dp)
    val large = RoundedCornerShape(16.dp)
    val pill = RoundedCornerShape(50)
}

@Composable
fun LxAppBackground(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(LxBackgroundOlive, LxBackgroundSecondary, LxBackground)
                )
            )
    ) {
        content()
    }
}

@Composable
fun LxCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = LxCardBg),
        shape = LxShapes.large,
        modifier = modifier
            .border(1.dp, LxOutline, LxShapes.large)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    ) {
        content()
    }
}

@Composable
fun LxScreenHeader(
    title: String,
    subtitle: String? = null,
    actions: @Composable () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.weight(1f)) {
            Text(title, style = LxType.screenTitle, color = LxTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) {
                Text(subtitle, style = LxType.caption, color = LxTextSecondary)
            }
        }
        actions()
    }
}

@Composable
fun LxSectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(title, style = LxType.sectionTitle, color = LxTextPrimary, modifier = Modifier.weight(1f))
        if (actionLabel != null) {
            Text(
                actionLabel,
                style = LxType.button,
                color = LxLime,
                modifier = Modifier
                    .clip(LxShapes.small)
                    .clickable(onClick = onActionClick)
                    .padding(horizontal = LxSpacing.sm, vertical = LxSpacing.xs)
            )
        }
    }
}

@Composable
fun LxSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = LxType.body, color = LxTextMuted) },
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, tint = LxTextSecondary, modifier = Modifier.size(18.dp)) }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LxLime,
            unfocusedBorderColor = LxOutline,
            focusedTextColor = LxTextPrimary,
            unfocusedTextColor = LxTextPrimary,
            cursorColor = LxLime
        ),
        shape = LxShapes.medium,
        singleLine = true,
        textStyle = LxType.body,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
    )
}

@Composable
fun LxFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, style = LxType.caption, maxLines = 1) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = LxSurfaceVariant,
            selectedLabelColor = LxLime,
            containerColor = LxCardBg,
            labelColor = LxTextSecondary
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = LxOutline,
            selectedBorderColor = LxLime
        ),
        shape = LxShapes.pill
    )
}

@Composable
fun LxPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = LxShapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = LxLime, contentColor = LxTextDark),
        modifier = modifier.heightIn(min = 48.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(LxSpacing.sm))
        }
        Text(text, style = LxType.button, maxLines = 1)
    }
}

@Composable
fun LxSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        shape = LxShapes.medium,
        border = androidx.compose.foundation.BorderStroke(1.dp, LxOutline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = LxTextPrimary),
        modifier = modifier.heightIn(min = 48.dp)
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = LxLime, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(LxSpacing.sm))
        }
        Text(text, style = LxType.button, maxLines = 1)
    }
}

@Composable
fun LxIconTile(
    icon: ImageVector,
    tint: Color = LxLime,
    size: Dp = 40.dp,
    background: Color = LxSurfaceVariant
) {
    Box(
        Modifier
            .size(size)
            .clip(LxShapes.small)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(size * 0.5f))
    }
}

@Composable
fun LxStatusPill(text: String, color: Color) {
    Box(
        Modifier
            .clip(LxShapes.pill)
            .background(color.copy(alpha = 0.14f))
            .border(1.dp, color.copy(alpha = 0.35f), LxShapes.pill)
            .padding(horizontal = LxSpacing.sm, vertical = LxSpacing.xs)
    ) {
        Text(text, style = LxType.caption, color = color, maxLines = 1)
    }
}

@Composable
fun LxSettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailing: String? = null,
    tint: Color = LxLime,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = LxSpacing.lg, vertical = LxSpacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LxIconTile(icon, tint = tint, size = 36.dp)
        Spacer(Modifier.width(LxSpacing.md))
        Column(Modifier.weight(1f)) {
            Text(title, style = LxType.cardTitle, color = LxTextPrimary, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (subtitle != null) {
                Text(subtitle, style = LxType.caption, color = LxTextSecondary, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
        if (trailing != null) {
            Text(trailing, style = LxType.caption, color = LxTextSecondary, maxLines = 1)
            Spacer(Modifier.width(LxSpacing.sm))
        }
        Icon(
            Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = LxTextSecondary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun LxBusinessAvatar(business: Business?, size: Dp = 44.dp) {
    val bitmap = remember(business?.logoPath) {
        business?.logoPath?.let { runCatching { BitmapFactory.decodeFile(it) }.getOrNull() }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Business logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(1.dp, LxOutline, CircleShape)
        )
    } else {
        Box(
            Modifier
                .size(size)
                .clip(CircleShape)
                .background(LxSurfaceVariant)
                .border(1.dp, LxLime.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                business?.name?.lxInitials().orEmpty().ifBlank { "LX" },
                style = LxType.button,
                color = LxLime
            )
        }
    }
}

@Composable
fun LxEmptyState(
    illustration: @Composable () -> Unit,
    title: String,
    body: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = LxSpacing.xl, vertical = LxSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        illustration()
        Spacer(Modifier.height(LxSpacing.lg))
        Text(title, style = LxType.sectionTitle, color = LxTextPrimary)
        Spacer(Modifier.height(LxSpacing.sm))
        Text(body, style = LxType.body, color = LxTextSecondary)
        if (actionLabel != null) {
            Spacer(Modifier.height(LxSpacing.xl))
            LxPrimaryButton(actionLabel, onActionClick)
        }
    }
}

@Composable
fun LxInfoButton(text: String) {
    var expanded = remember { androidx.compose.runtime.mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded.value = true }) {
            Icon(
                Icons.Filled.Info,
                contentDescription = "More information",
                tint = LxTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier.background(LxCardBg)
        ) {
            Text(text, style = LxType.caption, color = LxTextPrimary, modifier = Modifier.padding(LxSpacing.lg))
        }
    }
}

fun String.lxInitials(): String = trim()
    .split(Regex("\\s+"))
    .filter { it.isNotBlank() }
    .take(2)
    .mapNotNull { it.firstOrNull()?.uppercaseChar() }
    .joinToString("")

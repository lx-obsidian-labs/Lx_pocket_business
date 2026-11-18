package com.obsidianlabs.lxpocketbusiness.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxLime
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxSurfaceVariant
import com.obsidianlabs.lxpocketbusiness.ui.theme.LxTextSecondary

@Composable
fun AppBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val items: List<Pair<String, ImageVector>> = listOf(
        "Home" to Icons.Default.Home,
        "Customers" to Icons.Default.Group,
        "Money" to Icons.Default.AccountBalanceWallet,
        "Documents" to Icons.Default.Description,
        "More" to Icons.Default.MoreHoriz
    )
    NavigationBar(
        containerColor = LxSurfaceVariant,
        contentColor = LxTextSecondary,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, (label, icon) ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        icon,
                        contentDescription = label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        label,
                        fontSize = 11.sp,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = LxLime,
                    selectedTextColor = LxLime,
                    unselectedIconColor = LxTextSecondary,
                    unselectedTextColor = LxTextSecondary,
                    indicatorColor = LxLime.copy(alpha = 0.14f)
                )
            )
        }
    }
}

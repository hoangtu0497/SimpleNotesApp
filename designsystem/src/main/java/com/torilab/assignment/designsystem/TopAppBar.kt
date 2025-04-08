package com.torilab.assignment.designsystem

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppBar(
    text: String,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 4.dp),
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                ),
            )
        },
        colors = topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        modifier = modifier.shadow(elevation = 4.dp),
    )
}

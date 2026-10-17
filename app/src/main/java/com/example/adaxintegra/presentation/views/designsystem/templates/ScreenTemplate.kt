package com.example.adaxintegra.presentation.views.designsystem.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.adaxintegra.presentation.util.clearFocusOnTap
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader

// template: header + loading/error/content states, shared by screens
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ScreenTemplate(
    title: String,
    showBackButton: Boolean = true,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    isLoading: Boolean = false,
    error: String? = null,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    trailingAction: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppHeader(
            title = title,
            subtitle = subtitle,
            onBack = if (showBackButton) onBack else null,
            trailingAction = trailingAction,
            )

        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                RefreshContainer(isRefreshing = isRefreshing, onRefresh = onRefresh) {
                    if (error != null) {
                        //scrollable so the pull gesture works on the error state too
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = error,
                                style = AppTextStyle.BodyMedium,
                                fontWeight = FontWeight.Normal,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )
                        }
                    } else {
                    content()
                }
                }
            }
        }
    }
}



@Suppress("ktlint:standard:function-naming")
@Composable
private fun RefreshContainer(
    isRefreshing: Boolean,
    onRefresh: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    if (onRefresh == null) {
        content()
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            content()
        }
    }
}

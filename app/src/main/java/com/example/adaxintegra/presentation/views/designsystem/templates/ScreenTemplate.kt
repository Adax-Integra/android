package com.example.adaxintegra.presentation.views.designsystem.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader

// template: header + loading/error/content states, shared by screens
@Suppress("ktlint:standard:function-naming")
@Composable
fun ScreenTemplate(
    title: String,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    isLoading: Boolean = false,
    error: String? = null,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppHeader(title = title, subtitle = subtitle, onBack = onBack)

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
                                .verticalScroll(rememberScrollState()),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = error,
                                style = AppTextStyle.BodyMedium,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    else {
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

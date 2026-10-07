package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.test.user_attributes

// icons available to use
object AppIcons {
    val Home: ImageVector
        get() = Icons.Default.Home

    val Folder: ImageVector
        get() = Icons.Default.Folder

    val Profile: ImageVector
        get() = Icons.Default.Person

    val ArrowBack: ImageVector
        get() = Icons.AutoMirrored.Filled.ArrowBack

    val Notifications: ImageVector
        get() = Icons.Default.Notifications

    val CheckMark: ImageVector
        get() = Icons.Default.Check

    val Search: ImageVector
        get() = Icons.Default.Search

    val UserAttributes: ImageVector
        get() = user_attributes

    val Lock: ImageVector
        get() = Icons.Default.Lock

    val ChevronRight: ImageVector
        get() = Icons.Default.ChevronRight

    val Bell: ImageVector
        get() = Icons.Default.NotificationsNone

    val Account: ImageVector
        get() = Icons.Default.AccountCircle

    val Info: ImageVector
        get() = Icons.Default.Info

    val Location: ImageVector
        get() = Icons.Default.LocationOn

    val Clock: ImageVector
        get() = Icons.Default.Schedule
}

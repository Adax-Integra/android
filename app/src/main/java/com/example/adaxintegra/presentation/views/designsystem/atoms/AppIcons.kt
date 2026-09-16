package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

//icons available to use
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
}
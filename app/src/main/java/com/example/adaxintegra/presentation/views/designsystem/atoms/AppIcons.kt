package com.example.adaxintegra.presentation.views.designsystem.atoms

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.test.inbox_text_person
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

    val Add: ImageVector
        get() = Icons.Default.Add

    val Search: ImageVector
        get() = Icons.Default.Search

    val UserAttributes: ImageVector
        get() = user_attributes

    val InboxText: ImageVector
        get() = inbox_text_person

    val Lock: ImageVector
        get() = Icons.Default.Lock

    val Delete: ImageVector
        get() = Icons.Outlined.Delete

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

    val Admin: ImageVector
        get() = Icons.Default.Shield

    val People: ImageVector
        get() = Icons.Default.People

    val History: ImageVector
        get() = Icons.Default.History

    val Reports: ImageVector
        get() = Icons.Default.Description

    val Calendar: ImageVector
        get() = Icons.Default.CalendarMonth

    val Email: ImageVector
        get() = Icons.Outlined.MailOutline

    val Edit: ImageVector
        get() = Icons.Default.Edit

    val Close: ImageVector
        get() = Icons.Default.Close

    val Phone: ImageVector
        get() = Icons.Default.Phone

    val IdCard: ImageVector
        get() = Icons.Default.Badge

    val Upload: ImageVector
        get() = Icons.Outlined.FileUpload

    val UploadedImage: ImageVector
        get() = Icons.Outlined.Image

    val Download: ImageVector
        get() = Icons.Outlined.ArrowCircleDown

    val Replace: ImageVector
        get() = Icons.Default.Sync

    val Gallery: ImageVector
        get() = Icons.Outlined.PhotoLibrary

    val Files: ImageVector
        get() = Icons.Outlined.FolderOpen
}

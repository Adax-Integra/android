package com.example.test

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

// I added this icon directly from the Google material icon library
// It is a separate file because it does not come standard with the library we have installed
// specifically from https://fonts.gstatic.com/render/v1/Material+Symbols+Outlined/24dp/user_attributes.kt?var=opsz,wght,FILL,GRAD,ROND@24,400,1,0,50
@Suppress("CheckReturnValue")
public val user_attributes: ImageVector
    get() {
        if (_user_attributes != null) {
            return _user_attributes!!
        }
        _user_attributes =
            ImageVector.Builder(
                name = "user_attributes",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(14f, 7f)
                        verticalLineTo(5f)
                        horizontalLineToRelative(8f)
                        verticalLineTo(7f)
                        horizontalLineTo(14f)
                        close()
                        moveToRelative(0f, 4f)
                        verticalLineTo(9f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(14f)
                        close()
                        moveToRelative(0f, 4f)
                        verticalLineTo(13f)
                        horizontalLineToRelative(8f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(14f)
                        close()
                        moveTo(5.88f, 13.13f)
                        quadTo(5f, 12.25f, 5f, 11f)
                        reflectiveQuadTo(5.88f, 8.88f)
                        reflectiveQuadTo(8f, 8f)
                        reflectiveQuadToRelative(2.13f, 0.88f)
                        reflectiveQuadTo(11f, 11f)
                        reflectiveQuadToRelative(-0.88f, 2.13f)
                        reflectiveQuadTo(8f, 14f)
                        reflectiveQuadTo(5.88f, 13.13f)
                        close()
                        moveTo(2f, 20f)
                        verticalLineTo(18.1f)
                        quadToRelative(0f, -0.53f, 0.25f, -1f)
                        reflectiveQuadToRelative(0.7f, -0.75f)
                        quadTo(4.08f, 15.68f, 5.34f, 15.34f)
                        reflectiveQuadTo(8f, 15f)
                        reflectiveQuadToRelative(2.66f, 0.34f)
                        reflectiveQuadToRelative(2.39f, 1.01f)
                        quadToRelative(0.45f, 0.27f, 0.7f, 0.75f)
                        reflectiveQuadToRelative(0.25f, 1f)
                        verticalLineTo(20f)
                        horizontalLineTo(2f)
                        close()
                    }
                }
                .build()
        return _user_attributes!!
    }

private var _user_attributes: ImageVector? = null

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
// specifically from https://fonts.gstatic.com/render/v1/Material+Symbols+Outlined/24dp/inbox_text_person.kt?var=opsz,wght,FILL,GRAD,ROND@24,400,0,0,50
@Suppress("CheckReturnValue")
public val inbox_text_person: ImageVector
    get() {
        if (_inbox_text_person != null) {
            return _inbox_text_person!!
        }
        _inbox_text_person =
            ImageVector.Builder(
                name = "inbox_text_person",
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
                        moveTo(14f, 20f)
                        verticalLineTo(19.43f)
                        quadToRelative(0f, -0.6f, 0.33f, -1.1f)
                        reflectiveQuadToRelative(0.9f, -0.75f)
                        quadToRelative(0.7f, -0.27f, 1.36f, -0.43f)
                        reflectiveQuadTo(18.08f, 17f)
                        quadToRelative(0.73f, 0f, 1.43f, 0.15f)
                        reflectiveQuadToRelative(1.28f, 0.43f)
                        quadToRelative(0.57f, 0.25f, 0.9f, 0.75f)
                        reflectiveQuadTo(22f, 19.43f)
                        verticalLineTo(20f)
                        horizontalLineTo(14f)
                        close()
                        moveToRelative(4f, -4f)
                        quadToRelative(-0.82f, 0f, -1.41f, -0.59f)
                        reflectiveQuadTo(16f, 14f)
                        reflectiveQuadToRelative(0.59f, -1.41f)
                        reflectiveQuadTo(18f, 12f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        quadTo(20f, 13.18f, 20f, 14f)
                        reflectiveQuadToRelative(-0.59f, 1.41f)
                        reflectiveQuadTo(18f, 16f)
                        close()
                        moveTo(5f, 21f)
                        quadTo(4.18f, 21f, 3.59f, 20.41f)
                        reflectiveQuadTo(3f, 19f)
                        verticalLineTo(5f)
                        quadTo(3f, 4.17f, 3.59f, 3.59f)
                        reflectiveQuadTo(5f, 3f)
                        horizontalLineTo(19f)
                        quadToRelative(0.83f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(21f, 5f)
                        verticalLineToRelative(5.65f)
                        quadTo(20.53f, 10.43f, 20.03f, 10.29f)
                        reflectiveQuadTo(19f, 10.07f)
                        verticalLineTo(5f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(9f)
                        horizontalLineTo(9.2f)
                        quadToRelative(0.22f, 0.67f, 0.75f, 1.17f)
                        reflectiveQuadToRelative(1.17f, 0.7f)
                        quadToRelative(-0.07f, 0.5f, -0.1f, 1.01f)
                        reflectiveQuadToRelative(0.05f, 1.01f)
                        quadTo(10.18f, 17.73f, 9.39f, 17.24f)
                        reflectiveQuadTo(8f, 16f)
                        horizontalLineTo(5f)
                        verticalLineToRelative(3f)
                        horizontalLineToRelative(6.33f)
                        quadToRelative(0.17f, 0.55f, 0.4f, 1.05f)
                        reflectiveQuadTo(12.28f, 21f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(5f, 19f)
                        horizontalLineTo(8f)
                        quadToRelative(0.6f, 0f, 1.39f, 0f)
                        reflectiveQuadToRelative(1.69f, 0f)
                        quadToRelative(0.05f, 0f, 0.11f, 0f)
                        reflectiveQuadToRelative(0.14f, 0f)
                        horizontalLineTo(5f)
                        close()
                        moveTo(7f, 8.75f)
                        horizontalLineTo(17f)
                        verticalLineToRelative(-2f)
                        horizontalLineTo(7f)
                        verticalLineToRelative(2f)
                        close()
                        moveToRelative(0f, 3.5f)
                        horizontalLineToRelative(5.93f)
                        quadToRelative(0.68f, -0.73f, 1.51f, -1.23f)
                        quadToRelative(0.84f, -0.5f, 1.81f, -0.77f)
                        horizontalLineTo(7f)
                        verticalLineToRelative(2f)
                        close()
                    }
                }
                .build()
        return _inbox_text_person!!
    }

private var _inbox_text_person: ImageVector? = null

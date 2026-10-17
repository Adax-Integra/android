package com.example.adaxintegra.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.adaxintegra.domain.model.RecordDocumentType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

// V-13: turns the downloaded bytes into images the screen can draw.
// PDFs are drawn with the Android PdfRenderer, so the document never leaves the app
class DocumentPreviewRenderer @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun render(bytes: ByteArray, type: RecordDocumentType): List<ImageBitmap> = withContext(Dispatchers.IO) {
        when (type) {
            RecordDocumentType.IMAGE -> listOfNotNull(decodeImage(bytes))
            RecordDocumentType.PDF -> renderPdf(bytes)
        }
    }

    // Photos can be 4000 px wide, they are scaled down so the bitmap does not use too much memory
    private fun decodeImage(bytes: ByteArray): ImageBitmap? {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)

        var sampleSize = 1
        while (bounds.outWidth / (sampleSize * 2) >= TARGET_WIDTH_PX) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)?.asImageBitmap()
    }

    // PdfRenderer needs a file, the PDF lives in the cache only while it is drawn
    private fun renderPdf(bytes: ByteArray): List<ImageBitmap> {
        val file = File.createTempFile("v13-", ".pdf", context.cacheDir)

        return try {
            file.writeBytes(bytes)

            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { descriptor ->
                PdfRenderer(descriptor).use { renderer ->
                    val pageCount = minOf(renderer.pageCount, MAX_PDF_PAGES)

                    (0 until pageCount).map { index ->
                        renderer.openPage(index).use { page ->
                            val scale = TARGET_WIDTH_PX.toFloat() / page.width
                            val bitmap = Bitmap.createBitmap(
                                TARGET_WIDTH_PX,
                                (page.height * scale).toInt(),
                                Bitmap.Config.ARGB_8888,
                            )
                            // PDF pages are transparent, a white background keeps the text readable in dark mode
                            bitmap.eraseColor(Color.WHITE)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            bitmap.asImageBitmap()
                        }
                    }
                }
            }
        } finally {
            file.delete()
        }
    }

    private companion object {
        const val TARGET_WIDTH_PX = 1080

        // INE front and back, plus some margin for multi-page receipts
        const val MAX_PDF_PAGES = 4
    }
}

package com.example.adaxintegra.presentation.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.example.adaxintegra.presentation.model.SharedDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

// Folder inside cacheDir declared in res/xml/file_paths.xml
private const val SHARED_DOCUMENTS_FOLDER = "documents"

// R-06: The file is written in the app cache,
// never in the public Downloads folder, and handed to the app the user picks
// (Files, Drive, WhatsApp...). Only the last document is kept, the folder is
// emptied before writing.
suspend fun shareDocument(
    context: Context,
    document: SharedDocument,
) {
    val uri = withContext(Dispatchers.IO) {
        val folder = File(context.cacheDir, SHARED_DOCUMENTS_FOLDER)
        folder.deleteRecursively()
        folder.mkdirs()

        val file = File(folder, document.fileName)
        file.writeBytes(document.bytes)

        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = document.mimeType
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Guardar o compartir documento"))
}

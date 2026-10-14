
package com.example.adaxintegra.presentation.views.screens.profile

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adaxintegra.domain.model.UploadDocumentFile
import com.example.adaxintegra.presentation.viewmodel.UploadDocumentsViewModel
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppButton
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppIcons
import com.example.adaxintegra.presentation.views.designsystem.atoms.AppTextStyle
import com.example.adaxintegra.presentation.views.designsystem.atoms.Text
import com.example.adaxintegra.presentation.views.designsystem.molecules.DocumentUploadCard
import com.example.adaxintegra.presentation.views.designsystem.organisms.AppHeader
import com.example.adaxintegra.ui.theme.AdaxIntegraTheme
import com.example.adaxintegra.ui.theme.Purple
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("ktlint:standard:function-naming")
@Composable
fun UploadDocumentsScreen(
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel: UploadDocumentsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var isPreparing by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf("") }

    var identityUri by remember { mutableStateOf<Uri?>(null) }
    var addressUri by remember { mutableStateOf<Uri?>(null) }

    var identityError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }

    val identityPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            identityError = validateDocument(context, uri)

            if (identityError.isEmpty()) {
                identityUri = uri
            } else {
                identityUri = null
            }
        }
    }

    val addressPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        if (uri != null) {
            addressError = validateDocument(context, uri)

            if (addressError.isEmpty()) {
                addressUri = uri
            } else {
                addressUri = null
            }
        }
    }

    UploadDocumentsContent(
        uiState = uiState,
        identityName = identityUri?.let {
            getDocumentName(context, it)
        },
        addressName = addressUri?.let {
            getDocumentName(context, it)
        },
        identityError = identityError,
        addressError = addressError,
        uploadError = uploadError,
        isPreparing = isPreparing,
        onBack = onBack,
        onIdentityClick = {
            identityPicker.launch(
                arrayOf(
                    "application/pdf",
                    "image/png",
                    "image/jpeg",
                ),
            )
        },
        onAddressClick = {
            addressPicker.launch(
                arrayOf(
                    "application/pdf",
                    "image/png",
                    "image/jpeg",
                ),
            )
        },
        onSubmit = {
            val selectedIdentity = identityUri
            val selectedAddress = addressUri

            if (
                !isPreparing &&
                !uiState.isLoading &&
                (selectedIdentity != null || selectedAddress != null)
            ) {
                isPreparing = true
                uploadError = ""

                scope.launch {
                    try {
                        val identityFile = withContext(Dispatchers.IO) {
                            selectedIdentity?.let {
                                readDocumentFile(context, it)
                            }
                        }

                        val addressFile = withContext(Dispatchers.IO) {
                            selectedAddress?.let {
                                readDocumentFile(context, it)
                            }
                        }

                        viewModel.uploadDocuments(
                            identityDocument = identityFile,
                            proofOfAddress = addressFile,
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: IllegalArgumentException) {
                        uploadError = e.message
                            ?: "No se pudo validar el documento."
                    } catch (e: Exception) {
                        uploadError = "No se pudieron leer los documentos."
                    } finally {
                        isPreparing = false
                    }
                }
            }
        },
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
private fun UploadDocumentsContent(
    uiState: UploadDocumentsUiState,
    identityName: String?,
    addressName: String?,
    identityError: String,
    addressError: String,
    uploadError: String,
    isPreparing: Boolean,
    onBack: () -> Unit,
    onIdentityClick: () -> Unit,
    onAddressClick: () -> Unit,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        AppHeader(
            title = "Documentos de Identidad",
            onBack = onBack,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Comprobante de identidad",
            style = AppTextStyle.LabelMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        DocumentUploadCard(
            onClick = onIdentityClick,
        )

        if (identityName != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = identityName,
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        if (identityError.isNotEmpty()) {
            Text(
                text = identityError,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = AppIcons.Info,
                contentDescription = null,
                tint = Purple,
                modifier = Modifier.size(18.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Puedes usar tu INE, pasaporte o cualquier documento que compruebe tu identidad",
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Comprobante de domicilio",
            style = AppTextStyle.LabelMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        DocumentUploadCard(
            onClick = onAddressClick,
        )

        if (addressName != null) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = addressName,
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        if (addressError.isNotEmpty()) {
            Text(
                text = addressError,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = AppIcons.Info,
                contentDescription = null,
                tint = Purple,
                modifier = Modifier.size(18.dp),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Usa un comprobante de domicilio de menos de 3 meses de antigüedad.",
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Enviar documentos",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = (identityName != null || addressName != null) &&
                !isPreparing && !uiState.isLoading,
            isLoading = isPreparing || uiState.isLoading,
        )

        if (uploadError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uploadError,
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Subiendo documentos...",
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.errorMessage ?: "",
                style = AppTextStyle.BodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        if (uiState.isSuccess) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Documentos enviados correctamente.",
                style = AppTextStyle.BodySmall,
                color = Purple,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

private fun validateDocument(
    context: Context,
    uri: Uri,
): String {
    val type = context.contentResolver.getType(uri)

    if (
        type != "application/pdf" &&
        type != "image/png" &&
        type != "image/jpeg"
    ) {
        return "Formato no permitido. Selecciona PDF, PNG o JPG."
    }

    val cursor = context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.SIZE),
        null,
        null,
        null,
    )

    val size = cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.SIZE)

            if (index >= 0 && !it.isNull(index)) {
                it.getLong(index)
            } else {
                null
            }
        } else {
            null
        }
    }

    if (size == null) {
        return "No se pudo comprobar el tamaño del archivo."
    }

    if (size > 4 * 1024 * 1024) {
        return "El archivo supera el límite de 4 MB."
    }

    return ""
}

private fun getDocumentName(
    context: Context,
    uri: Uri,
): String {
    val cursor = context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.DISPLAY_NAME),
        null,
        null,
        null,
    )

    return cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)

            if (index >= 0) {
                it.getString(index)
            } else {
                null
            }
        } else {
            null
        }
    } ?: "Documento seleccionado"
}

private fun readDocumentFile(
    context: Context,
    uri: Uri,
): UploadDocumentFile {
    val error = validateDocument(context, uri)

    if (error.isNotEmpty()) {
        throw IllegalArgumentException(error)
    }

    val fileName = getDocumentName(context, uri)

    val mimeType = context.contentResolver.getType(uri)
        ?: throw IllegalArgumentException("Formato desconocido")

    val bytes = context.contentResolver.openInputStream(uri)?.use {
        it.readBytes()
    } ?: throw IllegalArgumentException("No se pudo leer el archivo")

    if (bytes.size > 4 * 1024 * 1024) {
        throw IllegalArgumentException("El archivo supera los 4 MB")
    }

    return UploadDocumentFile(
        fileName = fileName,
        mimeType = mimeType,
        bytes = bytes,
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 804,
)
@Composable
private fun UploadDocumentsScreenPreview() {
    AdaxIntegraTheme {
        UploadDocumentsContent(
            uiState = UploadDocumentsUiState(),
            identityName = "Identificacion_ejemplo.pdf",
            addressName = null,
            identityError = "",
            addressError = "",
            uploadError = "",
            isPreparing = false,
            onBack = {},
            onIdentityClick = {},
            onAddressClick = {},
            onSubmit = {},
        )
    }
}

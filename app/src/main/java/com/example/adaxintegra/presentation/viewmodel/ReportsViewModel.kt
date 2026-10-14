package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adaxintegra.domain.usecases.DownloadReportUseCase
import com.example.adaxintegra.presentation.views.screens.reports.ReportSelectRangeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val downloadReportUseCase: DownloadReportUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportSelectRangeUiState())
    val uiState = _uiState.asStateFlow()

    fun setStartDate(calendar: Calendar) {
        _uiState.update {
            it.copy(
                startDate = calendar,
                startDateError = null,
                endDateError = null,
                selectedChip = null,
                error = null,
                downloadedFile = null,
            )
        }
    }

    fun setEndDate(calendar: Calendar) {
        _uiState.update {
            it.copy(
                endDate = calendar,
                endDateError = null,
                selectedChip = null,
                error = null,
                downloadedFile = null,
            )
        }
    }

    fun downloadReport() {
        val startDate = _uiState.value.startDate
        val endDate = _uiState.value.endDate

        if (startDate == null) {
            _uiState.update {
                it.copy(startDateError = "Selecciona una fecha de inicio")
            }
        }
        if (endDate == null) {
            _uiState.update {
                it.copy(endDateError = "Selecciona una fecha de fin")
            }
        }

        if (startDate != null && endDate != null) {
            // We get the current date
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // We get the start date at the first time of the day
            val startZero = (startDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // We get the end date at the first time of the day
            val endZero = (endDate.clone() as Calendar).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // If any date is after today, we send an error
            if (startZero.after(today)) {
                _uiState.update {
                    it.copy(startDateError = "La fecha de inicio no puede ser en el futuro")
                }
                return
            }

            if (endZero.after(today)) {
                _uiState.update {
                    it.copy(endDateError = "La fecha de fin no puede ser en el futuro")
                }
                return
            }

            if (startDate.after(endDate)) {
                _uiState.update {
                    it.copy(endDateError = "La fecha final debe ser después a la inicial")
                }
                return
            }

            // Check that the range is not greater than 1 year
            val diffInMillis = endDate.timeInMillis - startDate.timeInMillis
            val oneYearInMillis = 366L * 24 * 60 * 60 * 1000L
            if (diffInMillis > oneYearInMillis) {
                _uiState.update {
                    it.copy(endDateError = "El rango del reporte no puede ser mayor a un año")
                }
                return
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val startDateStr = dateFormat.format(startDate.time)
            val endDateStr = dateFormat.format(endDate.time)

            viewModelScope.launch {
                // Reset file before starting new download
                _uiState.update { it.copy(isLoading = true, error = null, downloadedFile = null) }

                try {
                    val file = downloadReportUseCase(startDateStr, endDateStr)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            downloadedFile = file,
                            error = null,
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al descargar el reporte",
                        )
                    }
                }
            }
        }
    }

    // When selecting the chip of This month, it will set the start date to the first day of the
    // current month and the end date to the current date.
    fun chipThisMonthSelected() {
        val firstOfMonth = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
        val endOfMonth = Calendar.getInstance()
        _uiState.update {
            it.copy(
                startDate = firstOfMonth,
                endDate = endOfMonth,
                selectedChip = "Mes actual",
                error = null,
                startDateError = null,
                endDateError = null,
                downloadedFile = null,
            )
        }
    }

    // When selecting the chip of Previous month, it will set the range to the
    // previous complete month
    fun chipPreviousMonthSelected() {
        val startDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        val endDate = Calendar.getInstance().apply {
            add(Calendar.MONTH, -1)
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
        }
        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate,
                selectedChip = "Mes anterior",
                error = null,
                startDateError = null,
                endDateError = null,
                downloadedFile = null,
            )
        }
    }

    // When selecting the chip of Previous trimester, it will set the range to the
    // previous completed trimester
    fun chipPreviousTrimester() {
        val now = Calendar.getInstance()
        val currMonth = now.get(Calendar.MONTH)

        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        if (currMonth in 0..2) {
            val prevYear = now.get(Calendar.YEAR) - 1
            startDate.set(prevYear, Calendar.OCTOBER, 1)
            endDate.set(prevYear, Calendar.DECEMBER, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        if (currMonth in 3..5) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.JANUARY, 1)
            endDate.set(currentYear, Calendar.MARCH, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        if (currMonth in 6..8) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.APRIL, 1)
            endDate.set(currentYear, Calendar.JUNE, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        if (currMonth in 9..11) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.JULY, 1)
            endDate.set(currentYear, Calendar.SEPTEMBER, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate,
                selectedChip = "Trimestre previo",
                error = null,
                startDateError = null,
                endDateError = null,
                downloadedFile = null,
            )
        }
    }
}

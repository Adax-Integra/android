package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.adaxintegra.presentation.views.screens.reports.ReportSelectRangeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    // TODO create the usecase
    // private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportSelectRangeUiState())
    val uiState = _uiState.asStateFlow()

    // When selecting the chip of This month, it will set the start date to the first day of the
    // current month and the end date to the current date.
    fun chipThisMonthSelected() {
        val firstOfMonth = Calendar.getInstance().apply { set(Calendar.DAY_OF_MONTH, 1) }
        val endOfMonth = Calendar.getInstance()
        _uiState.update {
            it.copy(
                startDate = firstOfMonth,
                endDate = endOfMonth
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
                endDate = endDate
            )
        }
    }

    // When selecting the chip of Previous trimester, it will set the range to the
    // previous completed trimester
    fun chipPreviousTrimester() {
        // For this part we first get the current month
        // With this we can get the previous complete trimester
        val now = Calendar.getInstance()
        val currMonth = now.get(Calendar.MONTH)

        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()

        // If we are in the 1st trimester we get the 4th trimester of the previous year
        if (currMonth in 0..2) {
            val prevYear = now.get(Calendar.YEAR) - 1
            startDate.set(prevYear, Calendar.OCTOBER, 1)
            endDate.set(prevYear, Calendar.DECEMBER, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        // If we are in the 2nd trimester we get the 1st trimester of the year
        if (currMonth in 3..5) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.JANUARY, 1)
            endDate.set(currentYear, Calendar.MARCH, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        // If we are in the 3rd trimester we get the 2nd trimester of the year
        if (currMonth in 6..8) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.APRIL, 1)
            endDate.set(currentYear, Calendar.JUNE, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        // If we are in the 4th trimester we get the 3rd trimester of the year
        if (currMonth in 9..11) {
            val currentYear = now.get(Calendar.YEAR)
            startDate.set(currentYear, Calendar.JULY, 1)
            endDate.set(currentYear, Calendar.SEPTEMBER, 1)
            endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH))
        }

        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = endDate
            )
        }
    }
}

package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.adaxintegra.domain.model.UserSession
import com.example.adaxintegra.domain.usecases.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    observeSessionUseCase: ObserveSessionUseCase,
) : ViewModel() {
    // Navigation observes this state to select the oppropriate views
    val session: StateFlow<UserSession?> = observeSessionUseCase()
}

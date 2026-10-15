package com.example.adaxintegra.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.adaxintegra.presentation.viewmodel.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(
    //private val editProfileUseCase: EditProfileProfileUseCase,
//private val authRepository: AuthRepository, // get session to get userId
) : ViewModel() {

   private val _uiState = MutableStateFlow(EditProfileUiState(
       isLoading = true,
       name = TODO(),
       lastName = TODO(),
       urgency = TODO(),
       country = TODO(),
       state = TODO(),
       municipality = TODO(),
       isEditing = TODO(),
       error = TODO(),
       isRefreshing = TODO()
   ))
   val uiState: StateFlow<EditProfileUiState> = _uiState.asStateFlow()
    }

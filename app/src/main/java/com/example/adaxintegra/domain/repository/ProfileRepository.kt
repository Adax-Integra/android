package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.EditableProfile
import com.example.adaxintegra.domain.model.ProfileView

interface ProfileRepository {
    //profile view
    suspend fun getProfileByUserId(userId: String): ProfileView

    //edit profile
    suspend fun getEditableProfile(userId: String): EditableProfile

    //update profile
    suspend fun updateProfile(userId: String, fields: Map<String, String>)
    
    // G-08: deactivates the account of the signed in user
    suspend fun deactivateAccount(userId: String)
}

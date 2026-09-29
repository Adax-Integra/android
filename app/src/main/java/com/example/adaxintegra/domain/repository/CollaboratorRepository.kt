package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator

interface CollaboratorRepository {
    // G-03: returns the created collaborator or throws the error
    suspend fun createCollaborator(token : String, collaborator: NewCollaborator): Collaborator
}

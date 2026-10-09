package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.CollaboratorSummary
import com.example.adaxintegra.domain.model.NewCollaborator
import com.example.adaxintegra.domain.model.modifyCollaborator

interface CollaboratorRepository {
    // G-03: returns the created collaborator or throws the error
    suspend fun createCollaborator(collaborator: NewCollaborator): Collaborator

    // G-06: returns every internal account, newest first
    suspend fun getCollaborators(): List<CollaboratorSummary>

}

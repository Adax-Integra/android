package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
import kotlinx.coroutines.flow.Flow

interface CollaboratorRepository {
    fun createCollaborator(token: String, collaborator: NewCollaborator): Flow<Result<Collaborator>>
}

package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Collaborator
import com.example.adaxintegra.domain.model.NewCollaborator
package com.example.adaxintegra.domain.repository

interface CollaboratorRepository {
    // G-03: returns the created collaborator or throws the error
    suspend fun createCollaborator(collaborator: NewCollaborator): Collaborator

}

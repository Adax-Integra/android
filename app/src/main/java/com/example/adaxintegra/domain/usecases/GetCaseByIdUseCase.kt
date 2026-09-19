package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.model.Case
import com.example.adaxintegra.domain.repository.CaseRepository
import javax.inject.Inject

//caseRepository receives case id and returns Case from domain
class GetCaseByIdUseCase
@Inject
constructor(
    private val caseRepository: CaseRepository,
) {
    suspend operator fun invoke(caseId: String): Case {
        return caseRepository.getCaseById(caseId)
        }
}
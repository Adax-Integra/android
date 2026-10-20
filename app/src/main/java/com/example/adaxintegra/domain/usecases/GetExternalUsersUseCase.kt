package com.example.adaxintegra.domain.usecases

import com.example.adaxintegra.domain.common.Result
import com.example.adaxintegra.domain.model.ExternalUserPage
import com.example.adaxintegra.domain.repository.ExternalUserRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// G-16: the admin consults the list of external accounts
class GetExternalUsersUseCase @Inject constructor(
    private val repository: ExternalUserRepository,
) {
    operator fun invoke(page: Int, search: String, status: String): Flow<Result<ExternalUserPage>> = flow {
        emit(Result.Loading)

        try {
            val result = repository.getExternalUsers(page, search, status)
            emit(Result.Success(result))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }
}

package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.ActivityLogPage
import java.util.Date

interface ActivityLogRepository {
    // V-06: returns one page of the log, optionally between two dates, or throws the error
    suspend fun getActivityLog(page: Int, from: Date?, to: Date?): ActivityLogPage
}

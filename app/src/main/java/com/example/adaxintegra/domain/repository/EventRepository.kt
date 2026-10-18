package com.example.adaxintegra.domain.repository

import com.example.adaxintegra.domain.model.Event
import com.example.adaxintegra.domain.model.EventScope
import com.example.adaxintegra.domain.model.NewEvent

interface EventRepository {
    // E-01: returns the active events of the scope, sorted by start date
    suspend fun getEvents(scope: EventScope): List<Event>

    suspend fun createEvent(newEvent: NewEvent): Event
}

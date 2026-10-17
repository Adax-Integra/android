package com.example.adaxintegra.domain.entities

// V-12: which cases of the collaborator are shown, by their state
enum class CaseStatusFilter(val apiValue: String, val label: String){
    ALL("all", "Todos"),
    OPEN("open", "En curso"),
    CLOSED("closed", "Finalizados"),
}



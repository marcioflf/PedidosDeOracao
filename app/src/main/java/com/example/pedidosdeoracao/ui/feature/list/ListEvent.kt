package com.example.pedidosdeoracao.ui.feature.list

sealed interface ListEvent {
    data class Delete(val id: Long) : ListEvent
    data class ArchiveChanged(val id: Long, val isArchived: Boolean) : ListEvent
    data class AddEdit(val id: Long?) : ListEvent
    data class Pray(val id: Long) : ListEvent
}
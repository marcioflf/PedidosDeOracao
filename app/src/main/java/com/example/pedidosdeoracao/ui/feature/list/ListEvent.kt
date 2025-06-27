package com.example.pedidosdeoracao.ui.feature.list

sealed interface ListEvent {
    data class ArchiveChanged(val id: Long, val isArchived: Boolean) : ListEvent
    data class AddEdit(val id: Long?) : ListEvent
    data class Pray(val id: Long) : ListEvent
    data object ToggleShowPedidosOradosHoje : ListEvent
    data object ToggleCompactMode : ListEvent
    data class Details(val id: Long) : ListEvent
}
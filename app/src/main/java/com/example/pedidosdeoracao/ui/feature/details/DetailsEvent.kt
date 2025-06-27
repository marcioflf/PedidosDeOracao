package com.example.pedidosdeoracao.ui.feature.details

sealed interface DetailsEvent {
    data object Pray : DetailsEvent
    data object Edit : DetailsEvent
    data object Delete : DetailsEvent
}
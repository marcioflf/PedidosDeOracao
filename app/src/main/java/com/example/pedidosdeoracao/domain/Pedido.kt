package com.example.pedidosdeoracao.domain

import java.time.LocalDate

data class Pedido(
    val id: Long,
    val title: String,
    val description: String?,
    //val creationDate: LocalDate,
    //val lastPrayedDate: LocalDate?,
    val isArchived: Boolean,
)

// Objetos falsos
val pedido1 = Pedido(1, "Pedido 1", "Descrição do pedido 1", false)
val pedido2 = Pedido(2, "Pedido 2", "Descrição do pedido 2", true)
// val pedido3 = Pedido(3, "Pedido 3", "Pedido orado hoje", LocalDate.of(2025, 1, 1), LocalDate.now(), true)
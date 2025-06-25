package com.example.pedidosdeoracao.domain

import java.time.LocalDateTime

data class Pedido(
    val id: Long,
    val title: String,
    val description: String?,
    val creationDate: LocalDateTime,
    val isArchived: Boolean,
)

// Objetos falsos
val pedido1 = Pedido(1, "Pedido 1", "Descrição do pedido 1", LocalDateTime.now(),false)
val pedido2 = Pedido(2, "Pedido 2", "Descrição do pedido 2", LocalDateTime.of(2025, 6, 10, 20, 20), false)
val pedido3 = Pedido(3, "Pedido 3", "Pedido Arquivado", LocalDateTime.of(2025, 1, 1, 23, 20), true)

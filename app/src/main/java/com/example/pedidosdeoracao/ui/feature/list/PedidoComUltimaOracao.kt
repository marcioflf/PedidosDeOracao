package com.example.pedidosdeoracao.ui.feature.list

import com.example.pedidosdeoracao.domain.Pedido
import java.time.LocalDateTime

data class PedidoComUltimaOracao(
    val pedido: Pedido,
    val ultimaOracao: LocalDateTime?
)

//Objetos Falsos
val pedidoComUltimaOracao1 = PedidoComUltimaOracao(
    pedido = Pedido(1, "Pedido 1", "Descrição do pedido 1", LocalDateTime.now(), false),
    ultimaOracao = LocalDateTime.now()
)

val pedidoComUltimaOracao2 = PedidoComUltimaOracao(
    pedido = Pedido(2, "Pedido 2", "Descrição do pedido 2", LocalDateTime.of(2025, 6, 10, 20, 20), false),
    ultimaOracao = null
)

val pedidoComUltimaOracao3 = PedidoComUltimaOracao(
    pedido = Pedido(3, "Pedido 3", null, LocalDateTime.of(2025, 6, 12, 20, 20), false),
    ultimaOracao = LocalDateTime.of(2025, 6, 12, 20, 20)
)
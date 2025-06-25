package com.example.pedidosdeoracao.ui.feature.list

import com.example.pedidosdeoracao.domain.Pedido
import java.time.LocalDateTime

data class PedidoComUltimaOracao(
    val pedido: Pedido,
    val ultimaOracao: LocalDateTime?
)

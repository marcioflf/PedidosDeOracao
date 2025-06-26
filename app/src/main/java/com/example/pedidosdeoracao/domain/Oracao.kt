package com.example.pedidosdeoracao.domain

import java.time.LocalDateTime

data class Oracao(
    val id: Long,
    val pedidoId: Long,
    val dataHora: LocalDateTime
)
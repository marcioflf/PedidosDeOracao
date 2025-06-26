package com.example.pedidosdeoracao.data

import com.example.pedidosdeoracao.domain.Oracao
import kotlinx.coroutines.flow.Flow

interface OracaoRepository {

    suspend fun insert(pedidoId: Long)

    fun listByPedido(pedidoId: Long): Flow<List<Oracao>>

    suspend fun lastPrayByPedido(pedidoId: Long): Oracao?

    fun listarUltimasOracoes(): Flow<List<Oracao>>
}
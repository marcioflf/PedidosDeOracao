package com.example.pedidosdeoracao.data

import kotlinx.coroutines.flow.Flow

interface OracaoRepository {

    suspend fun insert(pedidoId: Long)

    fun listByPedido(pedidoId: Long): Flow<List<OracaoEntity>>

    suspend fun lastPrayByPedido(pedidoId: Long): OracaoEntity?

    fun listarUltimasOracoes(): Flow<List<OracaoEntity>>
}
package com.example.pedidosdeoracao.data

import com.example.pedidosdeoracao.domain.Pedido
import kotlinx.coroutines.flow.Flow

interface PedidoRepository {

    suspend fun insert(title: String, description: String?, id: Long? = null)

    suspend fun updateArchived(id: Long, isArchived: Boolean)

    suspend fun delete(id: Long)

    fun getAll(): Flow<List<Pedido>>

    suspend fun getBy(id: Long): Pedido?
}
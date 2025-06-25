package com.example.pedidosdeoracao.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class OracaoRepositoryImpl(
    private val dao: OracaoDao
) : OracaoRepository {

    override suspend fun insert(pedidoId: Long) {
        val entity = OracaoEntity(
            pedidoId = pedidoId,
            dataHora = LocalDateTime.now()
        )

        dao.insert(entity)
    }

    override fun listByPedido(pedidoId: Long): Flow<List<OracaoEntity>> {
        return dao.listByPedido(pedidoId)
    }

    override suspend fun lastPrayByPedido(pedidoId: Long): OracaoEntity? {
        return dao.lastPrayByPedido(pedidoId)
    }

    override fun listarUltimasOracoes(): Flow<List<OracaoEntity>> {
        return dao.listarUltimasOracoes()
    }

}
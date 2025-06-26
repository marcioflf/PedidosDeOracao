package com.example.pedidosdeoracao.data

import com.example.pedidosdeoracao.domain.Oracao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    override fun listByPedido(pedidoId: Long): Flow<List<Oracao>> {
        return dao.listByPedido(pedidoId).map { entities ->
            entities.map {
                it.toDomain()
            }
        }
    }

    override suspend fun lastPrayByPedido(pedidoId: Long): Oracao? {
        return dao.lastPrayByPedido(pedidoId)?.toDomain()
    }

    override fun listarUltimasOracoes(): Flow<List<Oracao>> {
        return dao.listarUltimasOracoes().map { entities ->
            entities.map {
                it.toDomain()
            }
        }
    }

    private fun OracaoEntity.toDomain(): Oracao {
        return Oracao(
            id = id,
            pedidoId = pedidoId,
            dataHora = dataHora
        )
    }

}
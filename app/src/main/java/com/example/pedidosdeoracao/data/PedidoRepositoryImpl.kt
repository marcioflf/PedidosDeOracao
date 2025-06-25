package com.example.pedidosdeoracao.data

import com.example.pedidosdeoracao.domain.Pedido
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PedidoRepositoryImpl(
    private val dao: PedidoDao
) : PedidoRepository {
        override suspend fun insert(title: String, description: String?, id: Long?) {
            val entity = id?.let {
                dao.getBy(it)?.copy(
                    title = title,
                    description = description
                )
            } ?: PedidoEntity(
                title = title,
                description = description,
                isArchived = false
            )

            id?.let {
                dao.update(entity)
            } ?: run {
                dao.insert(entity)
            }
        }

    override suspend fun updateArchived(id: Long, isArchived: Boolean) {
        val existingEntity = dao.getBy(id) ?: return
        val updatedEntity = existingEntity.copy(isArchived = isArchived)
        dao.insert(updatedEntity)
    }

    override suspend fun delete(id: Long) {
        val existingEntity = dao.getBy(id) ?: return
        dao.delete(existingEntity)
    }

    override fun getAll(): Flow<List<Pedido>> {
        return dao.getAll().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getBy(id: Long): Pedido? {
        return dao.getBy(id)?.let { entity ->
            entity.toDomain()
        }
    }

    private fun PedidoEntity.toDomain(): Pedido {
        return Pedido(
            id = id,
            title = title,
            description = description,
            isArchived = isArchived)
    }
}


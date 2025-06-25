package com.example.pedidosdeoracao.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OracaoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: OracaoEntity)

    @Query("SELECT * FROM oracoes WHERE pedidoId = :pedidoId ORDER BY dataHora")
    fun listByPedido(pedidoId: Long): Flow<List<OracaoEntity>>

    @Query("SELECT * FROM oracoes WHERE pedidoId = :pedidoId ORDER BY dataHora DESC LIMIT 1")
    suspend fun lastPrayByPedido(pedidoId: Long): OracaoEntity?

    @Query("""
    SELECT * FROM oracoes 
    WHERE id IN (
        SELECT MAX(id) FROM oracoes GROUP BY pedidoId
    )
""")
    fun listarUltimasOracoes(): Flow<List<OracaoEntity>>
}
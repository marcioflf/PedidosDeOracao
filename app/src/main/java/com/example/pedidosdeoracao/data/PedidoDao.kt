package com.example.pedidosdeoracao.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: PedidoEntity)

    @Update
    suspend fun update(entity: PedidoEntity)

    @Delete
    suspend fun delete(entity: PedidoEntity)

    @Query("SELECT * FROM pedidos")
    fun getAll(): Flow<List<PedidoEntity>>

    @Query("SELECT * FROM pedidos WHERE id = :id")
    suspend fun getBy(id: Long): PedidoEntity?

}
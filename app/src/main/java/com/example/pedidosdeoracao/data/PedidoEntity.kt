package com.example.pedidosdeoracao.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String?,
    val isArchived: Boolean,
    //val creationDate: Long,
    //val lastPrayedDate: Long?
)

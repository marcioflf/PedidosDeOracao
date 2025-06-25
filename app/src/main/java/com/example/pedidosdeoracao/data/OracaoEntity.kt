package com.example.pedidosdeoracao.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "oracoes",
    foreignKeys = [
        ForeignKey(
            entity = PedidoEntity::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OracaoEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pedidoId: Long,
    val dataHora: LocalDateTime

)
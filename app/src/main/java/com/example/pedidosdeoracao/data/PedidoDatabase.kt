package com.example.pedidosdeoracao.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PedidoEntity::class, OracaoEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class PedidoDatabase: RoomDatabase() {
    abstract val pedidoDao: PedidoDao
    abstract val oracaoDao: OracaoDao
}

object PedidoDatabaseProvider {

    @Volatile
    private var INSTANCE: PedidoDatabase? = null

    fun provide(context: Context): PedidoDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                PedidoDatabase::class.java,
                "pedidos-de-oracao-app"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            instance
        }
    }
}
package com.example.pedidosdeoracao

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pedidosdeoracao.navigation.PedidoNavHost
import com.example.pedidosdeoracao.ui.theme.PedidosDeOracaoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PedidosDeOracaoTheme {
                PedidoNavHost()
            }
        }
    }
}

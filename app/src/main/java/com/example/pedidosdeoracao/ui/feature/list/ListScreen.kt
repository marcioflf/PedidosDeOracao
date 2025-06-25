package com.example.pedidosdeoracao.ui.feature.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pedidosdeoracao.data.OracaoRepository
import com.example.pedidosdeoracao.data.OracaoRepositoryImpl
import com.example.pedidosdeoracao.data.PedidoDatabaseProvider
import com.example.pedidosdeoracao.data.PedidoRepositoryImpl
import com.example.pedidosdeoracao.domain.Pedido
import com.example.pedidosdeoracao.domain.pedido1
import com.example.pedidosdeoracao.domain.pedido2
import com.example.pedidosdeoracao.navigation.AddEditRoute
import com.example.pedidosdeoracao.ui.UiEvent
import com.example.pedidosdeoracao.ui.components.PedidoItem
import com.example.pedidosdeoracao.ui.theme.PedidosDeOracaoTheme

@Composable
fun ListScreen(
    navigateToAddEditScreen: (id: Long?) -> Unit,
) {
    val context = LocalContext.current.applicationContext
    val database = PedidoDatabaseProvider.provide(context)
    val pedidoRepository = PedidoRepositoryImpl(dao = database.pedidoDao)
    val oracaoRepository = OracaoRepositoryImpl(dao = database.oracaoDao)

    val viewModel = viewModel<ListViewModel> {
        ListViewModel(
            pedidoRepository = pedidoRepository,
            oracaoRepository = oracaoRepository
        )
    }

    val pedidos = viewModel.pedidosComUltimaOracao.collectAsState()
    val showPedidosOradosHoje = viewModel.mostrarPedidosOradosHoje.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {}
                UiEvent.NavigateBack -> {}

                is UiEvent.Navigate<*> -> {
                    when(uiEvent.route) {
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    ListContent(
        pedidos = pedidos.value,
        showPedidosOradosHoje = showPedidosOradosHoje.value,
        onEvent = viewModel::onEvent
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListContent(
    pedidos: List<PedidoComUltimaOracao>,
    showPedidosOradosHoje: Boolean,
    onEvent: (event: ListEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ListEvent.AddEdit(null))
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding()) // Topo do Scaffold
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp),
                text = "Pedidos de Oração",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextButton(onClick = {
                    onEvent(ListEvent.ToggleShowPedidosOradosHoje)
                }) {
                    Text(text = if (showPedidosOradosHoje) "Esconder pedidos orados hoje" else "Mostrar pedidos orados hoje")
                }

                Switch(
                    checked = showPedidosOradosHoje,
                    onCheckedChange = {
                        onEvent(ListEvent.ToggleShowPedidosOradosHoje)
                    }
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                itemsIndexed(pedidos) { index, item ->
                    PedidoItem(
                        pedido = item.pedido,
                        ultimaOracao = item.ultimaOracao,
                        onPrayClick = {
                            onEvent(ListEvent.Pray(item.pedido.id))
                        },
                        onItemClick = {
                            onEvent(ListEvent.AddEdit(item.pedido.id))
                        },
                        onDeleteClick = {
                            onEvent(ListEvent.Delete(item.pedido.id))
                        }
                    )

                    if (index < pedidos.lastIndex) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
          }
        }
    }
}

/*
@Preview
@Composable
private fun ListContentPreview() {
    PedidosDeOracaoTheme {
        ListContent(
            pedidos = listOf(
                pedido1,
                pedido2
            ),
            onEvent = {}
        )
    }
}
*/
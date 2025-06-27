package com.example.pedidosdeoracao.ui.feature.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pedidosdeoracao.R
import com.example.pedidosdeoracao.data.OracaoRepositoryImpl
import com.example.pedidosdeoracao.data.PedidoDatabaseProvider
import com.example.pedidosdeoracao.data.PedidoRepositoryImpl
import com.example.pedidosdeoracao.domain.Oracao
import com.example.pedidosdeoracao.domain.Pedido
import com.example.pedidosdeoracao.domain.pedido1
import com.example.pedidosdeoracao.navigation.AddEditRoute
import com.example.pedidosdeoracao.ui.UiEvent
import com.example.pedidosdeoracao.ui.theme.PedidosDeOracaoTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DetailsScreen(
    id: Long,
    snackbarHostState: SnackbarHostState,
    navigateBack: () -> Unit,
    navigateToAddEditScreen: (id: Long?) -> Unit,
    navigateBackWithMessage: (message: String) -> Unit
) {
    val context = LocalContext.current.applicationContext
    val database = PedidoDatabaseProvider.provide(context)
    val pedidoRepository = PedidoRepositoryImpl(dao = database.pedidoDao)
    val oracaoRepository = OracaoRepositoryImpl(dao = database.oracaoDao)

    val viewModel = viewModel<DetailsViewModel> {
        DetailsViewModel(
            pedidoId = id,
            pedidoRepository = pedidoRepository,
            oracaoRepository = oracaoRepository
        )
    }

    val pedidoState = viewModel.pedido.collectAsState()
    val oracoes = viewModel.oracoes.collectAsState()

    val pedido = pedidoState.value

    if (pedido == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Carregando pedido...", style = MaterialTheme.typography.bodyMedium)
        }
        return
    }

    val oracoesOrdenadas = oracoes.value.sortedByDescending { it.dataHora }

    val showDeleteDialog = viewModel.showDeleteDialog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshPedido()
        viewModel.uiEvent.collect { uiEvent ->
            when (uiEvent) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(message = uiEvent.message)
                }
                is UiEvent.NavigateBack -> {
                    navigateBack()
                }
                is UiEvent.Navigate<*> -> {
                    when(uiEvent.route) {
                        is AddEditRoute -> {
                            navigateToAddEditScreen(uiEvent.route.id)
                        }
                        else -> {}
                    }
                }
                is UiEvent.NavigateBackWithMessage -> {
                    navigateBackWithMessage(uiEvent.message)
                }
            }
        }
    }

    DetailsContent(
        pedido = pedido,
        oracoes = oracoesOrdenadas,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent
    )

    // AlertDialog de confirmação de exclusão
    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(DetailsEvent.CancelDelete) },
            title = { Text("Excluir pedido?") },
            text = { Text("Tem certeza que deseja excluir este pedido? Essa ação não pode ser desfeita.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(DetailsEvent.ConfirmDelete) }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(DetailsEvent.CancelDelete) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DetailsContent(
    pedido: Pedido,
    oracoes: List<Oracao>,
    snackbarHostState: SnackbarHostState,
    onEvent: (event: DetailsEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            Column {
                FloatingActionButton(onClick = {
                    onEvent(DetailsEvent.Pray)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pray),
                        contentDescription = "Pray",
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = {
                    onEvent(DetailsEvent.Edit)
                }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                Spacer(modifier = Modifier.height(16.dp))
                FloatingActionButton(onClick = {
                    onEvent(DetailsEvent.Delete)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            item {
                Text(
                    text = pedido.title,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Data de criação: ${pedido.creationDate.format(dateFormatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                if (!pedido.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = pedido.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Histórico de Orações:",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (oracoes.isEmpty()) {
                item {
                    Text(
                        text = "Nenhuma oração encontrada.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                items(oracoes) { oracao ->
                    Text(
                        text = oracao.dataHora.format(dateFormatter),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

@Preview
@Composable
private fun DetailsPreview() {
    PedidosDeOracaoTheme {
        DetailsContent(
            pedido = pedido1,
            oracoes = listOf(
                Oracao(
                    id = 1,
                    pedidoId = 1,
                    dataHora = LocalDateTime.now()
                ),
                Oracao(
                    id = 2,
                    pedidoId = 1,
                    dataHora = LocalDateTime.of(2023, 10, 10, 10, 10)
                ),
                Oracao(
                    id = 3,
                    pedidoId = 1,
                    dataHora = LocalDateTime.now()
                )
            ),
            snackbarHostState = SnackbarHostState(),
            onEvent = {}
        )
    }

}
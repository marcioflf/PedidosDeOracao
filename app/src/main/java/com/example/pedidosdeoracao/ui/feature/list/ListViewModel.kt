package com.example.pedidosdeoracao.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedidosdeoracao.data.OracaoRepository
import com.example.pedidosdeoracao.data.PedidoRepository
import com.example.pedidosdeoracao.navigation.AddEditRoute
import com.example.pedidosdeoracao.navigation.DetailsRoute
import com.example.pedidosdeoracao.ui.UiEvent
import com.example.pedidosdeoracao.ui.UiEvent.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class ListViewModel(
    private val pedidoRepository: PedidoRepository,
    private val oracaoRepository: OracaoRepository
) : ViewModel() {

    private val _mostrarPedidosOradosHoje = MutableStateFlow(false)
    val mostrarPedidosOradosHoje: StateFlow<Boolean> = _mostrarPedidosOradosHoje

    fun toggleShowPedidosOradosHoje() {
        _mostrarPedidosOradosHoje.update { !it }
    }

    private val _compactMode = MutableStateFlow(false)
    val compactMode: StateFlow<Boolean> = _compactMode

    fun toggleCompactMode() {
        _compactMode.update { !it }
    }

    val pedidosComUltimaOracao = combine(
        pedidoRepository.getAll(),
        oracaoRepository.listarUltimasOracoes(),
        mostrarPedidosOradosHoje
    ) { pedidos, oracoes, mostrarPedidosOradosHoje ->

        val oracoesMap = oracoes.associateBy { it.pedidoId }

        pedidos.map { pedido ->
            val ultima = oracoesMap[pedido.id]?.dataHora
            PedidoComUltimaOracao(pedido, ultima)
        }
            .sortedWith(compareBy(nullsFirst()) {it.ultimaOracao})
            .filter {
            if (mostrarPedidosOradosHoje) true
            else it.ultimaOracao?.toLocalDate() != LocalDate.now()
        }

    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
    )


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ListEvent) {
        when (event) {
            is ListEvent.Delete -> {
                delete(event.id)
            }
            is ListEvent.ArchiveChanged -> {
                archiveChanged(event.id, event.isArchived)
            }
            is ListEvent.AddEdit -> {
                viewModelScope.launch {
                    _uiEvent.send(Navigate(AddEditRoute(event.id)))
                }
            }
            is ListEvent.Pray -> {
                pray(event.id)
            }

            is ListEvent.ToggleShowPedidosOradosHoje -> {
                toggleShowPedidosOradosHoje()
            }

            is ListEvent.Details -> {
                viewModelScope.launch {
                    _uiEvent.send(Navigate(DetailsRoute(event.id)))
                }
            }

            is ListEvent.ToggleCompactMode -> {
                toggleCompactMode()
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            pedidoRepository.delete(id)
        }

    }

    private fun archiveChanged(id: Long, isArchived: Boolean) {
        viewModelScope.launch {
            pedidoRepository.updateArchived(id, isArchived)
        }
    }

    private fun pray(id: Long) {
        viewModelScope.launch {
            oracaoRepository.insert(id)
            _uiEvent.send(ShowSnackbar("Oração registrada com sucesso"))
        }
    }
}
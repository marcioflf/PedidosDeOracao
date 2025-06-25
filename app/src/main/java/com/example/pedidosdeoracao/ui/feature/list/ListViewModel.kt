package com.example.pedidosdeoracao.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedidosdeoracao.data.OracaoRepository
import com.example.pedidosdeoracao.data.PedidoRepository
import com.example.pedidosdeoracao.navigation.AddEditRoute
import com.example.pedidosdeoracao.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListViewModel(
    private val pedidoRepository: PedidoRepository,
    private val oracaoRepository: OracaoRepository
) : ViewModel() {
    /*
    val pedidos = pedidoRepository.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    val pedidosComUltimaOracao = pedidos.map { pedido ->
        val ultimaOracao = oracaoRepository.lastPrayByPedido(pedido.id)
        PedidoComUltimaOracao(pedido, ultimaOracao?.dataHora)
    }
    */

    val pedidosComUltimaOracao = combine(
        pedidoRepository.getAll(),
        oracaoRepository.listarUltimasOracoes()
    ) { pedidos, oracoes ->

        val oracoesMap = oracoes.associateBy { it.pedidoId }

        pedidos.map { pedido ->
            val ultima = oracoesMap[pedido.id]?.dataHora
            PedidoComUltimaOracao(pedido, ultima)
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
                    _uiEvent.send(UiEvent.Navigate(AddEditRoute(event.id)))
                }
            }
            is ListEvent.Pray -> {
                pray(event.id)
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
        }

    }
}
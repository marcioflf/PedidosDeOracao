package com.example.pedidosdeoracao.ui.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedidosdeoracao.data.OracaoRepository
import com.example.pedidosdeoracao.data.PedidoRepository
import com.example.pedidosdeoracao.domain.Oracao
import com.example.pedidosdeoracao.domain.Pedido
import com.example.pedidosdeoracao.navigation.AddEditRoute
import com.example.pedidosdeoracao.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val pedidoId: Long,
    private val pedidoRepository: PedidoRepository,
    private val oracaoRepository: OracaoRepository
) : ViewModel() {

    private val _pedido = MutableStateFlow<Pedido?>(null)
    val pedido: StateFlow<Pedido?> = _pedido.asStateFlow()

    val oracoes: StateFlow<List<Oracao>> =
        oracaoRepository.listByPedido(pedidoId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _pedido.value = pedidoRepository.getBy(pedidoId)
        }
    }

    fun onEvent(event: DetailsEvent)  {
        when(event) {
            DetailsEvent.Pray -> pray()
            DetailsEvent.Edit -> edit()
            DetailsEvent.Delete -> _showDeleteDialog.value = true
            DetailsEvent.ConfirmDelete -> delete()
            DetailsEvent.CancelDelete -> _showDeleteDialog.value = false
        }
    }

    private fun pray() {
        viewModelScope.launch {
            oracaoRepository.insert(pedidoId)
            _uiEvent.send(UiEvent.ShowSnackbar("Oração registrada com sucesso"))
        }
    }

    private fun edit() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.Navigate(AddEditRoute(pedidoId)))
        }
    }

    private fun delete() {
        viewModelScope.launch {
            pedidoRepository.delete(pedidoId)
            _uiEvent.send(UiEvent.NavigateBackWithMessage("Pedido excluído com sucesso"))
        }
    }

    fun refreshPedido() {
        viewModelScope.launch {
            _pedido.value = pedidoRepository.getBy(pedidoId)
        }
    }
}
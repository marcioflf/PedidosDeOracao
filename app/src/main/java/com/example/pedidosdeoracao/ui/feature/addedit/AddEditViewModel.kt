package com.example.pedidosdeoracao.ui.feature.addedit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pedidosdeoracao.data.PedidoRepository
import com.example.pedidosdeoracao.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditViewModel(
    private val id: Long? = null,
    private val repository: PedidoRepository,
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf<String?>(null)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        id?.let {
            viewModelScope.launch {
                val pedido = repository.getBy(it)
                title = pedido?.title ?: ""
                description = pedido?.description
            }
        }
    }

    fun onEvent(event: AddEditEvent) {
        when(event) {
            is AddEditEvent.TitleChanged -> {
                title = event.title
            }
            is AddEditEvent.DescriptionChanged -> {
                description = event.description
            }
            is AddEditEvent.Save -> {
                savePedido()
            }
        }
    }

    private fun savePedido() {
        viewModelScope.launch {
            if (title.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("O título não pode estar em branco"))
                return@launch
            }

            repository.insert(title, description, id)
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }
}
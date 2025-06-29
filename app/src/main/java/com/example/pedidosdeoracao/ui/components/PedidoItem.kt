package com.example.pedidosdeoracao.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pedidosdeoracao.R
import com.example.pedidosdeoracao.domain.Pedido
import com.example.pedidosdeoracao.domain.pedido2
import com.example.pedidosdeoracao.ui.theme.PedidosDeOracaoTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PedidoItem(
    modifier: Modifier = Modifier,
    pedido: Pedido,
    ultimaOracao: LocalDateTime?,
    compactMode: Boolean = false,
    onPrayClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Surface(
        onClick = onItemClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .padding(if(!compactMode) 16.dp else 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrayClick) {
                Icon(
                    painter = painterResource(id = R.drawable.pray),
                    contentDescription = "Pray"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = pedido.title,
                    style = if(!compactMode) MaterialTheme.typography.titleLarge
                    else MaterialTheme.typography.titleMedium
                )

                ultimaOracao?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Última oração: ${it.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if(!compactMode) {
                    pedido.description?.let {
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = pedido.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

@Preview
@Composable
private fun PedidoItemPreview() {
    PedidosDeOracaoTheme {
        PedidoItem(pedido = Pedido(
            id = 1,
            title = "Pedido 1",
            description = "Descrição do pedidooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo 1",
            creationDate = LocalDateTime.now(),
            isArchived = false
        ),
            ultimaOracao = LocalDateTime.now(),
            onPrayClick = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun PedidoItemNeverPrayedPreview() {
    PedidosDeOracaoTheme {
        PedidoItem(pedido = pedido2,
            ultimaOracao = null,
            onPrayClick = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun PedidoItemOnlyTitlePreview() {
    PedidosDeOracaoTheme {
        PedidoItem(pedido = Pedido(
            id = 1,
            title = "Pedido 1",
            description = null,
            creationDate = LocalDateTime.now(),
            isArchived = false
        ),
            ultimaOracao = null,
            onPrayClick = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun PedidoItemCompactModePreview() {
    PedidosDeOracaoTheme {
        PedidoItem(pedido = pedido2,
            ultimaOracao = LocalDateTime.now(),
            compactMode = true,
            onPrayClick = {},
            onItemClick = {}
        )
    }
}
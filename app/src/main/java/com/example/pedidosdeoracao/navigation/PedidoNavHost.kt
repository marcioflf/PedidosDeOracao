package com.example.pedidosdeoracao.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pedidosdeoracao.ui.feature.addedit.AddEditScreen
import com.example.pedidosdeoracao.ui.feature.details.DetailsScreen
import com.example.pedidosdeoracao.ui.feature.list.ListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListRoute

@Serializable
data class AddEditRoute(val id: Long? = null)

@Serializable
data class DetailsRoute(val id: Long)

@Composable
fun PedidoNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ListRoute) {
        composable<ListRoute> {
            ListScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                },
                navigateToDetailsScreen = { id ->
                    navController.navigate(DetailsRoute(id = id))
                }
            )
        }

        composable<AddEditRoute> {
            val addEditRoute = it.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<DetailsRoute> {
            val detailsRoute = it.toRoute<DetailsRoute>()
            DetailsScreen(
                id = detailsRoute.id,
                navigateBack = {
                    navController.popBackStack()
                },
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                }
            )
        }
    }
}
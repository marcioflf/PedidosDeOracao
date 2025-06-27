package com.example.pedidosdeoracao.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.pedidosdeoracao.ui.feature.addedit.AddEditScreen
import com.example.pedidosdeoracao.ui.feature.details.DetailsScreen
import com.example.pedidosdeoracao.ui.feature.list.ListScreen
import kotlinx.coroutines.launch
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

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    NavHost(navController = navController, startDestination = ListRoute) {
        composable<ListRoute> {
            ListScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditRoute(id = id))
                },
                navigateToDetailsScreen = { id ->
                    navController.navigate(DetailsRoute(id = id))
                },
                snackbarHostState = snackbarHostState
            )
        }

        composable<AddEditRoute> {
            val addEditRoute = it.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                navigateBack = {
                    navController.popBackStack()
                },
                navigateBackWithMessage = { message ->
                    navController.popBackStack()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
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
                },
                navigateBackWithMessage = { message ->
                    navController.popBackStack()
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
                },
                snackbarHostState = snackbarHostState
            )
        }
    }
}
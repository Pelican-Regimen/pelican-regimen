/*
 * Copyright 2020 Pelican Regimen
 *
 * This source file is a part of Pelican Regimen <https://github.com/PelicanRegimen/pelican-regimen>
 *
 * Pelican Regimen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pelican Regimen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pelican Regimen.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.pelicanregimen.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.pelicanregimen.ui.components.PelicanRegimenScaffold
import com.pelicanregimen.ui.components.PelicanRegimenSnackbar
import com.pelicanregimen.ui.home.HomeSections
import com.pelicanregimen.ui.home.PelicanRegimenBottomBar
import com.pelicanregimen.ui.home.addHomeGraph
import com.pelicanregimen.ui.snackdetail.SnackDetail
import com.pelicanregimen.ui.theme.PelicanRegimenTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.pelicanregimen.ui.MainDestinations
import com.pelicanregimen.ui.rememberAppStateHolder

@Composable
fun PelicanRegimenApp() {
    ProvideWindowInsets {
        PelicanRegimenTheme {
            val appStateHolder = rememberAppStateHolder()
            PelicanRegimenScaffold(
                bottomBar = {
                    if (appStateHolder.shouldShowBottomBar) {
                        PelicanRegimenBottomBar(
                            tabs = appStateHolder.bottomBarTabs,
                            currentRoute = appStateHolder.currentRoute!!,
                            navigateToRoute = appStateHolder::navigateToBottomBarRoute
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.systemBarsPadding(),
                        snackbar = { snackbarData -> PelicanRegimenSnackbar(snackbarData) }
                    )
                },
                scaffoldState = appStateHolder.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appStateHolder.navController,
                    startDestination = MainDestinations.HOME_ROUTE,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    pelicanRegimenNavGraph(
                        onSnackSelected = appStateHolder::navigateToSnackDetail,
                        upPress = appStateHolder::upPress
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.pelicanRegimenNavGraph(
    onSnackSelected: (Long, NavBackStackEntry) -> Unit,
    upPress: () -> Unit
) {
    navigation(
        route = MainDestinations.HOME_ROUTE,
        startDestination = HomeSections.FEED.route
    ) {
        addHomeGraph(onSnackSelected)
    }
    composable(
        "${MainDestinations.SNACK_DETAIL_ROUTE}/{${MainDestinations.SNACK_ID_KEY}}",
        arguments = listOf(navArgument(MainDestinations.SNACK_ID_KEY) { type = NavType.LongType })
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        val snackId = arguments.getLong(MainDestinations.SNACK_ID_KEY)
        SnackDetail(snackId, upPress)
    }
}

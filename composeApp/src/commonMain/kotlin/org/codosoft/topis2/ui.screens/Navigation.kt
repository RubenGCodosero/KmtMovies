package org.codosoft.topis2.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kmpmovies.composeapp.generated.resources.Res
import kmpmovies.composeapp.generated.resources.api_key
import kotlinx.serialization.json.Json
import org.codosoft.topis2.data.MovieService
import org.codosoft.topis2.data.MoviesRepository
import org.codosoft.topis2.ui.screens.detail.DetailScreen
import org.codosoft.topis2.ui.screens.detail.DetailViewModel
import org.codosoft.topis2.ui.screens.home.HomeScreen
import org.codosoft.topis2.ui.screens.home.HomeViewModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val repository = rememberMoviesRepository()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen( onMovieClick = {
                movie -> navController.navigate("detail/${movie.id}")
            },
                vm = viewModel { HomeViewModel(repository) })
        }

        composable(
            route ="detail/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) {
            backStackEntry ->
            val movieId = checkNotNull(backStackEntry.arguments?.getInt("movieId"))
            DetailScreen(
               vm = viewModel { DetailViewModel(movieId, repository) },
                onBack = { navController.popBackStack() })
        }
    }
}

@Composable
private fun rememberMoviesRepository(
    apiKey: String = stringResource(Res.string.api_key)
): MoviesRepository = remember {
    val client =
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.themoviedb.org"
                    parameters.append("api_key", apiKey)
                }
            }
        }
     MoviesRepository(MovieService(client))
}


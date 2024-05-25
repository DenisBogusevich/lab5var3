package org.kp5_7.project


import Book
import BookDetailScreen
import BookDetailsViewModel
import HistoryScreen
import OpenLibraryApiService
import SearchScreen
import SearchViewModel
import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.activity.viewModels
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class MainActivity : ComponentActivity() {
    private val dataRepository = DataRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val router = remember { NavigationController() }
            val apiService = OpenLibraryApi.service  // Obtain the API service instance
            val factory = remember { ViewModelFactory( application,router, apiService,dataRepository) }
            // Pass the API service to the factory


            when (val screen = router.currentScreen.value) {
                is Screen.Search -> {
                    val viewModel: SearchViewModel by viewModels { factory }


                    SearchScreen(viewModel)
                }

                is Screen.Detail -> {
                    val viewModel: BookDetailsViewModel by viewModels { factory }
                    BookDetailScreen(viewModel)
                }

                Screen.History -> {
                    val viewModel: SearchViewModel by viewModels { factory }

                    HistoryScreen(viewModel = viewModel)
                }
            }
        }
    }
}


class ViewModelFactory(
    private val application: Application,
    private val router: NavigationController,
    private val apiService: OpenLibraryApiService,
    private val dataRepository: DataRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(
                application,
                dataRepository,
                router,
                apiService
            ) as T

            modelClass.isAssignableFrom(BookDetailsViewModel::class.java) -> {
                val book = (router.currentScreen.value as? Screen.Detail)?.book
                    ?: throw IllegalStateException("Book  is required")
                println("BOOK "+ book)
                BookDetailsViewModel(dataRepository,router) as T

            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}


class NavigationController {
    private val _currentScreen = mutableStateOf<Screen>(Screen.Search)
    val currentScreen: State<Screen> = _currentScreen

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }
}

sealed class Screen {
    object Search : Screen()

    object History : Screen()

    data class Detail(val book: Book) :
        Screen() // Ensure you pass the book ID, not the book object.


}
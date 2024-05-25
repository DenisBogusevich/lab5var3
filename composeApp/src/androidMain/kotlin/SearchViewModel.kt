import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.kp5_7.project.BookDatabaseHelper
import org.kp5_7.project.DataRepository
import org.kp5_7.project.NavigationController
import org.kp5_7.project.Screen

class SearchViewModel(
    application: Application,
    private val dataRepository: DataRepository,
    private val router: NavigationController,
    private val apiService: OpenLibraryApiService
) : AndroidViewModel(application) {

    private val bookDatabaseHelper = BookDatabaseHelper(application)

    private fun updateData(data: Book) {
        dataRepository.updateData(data)
    }


    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults

    private val _history = MutableStateFlow<List<Book>>(emptyList())
    val history: StateFlow<List<Book>> get() = _history

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _history.value = bookDatabaseHelper.getAllBooks()
        }
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.searchBooks(query).execute()
            if (response.isSuccessful && response.body() != null) {
                _searchResults.value = response.body()!!.docs.map { doc ->
                    Book(
                        id = doc.key.replace("/works/", ""), // Clean up the key to get a usable ID
                        title = doc.title,
                        authors = doc.authorName.toString(), // Handle null author names
                        year = doc.firstPublishYear?.toString(), // Convert year to string
                        coverUrl = generateCoverUrl(doc.coverI) // Generate URL for cover image
                    )
                }
            } else {
                Log.e("API Error", "Failed to fetch search results: ${response.message()}")
            }
        }
    }

    private fun generateCoverUrl(coverId: Int?): String? {
        return coverId?.let { "https://covers.openlibrary.org/b/id/$it-L.jpg" }
    }

    fun selectBook(book: Book) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookDatabaseHelper.insertBook(book)
                _history.value = bookDatabaseHelper.getAllBooks()
            } catch (e: Exception) {
                // Handle error
            }
        }
        updateData(book)
        router.navigateTo(Screen.Detail(book))
    }

    fun showHistory() {
        router.navigateTo(Screen.History)
    }

    fun selectBookFromHistory(book: Book) {
        updateData(book)
        router.navigateTo(Screen.Detail(book))
    }

    fun clearAllBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            bookDatabaseHelper.clearAllBook()
            _history.value = bookDatabaseHelper.getAllBooks()
        }
    }
}
import android.util.Log
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
import org.kp5_7.project.NavigationController
import org.kp5_7.project.Screen

class SearchViewModel(private val router: NavigationController, private val apiService: OpenLibraryApiService) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Book>>(emptyList())
    val searchResults: StateFlow<List<Book>> = _searchResults

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.searchBooks(query).execute()
            if (response.isSuccessful && response.body() != null) {
                _searchResults.value = response.body()!!.docs.map { doc ->
                    Book(
                        id = doc.key.replace("/works/", ""), // Clean up the key to get a usable ID
                        title = doc.title,
                        authors = doc.authorName ?: emptyList(), // Handle null author names
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
        router.navigateTo(Screen.Detail(book))
    }
}
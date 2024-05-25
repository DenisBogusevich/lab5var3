import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.kp5_7.project.DataRepository
import org.kp5_7.project.NavigationController
import org.kp5_7.project.Screen

class BookDetailsViewModel(
    private val dataRepository: DataRepository,
    private val router: NavigationController
) : ViewModel() {
    val book: LiveData<Book> get() = dataRepository.data

   // private val _selectedBook = MutableStateFlow<Book?>(book)
    //val selectedBook: StateFlow<Book?> = _selectedBook
    init {

        loadBookDetails(book.value?.id.toString())
        println(book)

    }

    private fun loadBookDetails(bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Assuming book details are already loaded and you have a cover ID
                val coverResponse = OpenLibraryApi.service.getBookCover(bookId).execute()
                println(coverResponse)
                if (coverResponse.isSuccessful) {
                    // Assuming you have a way to update your Book object with the cover URL
                    book.value?.coverUrl = coverResponse.body()?.string()
                }
            } catch (e: Exception) {
                println(e)
                // Handle errors appropriately
            }
        }
    }


    fun goBack() {
        router.navigateTo(Screen.Search)
    }
}
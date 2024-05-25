import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(viewModel: SearchViewModel) {
    val searchResults by viewModel.searchResults.collectAsState(emptyList())
    val searchTextState = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Search Books") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = searchTextState.value,
                onValueChange = { searchTextState.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search for books...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.searchBooks(searchTextState.value)
                    keyboardController?.hide()
                }),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
            )
            SearchResultsList(searchResults, viewModel)
        }
    }
}

@Composable
fun SearchResultsList(results: List<Book>, viewModel: SearchViewModel) {
    Column {
        results.forEach { book ->
            SearchResultItem(book, viewModel)
        }
    }
}

@Composable
fun SearchResultItem(book: Book, viewModel: SearchViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { viewModel.selectBook(book) },
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(book.title, style = MaterialTheme.typography.h6)
            // Check if authors is not null and not empty before joining into a string
            book.authors?.let { authors ->
                if (authors.isNotEmpty()) {
                    Text(authors.joinToString(separator = ", "), style = MaterialTheme.typography.body2)
                }
            }
            book.year?.let { year ->
                Text("Published in $year", style = MaterialTheme.typography.caption)
            }
        }
    }
}
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

import coil.compose.rememberImagePainter
import com.google.accompanist.coil.rememberCoilPainter
import org.kp5_7.project.R


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun BookDetailScreen(viewModel: BookDetailsViewModel) {
    val book by viewModel.book.observeAsState()



  //  book?.let { bookDetails ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(book?.title ?: "No title available") }, // Handle nullable title
                    navigationIcon = {
                        IconButton(onClick = { viewModel.goBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Display the book cover if available
                book?.coverUrl?.let { url ->
                    Image(
                        painter = rememberAsyncImagePainter(model = url),
                        contentDescription = "Book Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                // Display the book title, or a default message if it is null
                Text(book?.title ?: "No title available", style = MaterialTheme.typography.h6)
                // Join authors into a string, or display "Author unknown" if the list is null or empty
                Text(
                    book?.authors  ?: "Author unknown",
                    style = MaterialTheme.typography.body1
                )
            }
        }
   // }
}
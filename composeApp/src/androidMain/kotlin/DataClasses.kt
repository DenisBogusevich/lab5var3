


data class Book(
    val id: String,
    val title: String,
    val authors: List<String>?,  // Make sure to handle nullable if the API might not provide authors
    val year: String?,    // This can also be nullable if year isn't always provided
    var coverUrl: String? // This should also be nullable
)


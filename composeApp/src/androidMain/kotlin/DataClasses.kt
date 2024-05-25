import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "joke")
data class Book(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val title: String,
    val authors: String?,  // Make sure to handle nullable if the API might not provide authors
    val year: String?,    // This can also be nullable if year isn't always provided
    var coverUrl: String? // This should also be nullable
)


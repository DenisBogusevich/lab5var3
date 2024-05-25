import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName
import retrofit2.http.Path

data class BookSearchResponse(
    @SerializedName("numFound") val numFound: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("docs") val docs: List<BookDoc>
)

data class BookDoc(
    @SerializedName("key") val key: String,
    @SerializedName("title") val title: String,
    @SerializedName("author_name") val authorName: List<String>?,
    @SerializedName("first_publish_year") val firstPublishYear: Int?,
    @SerializedName("cover_i") val coverI: Int?
)

interface OpenLibraryApiService {
    @GET("search.json")
    fun searchBooks(@Query("q") query: String): Call<BookSearchResponse>


    @GET("https://covers.openlibrary.org/b/id/{coverId}-L.jpg")
    fun getBookCover(@Path("coverId") coverId: String): Call<ResponseBody>
}

// Singleton Retrofit client
object OpenLibraryApi {
    private const val BASE_URL = "https://openlibrary.org/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: OpenLibraryApiService = retrofit.create(OpenLibraryApiService::class.java)
}


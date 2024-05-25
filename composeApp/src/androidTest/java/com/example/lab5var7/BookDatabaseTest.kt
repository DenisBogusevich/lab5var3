package com.example.lab5var7

import Book
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kp5_7.project.BookDatabaseHelper

@RunWith(AndroidJUnit4::class)
class BookDatabaseTest {
    private lateinit var dbHelper: BookDatabaseHelper

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = BookDatabaseHelper(context)
    }

    @After
    fun tearDown() {
        dbHelper.clearAllBook()
    }

    @Test
    fun insertBook_success() {
        val book = Book(
            id = "1",
            title = "title",
            authors = "authors",
            year = "year",
            coverUrl = "coverUrl"
        )



        dbHelper.insertBook(book)
        val books = dbHelper.getAllBooks()



        assertEquals(book.authors, books[0].authors)
        assertEquals(book.title, books[0].title)
        assertEquals(book.year, books[0].year)
        assertEquals(book.coverUrl, books[0].coverUrl)

    }

    @Test(expected = Exception::class)
    fun insertBook_fail() {
        val book = Book(
            id = "1",
            title = "title",
            authors = "authors",
            year = "year",
            coverUrl = "coverUrl"
        )


        dbHelper.insertBook(book)
        dbHelper.insertBook(book)


    }


     @Test
     fun clearAllBooks_success() {
         val book = Book(
             id = "1",
             title = "title",
             authors = "authors",
             year = "year",
             coverUrl = "coverUrl"
         )
         val book1 = Book(
             id = "2",
             title = "title2",
             authors = "authors2",
             year = "year2",
             coverUrl = "coverUrl2"
         )
         dbHelper.insertBook(book)
         dbHelper.insertBook(book1)
         dbHelper.clearAllBook()
         val books = dbHelper.getAllBooks()
         Assert.assertTrue(books.isEmpty())
     }

     @Test
     fun clearAllBooks_noBooksToClear() {
         val bookBeforeClear = dbHelper.getAllBooks()
         dbHelper.clearAllBook()
         val booksAfterClear = dbHelper.getAllBooks()
         assertEquals(bookBeforeClear, booksAfterClear)
     }

    @Test
    fun testGetAllBooks_emptyDatabase() {
        // Ensure the database is empty
        dbHelper.clearAllBook()
        val books = dbHelper.getAllBooks()
        assertTrue(books.isEmpty())
    }

    @Test
    fun testGetAllBooks_singleBook() {
        // Insert a single book into the database
        val book = Book("1", "Test Book", "Test Author", "2021", "testCoverUrl")
        dbHelper.insertBook(book)
        val books = dbHelper.getAllBooks()

        assertEquals(1, books.size)
        assertEquals(book.title, books[0].title)
        assertEquals(book.authors, books[0].authors)
        assertEquals(book.year, books[0].year)
        assertEquals(book.coverUrl, books[0].coverUrl)
    }

    @Test
    fun testGetAllBooks_multipleBooks() {
        // Insert multiple books into the database
        val book1 = Book("1", "Test Book 1", "Test Author 1", "2021", "testCoverUrl1")
        val book2 = Book("2", "Test Book 2", "Test Author 2", "2022", "testCoverUrl2")
        dbHelper.insertBook(book1)
        dbHelper.insertBook(book2)

        val books = dbHelper.getAllBooks()

        assertEquals(2, books.size)

        val retrievedBook1 = books.find { it.id == book1.id }
        val retrievedBook2 = books.find { it.id == book2.id }

        assertNotNull(retrievedBook1)
        assertNotNull(retrievedBook2)

        assertEquals(book1.title, retrievedBook1?.title)
        assertEquals(book1.authors, retrievedBook1?.authors)
        assertEquals(book1.year, retrievedBook1?.year)
        assertEquals(book1.coverUrl, retrievedBook1?.coverUrl)

        assertEquals(book2.title, retrievedBook2?.title)
        assertEquals(book2.authors, retrievedBook2?.authors)
        assertEquals(book2.year, retrievedBook2?.year)
        assertEquals(book2.coverUrl, retrievedBook2?.coverUrl)
    }
}
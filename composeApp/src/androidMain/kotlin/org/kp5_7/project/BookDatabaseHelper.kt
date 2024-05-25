package org.kp5_7.project

import Book
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "books.db"
private const val DATABASE_VERSION = 1
private const val TABLE_BOOKS = "books"
private const val COLUMN_ID = "id"
private const val COLUMN_TITLE = "title"
private const val COLUMN_AUTHORS = "authors"
private const val COLUMN_YEAR = "year"
private const val COLUMN_COVERURL = "coverUrl"

class BookDatabaseHelper  (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT,
                $COLUMN_AUTHORS TEXT,
                $COLUMN_YEAR TEXT,
                $COLUMN_COVERURL TEXT 
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }
    fun insertBook(book: Book) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, book.title)
            put(COLUMN_AUTHORS, book.authors)
            put(COLUMN_YEAR, book.year)
            put(COLUMN_COVERURL, book.coverUrl)

        }
        db.insert(TABLE_BOOKS, null, values)
    }
    /**
     * Retrieves all jokes from the database.
     * @return A list of all jokes.
     */
    fun getAllBooks(): List<Book> {
        val db = readableDatabase
        val books = mutableListOf<Book>()
        val cursor = db.query(TABLE_BOOKS, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val title = getString(getColumnIndexOrThrow(COLUMN_TITLE))
                val authors = getString(getColumnIndexOrThrow(COLUMN_AUTHORS))
                val year = getString(getColumnIndexOrThrow(COLUMN_YEAR))
                val coverUrl = getString(getColumnIndexOrThrow(COLUMN_COVERURL))

                books.add(Book(id.toString(), title, authors, year,coverUrl))
            }
        }
        cursor.close()
        return books
    }
    /**
     * Clears all jokes from the database.
     */
    fun clearAllBook() {
        val db = writableDatabase
        db.delete(TABLE_BOOKS, null, null)
    }

}
package org.kp5_7.project

import Book
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DataRepository {
    private val _data = MutableLiveData<Book>()
    val data: LiveData<Book> get() = _data

    fun updateData(data: Book) {
        _data.value = data
    }
}

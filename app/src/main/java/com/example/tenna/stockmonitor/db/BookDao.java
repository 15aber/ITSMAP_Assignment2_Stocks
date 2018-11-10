//modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#4
//and from https://codelabs.developers.google.com/codelabs/android-persistence/#3

package com.example.tenna.stockmonitor.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    //Add new book to book_table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Book book);

    //Delete all books in book_table
    @Query("DELETE FROM book_table")
    void deleteAll();

    //get all books
    @Query("SELECT * from book_table ORDER BY company_name ASC")
    LiveData<List<Book>> getAllBooks();

    //get book by id
    @Query("select * from book_table where symbol = :symbol")
    Book loadBookById(String symbol);
}

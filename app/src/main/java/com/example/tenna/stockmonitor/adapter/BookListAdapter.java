//Code modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10

package com.example.tenna.stockmonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tenna.stockmonitor.R;
import com.example.tenna.stockmonitor.db.Book;
import com.example.tenna.stockmonitor.db.Stock;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    public void add(Book book) {
        books.add(book);
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookItemView;

        private BookViewHolder(View itemView) {
            super(itemView);
            bookItemView = itemView.findViewById(R.id.tvListBookName);
        }
    }

    private final LayoutInflater mInflater;
    private List<Book> books; // Cached copy of books

    public BookListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.book_recyclerview_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        if (books != null) {
            Book current = books.get(position);
            holder.bookItemView.setText(current.getCompanyName());
        } else {
            // Covers the case of data not being ready yet.
            holder.bookItemView.setText("No books");
        }
    }

    public void setBooks(List<Book> books){
        this.books = books;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // books has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (books != null)
            return books.size();
        else return 0;
    }
}

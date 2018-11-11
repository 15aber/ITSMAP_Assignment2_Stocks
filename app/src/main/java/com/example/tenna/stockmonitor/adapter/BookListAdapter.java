//Code modified from https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#10

package com.example.tenna.stockmonitor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tenna.stockmonitor.R;
import com.example.tenna.stockmonitor.RecyclerViewItemClickListener;
import com.example.tenna.stockmonitor.db.Book;

import java.util.List;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    public void add(Book book) {
        books.add(book);
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookItemView;
        TextView tvListStockPrice;
        TextView tvListStockDiff;
        int position=0;
        LinearLayout listItem;

        private BookViewHolder(View itemView) {
            super(itemView);
            bookItemView = itemView.findViewById(R.id.tvListBookName);
            tvListStockPrice = itemView.findViewById(R.id.tvListStockPrice);
            tvListStockDiff = itemView.findViewById(R.id.tvListStockDiff);


            itemView.setOnClickListener(new View.OnClickListener() {
                //recyclerview on clicklistener inspired by http://android.mskurt.net/2015/12/28/handling-item-clicks-on-recyclerview/
                @Override
                public void onClick(View v) {
                    //When item view is clicked, trigger the itemclicklistener
                    //Because that itemclicklistener is indicated in MainActivity
                    recyclerViewItemClickListener.onItemClick(v,position);
                    //recyclerViewItemClickListener.onItemClick(bookItemView,position);
                    //recyclerViewItemClickListener.onItemClick(tvListStockPrice,position);
                    //recyclerViewItemClickListener.onItemClick(tvListStockDiff,position);

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //When item view is clicked long, trigger the itemclicklistener
                    //Because that itemclicklistener is indicated in MainActivity
                    recyclerViewItemClickListener.onItemLongClick(v,position);
                    return true;
                }
            });
        }


    }

    private final LayoutInflater mInflater;
    private List<Book> books; // Cached copy of books
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public BookListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    //Inspired by http://android.mskurt.net/2015/12/28/handling-item-clicks-on-recyclerview/
    //Set method of OnItemClickListener object
    public void setOnItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener){
        this.recyclerViewItemClickListener=recyclerViewItemClickListener;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.book_recyclerview_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, final int position) {
        if (books != null) {
            holder.position=position;
            Book current = books.get(position);
            holder.bookItemView.setText(current.getCompanyName());
            holder.tvListStockPrice.setText(String.format("%.2f", current.getLatestValue()));
            holder.tvListStockDiff.setText(String.format("%.2f", current.getPurchasePrice()-current.getLatestValue()));

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

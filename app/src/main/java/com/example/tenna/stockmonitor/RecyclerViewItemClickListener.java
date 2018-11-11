//Inspired by http://android.mskurt.net/2015/12/28/handling-item-clicks-on-recyclerview/

package com.example.tenna.stockmonitor;

import android.view.View;

public interface RecyclerViewItemClickListener   {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}

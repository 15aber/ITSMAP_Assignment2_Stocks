package com.example.tenna.stockmonitor;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//Code Modified from https://developer.android.com/training/volley/requestqueue
public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mContext;


    private MySingleton(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public void addToRequestQueue(Request request) {requestQueue.add(request);}
}
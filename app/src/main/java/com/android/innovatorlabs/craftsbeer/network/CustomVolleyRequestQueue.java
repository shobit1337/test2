package com.android.innovatorlabs.craftsbeer.network;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

public class CustomVolleyRequestQueue {

    private static CustomVolleyRequestQueue sCustomVolleyRequestQueue;

    private RequestQueue mRequestQueue;

    private Context context;

    public static CustomVolleyRequestQueue getInstance(Context context){

        if(sCustomVolleyRequestQueue == null){

            synchronized (CustomVolleyRequestQueue.class){

                if(sCustomVolleyRequestQueue == null){

                    sCustomVolleyRequestQueue = new CustomVolleyRequestQueue(context);
                }
            }
        }

        return sCustomVolleyRequestQueue;
    }

    private CustomVolleyRequestQueue(Context context){
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){

        if(mRequestQueue == null){

            Cache cache = new DiskBasedCache(context.getCacheDir(), 10*1024*1024);

            Network network = new BasicNetwork(new HurlStack());

            mRequestQueue = new RequestQueue(cache, network, 3);

            mRequestQueue.start();
        }

        return mRequestQueue;
    }
}

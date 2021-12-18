package com.android.innovatorlabs.craftsbeer.db.entity;

import android.text.TextUtils;

import com.android.innovatorlabs.craftsbeer.db.AppDatabase;

import java.util.ArrayList;

public class RecentlySearchedProducts {

    private static RecentlySearchedProducts sRecentlySearchedProducts;

    private ArrayList<String> items;

    public RecentlySearchedProducts getInstance(){

        if(sRecentlySearchedProducts == null){

            synchronized (RecentlySearchedProducts.class){

                if(sRecentlySearchedProducts == null){

                    sRecentlySearchedProducts = new RecentlySearchedProducts();
                }
            }
        }

        return sRecentlySearchedProducts;
    }

    public void insertRecentlySearchedItems(String name){

        if(items == null){
            items = new ArrayList<>(5);
        }

        if(TextUtils.isEmpty(name)){
            return;
        }

        if(items.contains(name)){
            items.remove(name);
        }

        items.add(0,name);
    }

    public ArrayList<String> getRecentlySearchedItems(){
        return items;
    }
}

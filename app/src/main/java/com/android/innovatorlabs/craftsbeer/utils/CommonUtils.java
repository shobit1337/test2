package com.android.innovatorlabs.craftsbeer.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.innovatorlabs.craftsbeer.BaseApplication;
import com.android.innovatorlabs.craftsbeer.db.entity.FilterEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommonUtils {

    private static EventBus eventBus;

    private static void initializeEventBus(){

        if(eventBus == null){
            eventBus = new EventBus();
        }
    }

    public static void registerEventBus(Context context){

        initializeEventBus();

        if(!eventBus.isRegistered(context)){
            eventBus.register(context);
        }
    }

    public static void unregisterEventBus(Context context){

        if(eventBus == null){
            return;
        }

        if(eventBus.isRegistered(context)){
            eventBus.unregister(context);
        }
    }

    public static void postEvent(int eventId, Object eventObject){

        initializeEventBus();

        IEvent iEvent = new IEvent.Builder().setEventID(eventId).setEventObject(eventObject).build();

        eventBus.post(iEvent);
    }

    public static void postStickEvent(int eventId, Object eventObject){

        initializeEventBus();

        IEvent iEvent = new IEvent.Builder().setEventID(eventId).setEventObject(eventObject).build();

        eventBus.postSticky(iEvent);
    }

    public static boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static List<FilterEntity> getFilters(Activity activity){
        return ((BaseApplication)activity.getApplication()).getAppDatabase().filterDao().loadAllFilters();
    }
}

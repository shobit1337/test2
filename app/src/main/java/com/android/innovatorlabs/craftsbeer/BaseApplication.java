package com.android.innovatorlabs.craftsbeer;

import android.app.Application;

import com.android.innovatorlabs.craftsbeer.db.AppDatabase;

public class BaseApplication extends Application {

    private AppExecutors appExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        appExecutors = new AppExecutors();
    }

    public AppDatabase getAppDatabase() {
        return AppDatabase.getInstance(this, appExecutors);
    }

}

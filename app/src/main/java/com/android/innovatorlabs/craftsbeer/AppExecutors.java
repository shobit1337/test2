package com.android.innovatorlabs.craftsbeer;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AppExecutors {

    private Executor mDiskThread;

    private Executor mNetworkThread;

    private Executor mMainThread;

    public AppExecutors(){
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3), new MainThreadExecutor());
    }

    private AppExecutors(Executor mDiskThread, Executor mNetworkThread, Executor mMainThread){
        this.mDiskThread = mDiskThread;
        this.mNetworkThread = mNetworkThread;
        this.mMainThread = mMainThread;
    }

    public Executor getMainThread() {
        return mMainThread;
    }

    public Executor getNetworkThread() {
        return mNetworkThread;
    }

    public Executor getDiskThread() {
        return mDiskThread;
    }

    private static class MainThreadExecutor implements Executor{

        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    }
}

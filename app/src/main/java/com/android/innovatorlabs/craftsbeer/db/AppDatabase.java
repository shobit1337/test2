package com.android.innovatorlabs.craftsbeer.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.innovatorlabs.craftsbeer.AppExecutors;
import com.android.innovatorlabs.craftsbeer.db.dao.FilterDao;
import com.android.innovatorlabs.craftsbeer.db.dao.ProductDao;
import com.android.innovatorlabs.craftsbeer.db.entity.FilterEntity;
import com.android.innovatorlabs.craftsbeer.db.entity.ProductEntity;

import java.util.List;

@Database(entities = {ProductEntity.class, FilterEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static String DATABASE_NAME = "crafts_beer_database.db";

    private static AppDatabase sAppDatabase;

    public abstract ProductDao productDao();

    public abstract FilterDao filterDao();

    private boolean mDatabaseCreated;

    public static AppDatabase getInstance(Context context, AppExecutors appExecutors){

        if(sAppDatabase == null){

            synchronized (AppDatabase.class){

                if(sAppDatabase == null){

                    sAppDatabase = buildDatabase(context, appExecutors);

                    sAppDatabase.updateDatabaseCreated(context);
                }
            }
        }

        return sAppDatabase;
    }

    private static AppDatabase buildDatabase(final Context context, final AppExecutors appExecutors){

        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                final AppDatabase appDatabase = AppDatabase.getInstance(context, appExecutors);

                appDatabase.setDatabaseCreated();

            }
        }).allowMainThreadQueries().build();
    }

    public void insertProducts(AppDatabase appDatabase, final List<ProductEntity> productEntities){

        if(productEntities != null && !productEntities.isEmpty()){

            appDatabase.runInTransaction(new Runnable() {
                @Override
                public void run() {

                    sAppDatabase.productDao().insertProducts(productEntities);
                }
            });
        }
    }

    public void insertFilters(AppDatabase appDatabase, final List<FilterEntity> filterEntities){

        if(filterEntities != null && !filterEntities.isEmpty()){

            appDatabase.runInTransaction(new Runnable() {
                @Override
                public void run() {

                    sAppDatabase.filterDao().insertFilters(filterEntities);
                }
            });
        }
    }


    private void updateDatabaseCreated(Context context){
        if(context.getDatabasePath(DATABASE_NAME).exists()){
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mDatabaseCreated = true;
    }

    public boolean getDatabaseCreated(){
        return mDatabaseCreated;
    }
}

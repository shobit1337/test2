package com.android.innovatorlabs.craftsbeer.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.android.innovatorlabs.craftsbeer.db.entity.FilterEntity;

import java.util.List;

@Dao
public interface FilterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFilters(List<FilterEntity> filters);

    @Query("SELECT * FROM crafts_beer_filters")
    List<FilterEntity> loadAllFilters();
}

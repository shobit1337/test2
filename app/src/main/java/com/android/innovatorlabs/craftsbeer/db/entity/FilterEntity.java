package com.android.innovatorlabs.craftsbeer.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.android.innovatorlabs.craftsbeer.model.Filter;
import com.android.innovatorlabs.craftsbeer.model.Product;

import java.util.List;

@Entity(tableName = "crafts_beer_filters")
public class FilterEntity implements Filter{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "filter")
    private String filter;

    @Override
    public String getFilter() {
        return filter;
    }

    public FilterEntity(String filter){
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

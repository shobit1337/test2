package com.android.innovatorlabs.craftsbeer.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.android.innovatorlabs.craftsbeer.model.Product;

@Entity(tableName = "crafts_beer_products")
public class ProductEntity implements Product{

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "alcoholContent")
    private String alcoholContent;

    @ColumnInfo(name = "bitterUnits")
    private String bitterUnits;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "style")
    private String style;

    @ColumnInfo(name = "ounces")
    private double ounces;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getAlcoholContent() {
        return alcoholContent;
    }

    @Override
    public String getBitterUnits() {
        return bitterUnits;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public double getOunces() {
        return ounces;
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBitterUnits(String bitterUnits) {
        this.bitterUnits = bitterUnits;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setOunces(double ounces) {
        this.ounces = ounces;
    }
}

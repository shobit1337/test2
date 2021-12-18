package com.android.innovatorlabs.craftsbeer.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.android.innovatorlabs.craftsbeer.db.entity.ProductEntity;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProducts(List<ProductEntity> productEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ProductEntity productEntity);

    @Query("SELECT * FROM crafts_beer_products WHERE id = :productId")
    ProductEntity getProductById(int productId);

    @Query("SELECT * FROM crafts_beer_products WHERE style = :productStyle")
    List<ProductEntity> getProductByStyle(String productStyle);

    @Query("SELECT * FROM crafts_beer_products WHERE name = :productName")
    List<ProductEntity> getProductByName(String productName);

    @Query("SELECT * FROM crafts_beer_products")
    List<ProductEntity> getAllProducts();
}

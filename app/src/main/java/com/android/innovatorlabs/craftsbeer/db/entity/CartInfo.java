package com.android.innovatorlabs.craftsbeer.db.entity;

import java.util.HashMap;

public class CartInfo {

    private static CartInfo sCartInfo;

    private HashMap<ProductEntity, Integer> addedProducts;

    public static CartInfo getInstance(){

        if(sCartInfo == null){

            synchronized (CartInfo.class){

                if(sCartInfo == null){

                    sCartInfo = new CartInfo();
                }
            }
        }

        return sCartInfo;
    }

    public void addProduct(ProductEntity productEntity){

        if(addedProducts == null){
            addedProducts = new HashMap<>();
        }

        if(addedProducts.get(productEntity) == null){
            addedProducts.put(productEntity, 1);
            return;
        }

        addedProducts.put(productEntity, addedProducts.get(productEntity) + 1);
    }

    public HashMap<ProductEntity, Integer> getAddedProducts(){
        return addedProducts;
    }
}

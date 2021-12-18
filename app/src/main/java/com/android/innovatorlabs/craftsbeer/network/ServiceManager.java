package com.android.innovatorlabs.craftsbeer.network;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.constants.ResponseParams;
import com.android.innovatorlabs.craftsbeer.constants.ServiceConstants;
import com.android.innovatorlabs.craftsbeer.db.entity.ProductEntity;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceManager {

    private static final String TAG = ServiceManager.class.getSimpleName();

    private static int objectsParsed = 0;

    public static void fetchProducts(final Context context){

        CustomVolleyRequestQueue customVolleyRequestQueue = CustomVolleyRequestQueue.getInstance(context.getApplicationContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ServiceConstants.PRODUCTS_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                parseResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                CommonUtils.postEvent(IEventConstants.EVENT_PRODUCTS_CALL_FAILURE, error);
            }
        });

        jsonArrayRequest.setShouldCache(true);

        customVolleyRequestQueue.getRequestQueue().add(jsonArrayRequest);
    }

    private static void parseResponse(final JSONArray response){

        final Set<String> filters = new HashSet<>();

        final int length = response.length();

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if(objectsParsed == length){
                    handler.removeCallbacks(this);
                }

                int max = objectsParsed + 100 > length ? length : objectsParsed + 100;

                List<ProductEntity> productEntities = new ArrayList<>();

                for (int i = objectsParsed; i < max; i++) {

                    JSONObject jsonObject = null;

                    try {
                        jsonObject = response.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(jsonObject != null){

                        ProductEntity productEntity = new ProductEntity();

                        try {

                            productEntity.setId(jsonObject.getInt(ResponseParams.ID));

                            productEntity.setName(jsonObject.getString(ResponseParams.NAME));

                            productEntity.setAlcoholContent(jsonObject.getString(ResponseParams.ABV));

                            productEntity.setBitterUnits(jsonObject.getString(ResponseParams.IBU));

                            productEntity.setOunces(jsonObject.getDouble(ResponseParams.OUNCES));

                            String style = jsonObject.getString(ResponseParams.STYLE);

                            if(!TextUtils.isEmpty(style)){
                                filters.add(style);
                            }

                            productEntity.setStyle(style);

                            productEntities.add(productEntity);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(i == max - 1){

                        objectsParsed = max;

                        CommonUtils.postStickEvent(IEventConstants.EVENT_DATABASE_INSERT_PRODUCTS, productEntities);

                        handler.postDelayed(this, 1000);
                    }

                    if(i == 99){
                        CommonUtils.postStickEvent(IEventConstants.EVENT_LOAD_PRODUCTS, productEntities);
                    }

                    if(max == length && i == max - 1){
                        CommonUtils.postStickEvent(IEventConstants.EVENT_DATABASE_INSERT_DONE, productEntities);

                        CommonUtils.postStickEvent(IEventConstants.EVENT_DATABASE_INSERT_FILTERS, filters);
                    }
                }
            }
        };

        handler.postDelayed(runnable, 1000);
    }
}

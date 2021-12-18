package com.android.innovatorlabs.craftsbeer.constants;

public class IEventConstants {

    public static final int EVENT_PRODUCTS_CALL_SUCCESS = 10000;

    public static final int EVENT_PRODUCTS_CALL_FAILURE = EVENT_PRODUCTS_CALL_SUCCESS + 1;

    public static final int EVENT_DATABASE_INSERT_PRODUCTS = EVENT_PRODUCTS_CALL_FAILURE + 1;

    public static final int EVENT_LOAD_PRODUCTS = EVENT_DATABASE_INSERT_PRODUCTS + 1;

    public static final int EVENT_DATABASE_INSERT_DONE = EVENT_LOAD_PRODUCTS + 1;

    public static final int EVENT_PRODUCT_ADD_TO_CART = EVENT_DATABASE_INSERT_DONE + 1;

    public static final int EVENT_SORT_PRODUCTS = EVENT_PRODUCT_ADD_TO_CART + 1;

    public static final int EVENT_FILTER_PRODUCTS = EVENT_SORT_PRODUCTS + 1;

    public static final int EVENT_FILTER_ITEM_CLICK = EVENT_FILTER_PRODUCTS + 1;

    public static final int EVENT_SORT_ITEM_CLICK = EVENT_FILTER_ITEM_CLICK + 1;

    public static final int EVENT_DATABASE_INSERT_FILTERS = EVENT_SORT_ITEM_CLICK + 1;

    public static final int EVENT_CLEAR_FILTERS = EVENT_DATABASE_INSERT_FILTERS + 1;

    public static final int EVENT_SEARCH_BY_NAME = EVENT_CLEAR_FILTERS + 1;

}

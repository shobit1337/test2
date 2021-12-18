package com.android.innovatorlabs.craftsbeer.ui;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.innovatorlabs.craftsbeer.BaseApplication;
import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.Constants;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.databinding.ActivityMainBinding;
import com.android.innovatorlabs.craftsbeer.db.entity.CartInfo;
import com.android.innovatorlabs.craftsbeer.db.entity.FilterEntity;
import com.android.innovatorlabs.craftsbeer.db.entity.ProductEntity;
import com.android.innovatorlabs.craftsbeer.network.ServiceManager;
import com.android.innovatorlabs.craftsbeer.ui.adapters.ProductAdapter;
import com.android.innovatorlabs.craftsbeer.ui.widget.FilterWidget;
import com.android.innovatorlabs.craftsbeer.ui.widget.SortWidget;
import com.android.innovatorlabs.craftsbeer.utils.AlertDialogUtils;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;
import com.android.innovatorlabs.craftsbeer.utils.IEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ProductListActivity extends AppCompatActivity {

    private List<ProductEntity> productEntities = new ArrayList<>();

    private ActivityMainBinding mBinding;

    private ProductAdapter productAdapter;

    private BottomSheetBehavior filterWidgetBehavior;

    private BottomSheetBehavior sortWidgetBehavior;

    private boolean isInsertionDone = false;

    private boolean loading = false;

    private int pastVisibleItems;

    private int visibleItemCount;

    private int totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        CommonUtils.registerEventBus(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initializeViews();

        mBinding.setIsLoading(true);

        boolean isServiceCallCompleted = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.INSERTION_KEY_VALUE, false);

        if(isServiceCallCompleted){

            isInsertionDone = true;

            if(mBinding.optionsWidget.getVisibility() == View.GONE){
                mBinding.optionsWidget.slideUpAnimation(this);
            }

            productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getAllProducts();

            if(productEntities != null && !productEntities.isEmpty()){

                setLayout(true);

                setProductAdapter(productEntities.subList(0, 100));
            }

        }else {

            if(CommonUtils.isInternetAvailable(this)){

                mBinding.setIsLoading(true);

                ServiceManager.fetchProducts(this);

            }else {

                AlertDialogUtils.showInternetUnavailableAlertDialog(this);
            }
        }
    }

    private void initializeViews(){

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        mBinding.productListRecyclerView.setLayoutManager(linearLayoutManager);

        productAdapter = new ProductAdapter(this);

        mBinding.productListRecyclerView.setAdapter(productAdapter);

        mBinding.productListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);

                if(!isInsertionDone){
                    return;
                }

                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    mBinding.optionsWidget.slideDownAnimation(ProductListActivity.this);
                }else if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    mBinding.optionsWidget.slideUpAnimation(ProductListActivity.this);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);

                if(dy > 0){

                    visibleItemCount = linearLayoutManager.getChildCount();

                    totalItemCount = linearLayoutManager.getItemCount();

                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!loading && (visibleItemCount + pastVisibleItems) >= totalItemCount) {

                        loading = true;

                        Log.v("...", "Last Item Wow !");

                        doPagination();
                    }
                }
            }
        });
    }

    private void doPagination(){

        List<ProductEntity> productEntities;

        if(isInsertionDone){

           productEntities = this.productEntities;

        }else {
            productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getAllProducts();
        }

        int size = productAdapter.getProducts().size();

        int max = size + 100 > productEntities.size() ? productEntities.size() : size + 100;

        productAdapter.addProducts(productEntities.subList(0, max));

        loading = false;
    }

    @Subscribe
    public void onEvent(IEvent iEvent){

        int eventId = iEvent.getEventID();

        if(eventId == IEventConstants.EVENT_DATABASE_INSERT_PRODUCTS){

            List<ProductEntity> productEntityList = (List<ProductEntity>) iEvent.getEventObject();

            ((BaseApplication)getApplication()).getAppDatabase().
                    insertProducts(((BaseApplication)getApplication()).getAppDatabase(), productEntityList);

        }else if(eventId == IEventConstants.EVENT_LOAD_PRODUCTS){

            setLayout(true);

            List<ProductEntity> productEntityList = (List<ProductEntity>) iEvent.getEventObject();

            setProductAdapter(productEntityList);

        }else if(eventId == IEventConstants.EVENT_PRODUCTS_CALL_FAILURE){

            setLayout(false);

        }else if(eventId == IEventConstants.EVENT_PRODUCT_ADD_TO_CART){

            ProductEntity productEntity = (ProductEntity) iEvent.getEventObject();

            CartInfo.getInstance().addProduct(productEntity);

            if(TextUtils.isEmpty(mBinding.cartIcon.getCount())){
                mBinding.cartIcon.setCount(1);
            }else {
                mBinding.cartIcon.setCount(Integer.valueOf(mBinding.cartIcon.getCount().toString()) + 1);
            }

            Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();

        }else if(eventId == IEventConstants.EVENT_SORT_PRODUCTS){

            launchSortWidget();

        }else if(eventId == IEventConstants.EVENT_FILTER_PRODUCTS){

            launchFilterWidget();

        }else if(eventId == IEventConstants.EVENT_SORT_ITEM_CLICK){

            sortWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            String sortType = (String) iEvent.getEventObject();

            processSortRequest(sortType);

        }else if(eventId == IEventConstants.EVENT_FILTER_ITEM_CLICK){

            filterWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            String filterType = (String) iEvent.getEventObject();

            processFilterRequest(filterType);

        }else if(eventId == IEventConstants.EVENT_DATABASE_INSERT_DONE){

            isInsertionDone = true;

            Toast.makeText(this, getString(R.string.processing_products_list_done), Toast.LENGTH_SHORT).show();

            if(mBinding.optionsWidget.getVisibility() == View.GONE){
                mBinding.optionsWidget.slideUpAnimation(this);
            }

            productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getAllProducts();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

            SharedPreferences.Editor editor = prefs.edit();

            editor.putBoolean(Constants.INSERTION_KEY_VALUE, true);

            editor.apply();

        }else if(eventId == IEventConstants.EVENT_DATABASE_INSERT_FILTERS){

            Set<String> filters = (Set<String>) iEvent.getEventObject();

            if(filters == null || filters.isEmpty()){
                return;
            }

            List<FilterEntity> filterEntities = new ArrayList<>();

            for (int i = 0; i < filters.size(); i++) {
                filterEntities.add(new FilterEntity((String) filters.toArray()[i]));
            }

            ((BaseApplication)getApplication()).getAppDatabase().
                    insertFilters(((BaseApplication)getApplication()).getAppDatabase(), filterEntities);

        }else if(eventId == IEventConstants.EVENT_CLEAR_FILTERS){

            if(sortWidgetBehavior != null && sortWidgetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                sortWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            if(filterWidgetBehavior != null && filterWidgetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                filterWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }

            mBinding.productListProgressBar.setVisibility(View.VISIBLE);

            mBinding.productListRecyclerView.setVisibility(View.GONE);

            mBinding.optionsWidget.setVisibility(View.GONE);

            productAdapter.setProducts(new ArrayList<ProductEntity>());

            productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getAllProducts();

            if(productEntities != null && !productEntities.isEmpty()){

                mBinding.productListProgressBar.setVisibility(View.GONE);

                mBinding.productListRecyclerView.setVisibility(View.VISIBLE);

                mBinding.optionsWidget.slideUpAnimation(this);

                setProductAdapter(productEntities.subList(0, 100));
            }
        }else if(eventId == IEventConstants.EVENT_SEARCH_BY_NAME){

            mBinding.productListProgressBar.setVisibility(View.VISIBLE);

            mBinding.productListRecyclerView.setVisibility(View.GONE);

            mBinding.optionsWidget.setVisibility(View.GONE);

            productAdapter.setProducts(new ArrayList<ProductEntity>());

            String query = (String) iEvent.getEventObject();

            productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getProductByName(query);

            if(productEntities != null && !productEntities.isEmpty()){

                if(productEntities.size() >= 100){
                    setProductAdapter(productEntities.subList(0, 100));
                }else {
                    setProductAdapter(productEntities.subList(0, productEntities.size()));
                }

                mBinding.productListProgressBar.setVisibility(View.GONE);

                mBinding.productListRecyclerView.setVisibility(View.VISIBLE);

                mBinding.optionsWidget.slideUpAnimation(this);

            }else {

                setErrorText(getString(R.string.no_product_with_the_name));
            }
        }
    }

    private void setProductAdapter(final List<ProductEntity> productEntities){

        mBinding.productListRecyclerView.smoothScrollToPosition(0);

        productAdapter.addProducts(productEntities);

        productAdapter.notifyDataSetChanged();
    }

    private void processSortRequest(String type){

        if(Constants.SORT_ALCOHOL_CONTENT_ASCENDING.equalsIgnoreCase(type)){

            mBinding.setIsLoading(true);

            mBinding.optionsWidget.setVisibility(View.GONE);

            productAdapter.setProducts(new ArrayList<ProductEntity>());

            Collections.sort(productEntities, new AlcoholAscendingComparator());

            if(productEntities.size() >= 100){
                setProductAdapter(productEntities.subList(0, 100));
            }else {
                setProductAdapter(productEntities.subList(0, productEntities.size()));
            }

            mBinding.setIsLoading(false);

            mBinding.optionsWidget.slideUpAnimation(this);


        }else if(Constants.SORT_ALCOHOL_CONTENT_DESCENDING.equalsIgnoreCase(type)){

            mBinding.setIsLoading(true);

            mBinding.optionsWidget.setVisibility(View.GONE);

            productAdapter.setProducts(new ArrayList<ProductEntity>());

            Collections.sort(productEntities, new AlcoholDescendingComparator());

            if(productEntities.size() >= 100){
                setProductAdapter(productEntities.subList(0, 100));
            }else {
                setProductAdapter(productEntities.subList(0, productEntities.size()));
            }

            mBinding.setIsLoading(false);

            mBinding.optionsWidget.slideUpAnimation(this);
        }
    }

    private void processFilterRequest(String type){

        mBinding.productListProgressBar.setVisibility(View.VISIBLE);

        mBinding.productListRecyclerView.setVisibility(View.GONE);

        mBinding.optionsWidget.setVisibility(View.GONE);

        productAdapter.setProducts(new ArrayList<ProductEntity>());

        productEntities = ((BaseApplication)getApplication()).getAppDatabase().productDao().getProductByStyle(type);

        mBinding.productListProgressBar.setVisibility(View.GONE);

        if(productEntities != null && !productEntities.isEmpty()){

            if(productEntities.size() >= 100){
                setProductAdapter(productEntities.subList(0, 100));
            }else {
                setProductAdapter(productEntities.subList(0, productEntities.size()));
            }

            mBinding.productListRecyclerView.setVisibility(View.VISIBLE);

            mBinding.optionsWidget.slideUpAnimation(this);

        }else {
            setErrorText(getString(R.string.no_filter_results));
        }
    }

    private void launchFilterWidget(){

        if(!isInsertionDone){
            Toast.makeText(this, getString(R.string.processing_products_list), Toast.LENGTH_SHORT).show();
            return;
        }

        FilterWidget filterWidget = new FilterWidget(this);

        filterWidgetBehavior = BottomSheetBehavior.from(mBinding.filterBottomSheet);

        filterWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        filterWidgetBehavior.setSkipCollapsed(true);

        ((ViewGroup) mBinding.filterBottomSheet.getParent()).removeView(filterWidget);

        mBinding.filterBottomSheet.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimens_200dp);

        filterWidgetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.dimens_200dp));

        mBinding.filterBottomSheet.addView(filterWidget);

        if(filterWidgetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            filterWidgetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void launchSortWidget(){

        if(!isInsertionDone){
            Toast.makeText(this, getString(R.string.processing_products_list), Toast.LENGTH_SHORT).show();
            return;
        }

        SortWidget sortWidget = new SortWidget(this);

        sortWidgetBehavior = BottomSheetBehavior.from(mBinding.sortBottomSheet);

        sortWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sortWidgetBehavior.setSkipCollapsed(true);

        ((ViewGroup) mBinding.sortBottomSheet.getParent()).removeView(sortWidget);

        mBinding.sortBottomSheet.getLayoutParams().height = (int) getResources().getDimension(R.dimen.dimens_120dp);

        sortWidgetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.dimens_120dp));

        mBinding.sortBottomSheet.addView(sortWidget);

        if(sortWidgetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
            sortWidgetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    private void setLayout(boolean isServiceCallSuccess){

        mBinding.setIsLoading(false);

        if(isServiceCallSuccess){

            mBinding.errorLayout.setVisibility(View.GONE);

        }else {
            mBinding.productListRecyclerView.setVisibility(View.GONE);

            mBinding.errorLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setErrorText(String errorText){

        mBinding.productListRecyclerView.setVisibility(View.GONE);

        mBinding.errorLayout.setVisibility(View.VISIBLE);

        mBinding.errorText.setText(errorText);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (filterWidgetBehavior != null && filterWidgetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {

                Rect outRect = new Rect();

                mBinding.filterBottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())){
                    filterWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

            }else if (sortWidgetBehavior != null && sortWidgetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {

                Rect outRect = new Rect();

                mBinding.sortBottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())){
                    sortWidgetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        CommonUtils.unregisterEventBus(this);
        super.onDestroy();
    }

    private class AlcoholAscendingComparator implements Comparator<ProductEntity>{

        @Override
        public int compare(ProductEntity p1, ProductEntity p2) {

            double p1Content = !TextUtils.isEmpty(p1.getAlcoholContent()) ? Double.parseDouble(p1.getAlcoholContent()) : 0;

            double p2Content = !TextUtils.isEmpty(p2.getAlcoholContent()) ? Double.parseDouble(p2.getAlcoholContent()) : 0;

            return Double.compare(p1Content, p2Content);
        }
    }

    private class AlcoholDescendingComparator implements Comparator<ProductEntity>{

        @Override
        public int compare(ProductEntity p1, ProductEntity p2) {

            double p1Content = !TextUtils.isEmpty(p1.getAlcoholContent()) ? Double.parseDouble(p1.getAlcoholContent()) : 0;

            double p2Content = !TextUtils.isEmpty(p2.getAlcoholContent()) ? Double.parseDouble(p2.getAlcoholContent()) : 0;

            return -Double.compare(p1Content, p2Content);
        }
    }
}

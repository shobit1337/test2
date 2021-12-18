package com.android.innovatorlabs.craftsbeer.ui.widget;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

public class OptionsWidget extends LinearLayout implements View.OnClickListener {

    public OptionsWidget(Context context) {
        super(context);
        initializeViews(context);
    }

    public OptionsWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public OptionsWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public OptionsWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context){

        View view = LayoutInflater.from(context).inflate(R.layout.options_widget,this, true);

        RelativeLayout sortContainer = view.findViewById(R.id.sort_container);

        RelativeLayout filterContainer = view.findViewById(R.id.filter_container);

        sortContainer.setOnClickListener(this);

        filterContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.sort_container){

            CommonUtils.postEvent(IEventConstants.EVENT_SORT_PRODUCTS, null);

        }else if(v.getId() == R.id.filter_container){

            CommonUtils.postEvent(IEventConstants.EVENT_FILTER_PRODUCTS, null);
        }
    }

    public void slideUpAnimation(final Activity activity) {
        this.setVisibility(View.VISIBLE);
        Animation slideInCount = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_top);
        this.startAnimation(slideInCount);
    }

    public void slideDownAnimation(Activity activity) {
        Animation slideOutCount = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_bottom);
        this.startAnimation(slideOutCount);
        this.setVisibility(View.GONE);
    }
}

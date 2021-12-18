package com.android.innovatorlabs.craftsbeer.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.Constants;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.ui.adapters.FilterAdapter;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

public class FilterWidget extends LinearLayout {

    public FilterWidget(Activity context) {
        super(context);
        initializeViews(context);
    }

    public FilterWidget(Activity context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public FilterWidget(Activity context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public FilterWidget(Activity context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Activity context){

        View view = LayoutInflater.from(context).inflate(R.layout.filter_widget,this, true);

        TextView clear = view.findViewById(R.id.clear);

        clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.postEvent(IEventConstants.EVENT_CLEAR_FILTERS, null);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.filter_types);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FilterAdapter filterAdapter = new FilterAdapter(context);

        recyclerView.setAdapter(filterAdapter);
    }
}

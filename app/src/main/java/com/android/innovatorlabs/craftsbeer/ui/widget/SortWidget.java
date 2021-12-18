package com.android.innovatorlabs.craftsbeer.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.Constants;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

public class SortWidget extends LinearLayout implements View.OnClickListener{

    public SortWidget(Context context) {
        super(context);
        initializeViews(context);
    }

    public SortWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public SortWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public SortWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context){

        View view = LayoutInflater.from(context).inflate(R.layout.sort_widget,this, true);

        TextView clear = view.findViewById(R.id.clear);

        RelativeLayout ascendingContainer = view.findViewById(R.id.sort_ascending_container);

        RelativeLayout descendingContainer = view.findViewById(R.id.sort_descending_container);

        ascendingContainer.setOnClickListener(this);

        descendingContainer.setOnClickListener(this);

        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.sort_ascending_container){

            CommonUtils.postEvent(IEventConstants.EVENT_SORT_ITEM_CLICK, Constants.SORT_ALCOHOL_CONTENT_ASCENDING);

        }else if(v.getId() == R.id.sort_descending_container){

            CommonUtils.postEvent(IEventConstants.EVENT_SORT_ITEM_CLICK, Constants.SORT_ALCOHOL_CONTENT_DESCENDING);

        }else if(v.getId() == R.id.clear){

            CommonUtils.postEvent(IEventConstants.EVENT_CLEAR_FILTERS, null);
        }
    }
}

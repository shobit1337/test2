package com.android.innovatorlabs.craftsbeer.ui.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.db.entity.FilterEntity;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private List<FilterEntity> mFilterTypes ;

    private Activity context;

    public FilterAdapter(Activity context){
        this.context = context;
        mFilterTypes = CommonUtils.getFilters(context);
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.filter_item, parent, false);

        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {

        FilterEntity filterEntity = mFilterTypes.get(position);

        holder.textView.setText(filterEntity.getFilter());

    }

    @Override
    public int getItemCount() {
        return mFilterTypes != null ? mFilterTypes.size() : 0;
    }

    public class FilterViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public FilterViewHolder(View view) {

            super(view);

            textView = view.findViewById(R.id.filter_text);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonUtils.postEvent(IEventConstants.EVENT_FILTER_ITEM_CLICK, mFilterTypes.get(getAdapterPosition()).getFilter());
                }
            });
        }
    }
}

package com.android.innovatorlabs.craftsbeer.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.innovatorlabs.craftsbeer.R;

public class CartBadgeMenu extends FrameLayout{

    private TextView countText;

    public CartBadgeMenu(Context context) {
        this(context, null);
    }

    public CartBadgeMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartBadgeMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.badge_layout, this);
        countText = (TextView) view.findViewById(R.id.count_txt);
    }

    public void setCount(int aCartCount) {
        if (aCartCount > 0) {
            countText.setText(String.valueOf(aCartCount));
            if (countText.getVisibility() != View.VISIBLE) {
                countText.setVisibility(View.VISIBLE);
            }
        } else {
            if (countText.getVisibility() == View.VISIBLE) {
                countText.setVisibility(View.GONE);
            }
        }
    }

    public CharSequence getCount() {
        if (countText != null) {
            return countText.getText();
        }
        return null;
    }
}

package com.android.innovatorlabs.craftsbeer.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.android.innovatorlabs.craftsbeer.ui.listeners.BackPressListener;

public class CustomEdiText extends AppCompatEditText{

    private BackPressListener backPressListener;

    public CustomEdiText(Context context) {
        super(context, null);
    }

    public CustomEdiText(Context context, AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public CustomEdiText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackPressListener(BackPressListener backPressListener){
        this.backPressListener = backPressListener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){

            if(backPressListener != null){

                backPressListener.onBackPressesd();
            }
        }

        return super.onKeyPreIme(keyCode, event);
    }
}

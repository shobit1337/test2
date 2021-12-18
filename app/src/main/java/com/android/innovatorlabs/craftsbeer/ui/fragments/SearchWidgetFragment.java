package com.android.innovatorlabs.craftsbeer.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.innovatorlabs.craftsbeer.R;
import com.android.innovatorlabs.craftsbeer.constants.Constants;
import com.android.innovatorlabs.craftsbeer.constants.IEventConstants;
import com.android.innovatorlabs.craftsbeer.ui.listeners.BackPressListener;
import com.android.innovatorlabs.craftsbeer.ui.widget.CustomEdiText;
import com.android.innovatorlabs.craftsbeer.utils.CommonUtils;

public class SearchWidgetFragment extends Fragment{

    private EditText customAutoCompleteTextView;

    private ImageView closeIcon;

    private ImageView voiceIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.widget_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        customAutoCompleteTextView = view.findViewById(R.id.search_auto_complete_text_view);

        customAutoCompleteTextView.setCursorVisible(false);

        closeIcon = view.findViewById(R.id.shop_close_icon);

        voiceIcon = view.findViewById(R.id.shop_voice_icon);

        customAutoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                closeIcon.setVisibility(View.VISIBLE);

                voiceIcon.setVisibility(View.GONE);

                customAutoCompleteTextView.setCursorVisible(true);

                return false;
            }
        });

        customAutoCompleteTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    customAutoCompleteTextView.setCursorVisible(false);

                    customAutoCompleteTextView.setText(customAutoCompleteTextView.getEditableText().toString());

                    closeIcon.setVisibility(View.GONE);

                    voiceIcon.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromInputMethod(getView().getWindowToken(), 0);

                    CommonUtils.postEvent(IEventConstants.EVENT_SEARCH_BY_NAME, customAutoCompleteTextView.getEditableText().toString());
                }

                return false;
            }
        });

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customAutoCompleteTextView.setCursorVisible(false);

                customAutoCompleteTextView.setText(Constants.EMPTY);

                closeIcon.setVisibility(View.GONE);

                voiceIcon.setVisibility(View.VISIBLE);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromInputMethod(getView().getWindowToken(), 0);
            }
        });
    }
}

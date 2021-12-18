package com.android.innovatorlabs.craftsbeer.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.innovatorlabs.craftsbeer.R;

public class AlertDialogUtils {

    public static void showInternetUnavailableAlertDialog(final Activity activity){

        if(activity.isFinishing()){
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(activity.getString(R.string.no_internet_title));

        builder.setMessage(activity.getString(R.string.no_internet_message));

        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });

        builder.show();
    }
}

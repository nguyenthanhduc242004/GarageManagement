package com.example.garagemanagement.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.garagemanagement.R;

public class ConfirmationDialog {

    public static void showConfirmationDialog(Context context, String title, String message,
                                              DialogInterface.OnClickListener onConfirmClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(AppCompatResources.getDrawable(builder.getContext(), R.drawable.baseline_warning_24));

        builder.setPositiveButton("Xác nhận", onConfirmClickListener);
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    };
}
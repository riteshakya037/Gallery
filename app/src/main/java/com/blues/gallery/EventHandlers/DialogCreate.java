package com.blues.gallery.EventHandlers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.blues.gallery.Adaptors.ImageModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */
public class DialogCreate {

    public DialogCreate(String title, final int pos, final CustomDialogInterface customDialogInterface, RunOption runOption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(customDialogInterface.getContext());
        builder.setTitle(title);
        final EditText input = new EditText(customDialogInterface.getContext());
        runOption.additionalOption(input, customDialogInterface.getContext());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<ImageModel> newData = new ArrayList<>();
                for (ImageModel imageModel : customDialogInterface.getData()) {
                    String[] propSplit = imageModel.getName().split("_");
                    if (propSplit.length == 3) {
                        if (propSplit[pos].equalsIgnoreCase(input.getText().toString())) {
                            newData.add(imageModel);
                        }
                    } else if (imageModel.getName().contains(input.getText().toString()))
                        newData.add(imageModel);
                }
                customDialogInterface.UpdateDone(newData, pos);

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static class DateOption implements RunOption {
        @Override
        public void additionalOption(final EditText input, Context context) {
            final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
            input.setFocusable(false);
            Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    input.setText(dateFormatter.format(newDate.getTime()));
                }

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fromDatePickerDialog.show();
                }
            });
        }
    }

    public static class TextOption implements RunOption {
        @Override
        public void additionalOption(final EditText input, Context context) {
        }
    }
}

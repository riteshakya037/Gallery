package com.blues.gallery.EventHandlers;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.Helper.DatabaseContract;
import com.blues.gallery.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */
public class DialogCreate {

    public DialogCreate(String title, final int pos, final CustomDialogInterface customDialogInterface, final RunOption runOption) {
        AlertDialog.Builder builder = new AlertDialog.Builder(customDialogInterface.getContext());
        builder.setTitle(title);
        final AutoCompleteTextView input = new AutoCompleteTextView(customDialogInterface.getContext());
        input.setSingleLine(true);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        runOption.additionalOption(input, customDialogInterface, pos);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customDialogInterface.UpdateDone(runOption.getData(), pos);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public static class DateOption implements RunOption {
        private EditText input;
        private CustomDialogInterface customDialogInterface;
        private int pos;

        @Override
        public void additionalOption(final AutoCompleteTextView input, CustomDialogInterface context, int pos) {
            this.input = input;
            this.customDialogInterface = context;
            this.pos = pos;

            final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
            input.setFocusable(false);
            Calendar newCalendar = Calendar.getInstance();
            final DatePickerDialog fromDatePickerDialog = new DatePickerDialog(context.getContext(), new DatePickerDialog.OnDateSetListener() {

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

        @Override
        public ArrayList<ImageModel> getData() {
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
            return newData;
        }
    }

    public static class TextOption implements RunOption {
        private EditText input;
        private CustomDialogInterface customDialogInterface;
        private int pos;

        @Override
        public void additionalOption(final AutoCompleteTextView input, CustomDialogInterface customDialogInterface, int pos) {
            this.input = input;
            this.customDialogInterface = customDialogInterface;
            this.pos = pos;
        }

        @Override
        public ArrayList<ImageModel> getData() {
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
            return newData;
        }
    }

    public static class Database implements RunOption {
        private EditText input;
        private CustomDialogInterface customDialogInterface;

        @Override
        public void additionalOption(final AutoCompleteTextView input, CustomDialogInterface customDialogInterface, int pos) {
            this.input = input;
            this.customDialogInterface = customDialogInterface;
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(customDialogInterface.getContext());
            String[] languages = dbHelper.getTitles();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(customDialogInterface.getContext(), R.layout.datase_search_dropdown, languages);
            input.setAdapter(adapter);
            input.setThreshold(1);
        }

        @Override
        public ArrayList<ImageModel> getData() {
            ArrayList<ImageModel> newData;
            ProgressDialog dialog = ProgressDialog.show(customDialogInterface.getContext(), "",
                    "Writing. Please wait...", true, false);
            DatabaseContract.DbHelper selectFiles = new DatabaseContract.DbHelper(customDialogInterface.getContext());
            newData = selectFiles.onSelect(input.getText().toString());
            dialog.dismiss();
            return newData;
        }
    }
}

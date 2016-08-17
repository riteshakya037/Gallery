package com.blues.gallery.EventHandlers;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.blues.gallery.Activity.MomentsFragment;
import com.blues.gallery.CustomViews.NDSpinner;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public class SpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    boolean userSelect = false;
    private NDSpinner spinner;
    ResetInterface resetInterface;
    CustomDialogInterface customDialogInterface;

    public SpinnerInteractionListener(MomentsFragment fragment) {
        this.spinner = fragment.getSpinner();
        this.resetInterface = fragment;
        this.customDialogInterface = fragment;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (userSelect) {
            String items = spinner.getSelectedItem().toString();
            switch (items) {
                case "ALL":
                    resetInterface.resetForAll();
                    return;
                case "DATE":
                    new DialogCreate("Select Date", 1, customDialogInterface, new DialogCreate.DateOption());
                    resetInterface.resetLayout(1);
                    return;
                case "LOCATION":
                    new DialogCreate("Select Location", 0, customDialogInterface, new DialogCreate.TextOption());
                    resetInterface.resetLayout(2);
                    return;
                case "EVENT":
                    new DialogCreate("Select Event", 2, customDialogInterface, new DialogCreate.TextOption());
                    resetInterface.resetLayout(3);

            }
            userSelect = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}
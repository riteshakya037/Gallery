package com.blues.gallery.EventHandlers;

import android.widget.AutoCompleteTextView;

import com.blues.gallery.Adaptors.ImageModel;

import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public interface RunOption {
    void additionalOption(AutoCompleteTextView editText, CustomDialogInterface context, int pos);

    ArrayList<ImageModel> getData();
}

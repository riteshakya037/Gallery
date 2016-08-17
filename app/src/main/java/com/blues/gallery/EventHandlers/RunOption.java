package com.blues.gallery.EventHandlers;

import android.content.Context;
import android.widget.EditText;

import com.blues.gallery.Adaptors.ImageModel;

import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public interface RunOption {
    void additionalOption(EditText editText, CustomDialogInterface context, int pos);

    ArrayList<ImageModel> getData();
}

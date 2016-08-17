package com.blues.gallery.EventHandlers;

import android.content.Context;

import com.blues.gallery.Adaptors.ImageModel;

import java.util.ArrayList;

/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public interface CustomDialogInterface {
    void UpdateDone(ArrayList<ImageModel> data, int pos);
    Context getContext();
    ArrayList<ImageModel> getData();
}

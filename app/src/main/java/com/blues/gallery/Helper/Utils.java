package com.blues.gallery.Helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.blues.gallery.Adaptors.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Ritesh Shakya on 7/24/2016.
 */
public class Utils {

    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    HashMap<String, ArrayList<ImageModel>> filePaths;

    // Reading file paths from SDCard
    public HashMap<String, ArrayList<ImageModel>> getFilePaths(String albumName) {
        filePaths = new HashMap<>();

        File directory = new File(
                android.os.Environment.getExternalStorageDirectory()
                        + File.separator /*+ AppConstant.PHOTO_ALBUM*/);

        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();
            for (File file : listFiles) {
                recursiveSearch(albumName, file);
            }
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(_context);
            alert.setTitle("Error!");
            alert.setMessage(AppConstant.PHOTO_ALBUM
                    + " directory path is not valid! Please set the image directory name AppConstant.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    private void recursiveSearch(String albumName, File directory) {
        if (directory.isDirectory() && !directory.getName().startsWith(".")) {
            // getting list of file paths
            for (File file : directory.listFiles()) {
                recursiveSearch(albumName, file);
            }
        } else {
            String filePath = directory.getName();
            // check for supported file extension
            if (IsSupportedFile(filePath)) {
                // Add image path to array list
                if (albumName == null) {
                    addFile(directory);
                } else if (directory.getParent().equals(albumName)) {
                    addFile(directory);
                }
            }
        }
    }

    private void addFile(File directory) {
        ImageModel imageModel = new ImageModel();
        imageModel.setName(directory.getName());
        imageModel.setUrl(directory.getAbsolutePath());
        if (!filePaths.containsKey(directory.getParent())) {
            filePaths.put(directory.getParent(), new ArrayList<ImageModel>());
        }
        filePaths.get(directory.getParent()).add(imageModel);

    }
    // Check supported file extensions

    private boolean IsSupportedFile(String filePath) {
        String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstant.FILE_EXTN
                .contains(ext.toLowerCase(Locale.getDefault())) && !(new File(filePath).getName().startsWith(".")))
            return true;
        else
            return false;

    }

    /*
     * getting screen width
     */
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public static String getName(String filePath) {
        return (new File(filePath)).getName();
    }

    public float dpToPx(int i) {
        Resources r = _context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, r.getDisplayMetrics());
    }


}
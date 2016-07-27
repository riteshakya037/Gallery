package com.blues.gallery.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DummyActivity extends AppCompatActivity {
    public static HashMap<String, ArrayList<ImageModel>> IMGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyHavePermission()) {
                requestForSpecificPermission();
            } else {
                init();
            }
        } else {
            init();
        }
    }

    private void init() {
        Toast.makeText(this, "Scanning for Images",
                Toast.LENGTH_LONG).show();
        Utils utils = new Utils(DummyActivity.this);
        IMGS = utils.getFilePaths(null);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private boolean checkIfAlreadyHavePermission() {
        int result = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_LOGS}, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    requestForSpecificPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

package com.blues.gallery.CustomViews;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;


/**
 * Created by Ritesh Shakya on 8/17/2016.
 */

public class CustomDialogClass extends Dialog implements View.OnClickListener {
    private Button btnSave;

    public CustomDialogClass(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.action_dialog);
        btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(this);
        Utils utils = new Utils(getContext());
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            getWindow().setLayout((int) (utils.getScreenWidth() * .9), WindowManager.LayoutParams.WRAP_CONTENT);
        else
            getWindow().setLayout((int) (utils.getScreenWidth() * .5), WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getContext(), "ZIP", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

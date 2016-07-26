package com.blues.gallery.BottomSheet;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ritesh on 7/8/2015.
 */
public class BottomSheet extends Dialog implements View.OnClickListener {
    private Context context;
    private ImageModel imageModel;
    TextView title, dateTaken, albumName, albumSource;
    ImageView imageView;

    public BottomSheet(Context context, ImageModel imageModel) {
        super(context, R.style.BottomSheet_Dialog);
        this.context = context;
        this.imageModel = imageModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_out_menu);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        params.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);
        setCanceledOnTouchOutside(true);

        //-------------------------END-------------------------------------/
        title = (TextView) findViewById(R.id.photoTitle);
        title.setText(imageModel.getName());
        File file = new File(imageModel.getUrl());
        Date d = new Date(file.lastModified());
        String PATTERN = "MMMM dd, yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(PATTERN);

        dateTaken = (TextView) findViewById(R.id.dateTaken);
        dateTaken.setText(dateFormat.format(d));

        albumName = (TextView) findViewById(R.id.albumName);
        albumName.setText(Utils.getName(file.getParent()));
        imageView = (ImageView) findViewById(R.id.threeButton);
        RotateAnimation animation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(100);
        imageView.startAnimation(animation);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.threeButtonPanel);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void show() {
        super.show();
        RotateAnimation animation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(400);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);

    }

    @Override
    public void dismiss() {
        RotateAnimation animation = new RotateAnimation(180f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(200);
        imageView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BottomSheet.super.dismiss();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }
}

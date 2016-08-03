package com.blues.gallery.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.CustomViews.MultiViewPager;
import com.blues.gallery.EventHandlers.ZoomOutPageTransformer;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class CarouselActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    public ArrayList<ImageModel> data = new ArrayList<>();
    int pos;
    MultiViewPager pager;

    LinearLayout footer;
    RelativeLayout imageView;
    ImageView threeImage;
    private int oldPosition = Integer.MAX_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        data = getIntent().getParcelableArrayListExtra("data");
        pos = getIntent().getIntExtra("pos", 0);
        boolean overlayCheck = getIntent().getBooleanExtra("overlayCheck", false);
        ImageModel currentClicked = data.get(pos);

        Utils utils = new Utils(this);
        if (overlayCheck) {
            Iterator<ImageModel> iterator = data.iterator();
            while (iterator.hasNext()) {
                ImageModel next = iterator.next();
                if (utils.checkJpegPlus(next)) {
                    iterator.remove();
                }
            }
            pos = data.indexOf(currentClicked);
        }
        pager = (MultiViewPager) findViewById(R.id.pager);

        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Fragment getItem(int position) {
                return ScreenSlidePageFragment.create(position, data.get(position));
            }

        };
        pager.setAdapter(adapter);
        pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setCurrentItem(pos);
        changeBottomDetail(pos);
        pager.addOnPageChangeListener(this);
        imageView = (RelativeLayout) findViewById(R.id.threeButton);
        imageView.setOnClickListener(this);
        threeImage = (ImageView) findViewById(R.id.threeImage);

        footer = (LinearLayout) findViewById(R.id.contentPanel);
    }


    @Override
    public void onClick(View view) {
        if (footer.getVisibility() == View.VISIBLE) {
            collapse(footer);
        } else {
            expand(footer);
        }

//        showBottomSheet(pager.getCurrentItem());
    }

    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        RotateAnimation animation = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        animation.setFillAfter(true);
        threeImage.startAnimation(animation);
        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        RotateAnimation animation = new RotateAnimation(180f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        threeImage.startAnimation(animation);
        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    private void changeBottomDetail(int position) {
        TextView title = (TextView) findViewById(R.id.photoTitle);
        title.setText(data.get(position).getName());
        File file = new File(data.get(position).getUrl());
        Date d = new Date(file.lastModified());
        String PATTERN = "MMMM dd, yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(PATTERN);

        TextView dateTaken = (TextView) findViewById(R.id.dateTaken);
        dateTaken.setText(dateFormat.format(d));

        TextView albumName = (TextView) findViewById(R.id.albumName);
        albumName.setText(Utils.getName(file.getParent()));
    }

    @Override
    public void onPageSelected(int position) {
        changeBottomDetail(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

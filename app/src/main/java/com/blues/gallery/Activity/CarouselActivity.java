package com.blues.gallery.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.BottomSheet.BottomSheet;
import com.blues.gallery.CustomViews.MultiViewPager;
import com.blues.gallery.EventHandlers.ZoomOutPageTransformer;
import com.blues.gallery.R;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity implements View.OnClickListener {
    public ArrayList<ImageModel> data = new ArrayList<>();
    int pos;
    MultiViewPager pager;

    private BottomSheet bottomSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        data = getIntent().getParcelableArrayListExtra("data");
        pos = getIntent().getIntExtra("pos", 0);

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
        RelativeLayout imageView = (RelativeLayout) findViewById(R.id.threeButton);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            showBottomSheet(pager.getCurrentItem());
    }

    public void showBottomSheet(int mPageNumber) {
        if (bottomSheet == null || !bottomSheet.isShowing()) {
            bottomSheet = new BottomSheet(this, data.get(mPageNumber));
            bottomSheet.show();
        }
    }

}

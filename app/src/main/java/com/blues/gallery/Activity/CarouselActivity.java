package com.blues.gallery.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.CustomViews.MultiViewPager;
import com.blues.gallery.R;
import com.blues.gallery.EventHandlers.ZoomOutPageTransformer;

import java.util.ArrayList;

public class CarouselActivity extends AppCompatActivity {
    public ArrayList<ImageModel> data = new ArrayList<>();
    int pos;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        data = getIntent().getParcelableArrayListExtra("data");
        pos = getIntent().getIntExtra("pos", 0);

        final MultiViewPager pager = (MultiViewPager) findViewById(R.id.pager);

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

    }
}

/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blues.gallery.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blues.gallery.Adaptors.ImageModel;
import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class ScreenSlidePageFragment extends Fragment {
    /**
     * The argument key for the page number this fragment represents.
     */
    public static final String ARG_PAGE = "page";
    public static final String ARG_IMAGE = "image";

    /**
     * The fragment's page number, which is set to the argument value for {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private ImageModel mImage;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber, ImageModel imageModel) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putParcelable(ARG_IMAGE, imageModel);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        mImage = getArguments().getParcelable(ARG_IMAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Utils utils = new Utils(getActivity());
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageHolder);
        imageView.setId(View.generateViewId());
        final ImageView item_overlay = (ImageView) rootView.findViewById(R.id.item_overlay);

        Glide.with(getActivity()).load(mImage.getUrl()).asBitmap()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (utils.checkJpegPlus(mImage)) {
                            item_overlay.setVisibility(View.VISIBLE);
                        }
                        return false;
                    }
                })
                .into(imageView);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.vg_cover);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayout.getLayoutParams();
        layoutParams.width = (int) (utils.getScreenWidth() * .6f);
        relativeLayout.setLayoutParams(layoutParams);

        if (utils.checkJpegPlus(mImage)) {
            layoutParams = new RelativeLayout.LayoutParams(
                    (int) utils.dpToPx(48),
                    (int) utils.dpToPx(48));
            layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, imageView.getId());        // <== THIS DOESN'T SEEM TO WORK
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, imageView.getId());
            item_overlay.setLayoutParams(layoutParams);
        } else {
            item_overlay.setVisibility(View.GONE);
        }


        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    public int getPageNumber() {
        return mPageNumber;
    }
}

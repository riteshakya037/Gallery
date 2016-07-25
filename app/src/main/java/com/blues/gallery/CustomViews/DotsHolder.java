package com.blues.gallery.CustomViews;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.blues.gallery.R;

/**
 * Created by Ritesh Shakya on 7/25/2016.
 */

public class DotsHolder extends FrameLayout implements View.OnClickListener {
    private static final AccelerateDecelerateInterpolator ACCELERATE_DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    ThreeDots vDotsView;
    private boolean isChecked;
    private AnimatorSet animatorSet;

    public DotsHolder(Context context) {
        super(context);
        init();
    }

    public DotsHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotsHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotsHolder(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.sample_three_dots, this, true);
        setOnClickListener(this);
        vDotsView = (ThreeDots) findViewById(R.id.vDotsView);
    }

    @Override
    public void onClick(View view) {
        isChecked = !isChecked;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        if (isChecked) {
            vDotsView.setCurrentProgress(0);
            animatorSet = new AnimatorSet();
            ObjectAnimator dotsAnimator = ObjectAnimator.ofFloat(vDotsView, ThreeDots.DOTS_PROGRESS, 0, 1f);
            dotsAnimator.setDuration(100);
            dotsAnimator.setInterpolator(ACCELERATE_DECELERATE_INTERPOLATOR);
            animatorSet.playTogether(
                    dotsAnimator
            );
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    vDotsView.setCurrentProgress(0);
                }
            });

            animatorSet.start();
        }
    }
}

package com.blues.gallery.CustomViews;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import com.blues.gallery.Helper.Utils;
import com.blues.gallery.R;

/**
 * TODO: document your custom view class.
 */
public class ThreeDots extends View {

    private static final int COLOR_1 = 0xFFFFC107;
    private static final int COLOR_2 = 0xFFFF9800;
    private static final int COLOR_3 = 0xFFFF5722;

    private int centerX;
    private int centerY;

    private float currentProgress = .5f;

    private float currentRadius1;
    private float currentDotSize1 = 5;


    public ThreeDots(Context context) {
        super(context);
        init();
    }

    public ThreeDots(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThreeDots(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ThreeDots(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;
        currentDotSize1 = w / 6;
        currentRadius1 = w / 2 - currentDotSize1 / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawOuterDotsFrame(canvas);

    }

    private void drawOuterDotsFrame(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(COLOR_1);

        //For top most dot
        float cX = centerX;
        float cY = (centerY + (float) Utils.mapValueFromRangeToRange(currentProgress, -currentRadius1, currentRadius1));
        canvas.drawCircle(cX, cY, currentDotSize1, paint);
        paint.setColor(COLOR_2);

        canvas.drawLine(centerX, centerY, centerX + currentRadius1, centerY, paint);
        //For left dot
        cX = (float) (centerX + currentRadius1 * Math.cos(180 - Utils.mapValueFromRangeToRange(currentProgress, -30, 30)));
        cY = (float) (centerY + currentRadius1 * Math.sin(180 - Utils.mapValueFromRangeToRange(currentProgress, -30, 30)));
        canvas.drawCircle(cX, cY, currentDotSize1, paint);
        paint.setColor(COLOR_3);
        cX = (float) (centerX + currentRadius1 * Math.cos(90 - Utils.mapValueFromRangeToRange(currentProgress, -30, 30)));
        cY = (float) (centerY + currentRadius1 * Math.sin(90 - Utils.mapValueFromRangeToRange(currentProgress, -30, 30)));
        canvas.drawCircle(cX, cY, currentDotSize1, paint);
        canvas.drawLine(centerX, centerY, centerX, centerY + currentRadius1, paint);
    }


    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;
        postInvalidate();
    }

    public float getCurrentProgress() {
        return currentProgress;
    }


    public static final Property<ThreeDots, Float> DOTS_PROGRESS = new Property<ThreeDots, Float>(Float.class, "dotsProgress") {
        @Override
        public Float get(ThreeDots object) {
            return object.getCurrentProgress();
        }

        @Override
        public void set(ThreeDots object, Float value) {
            object.setCurrentProgress(value);
        }
    };
}

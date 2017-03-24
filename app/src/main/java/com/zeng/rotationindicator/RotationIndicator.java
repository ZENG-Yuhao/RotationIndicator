package com.zeng.rotationindicator;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * <p>
 * Created by ZENG Yuhao. <br>
 * Contact: enzo.zyh@gmail.com
 * </p>
 */

public class RotationIndicator extends View {
    private final static int DEFAULT_DURATION = 200;

    public enum Style {
        RECT, CIRCLE
    }

    private Paint mBackgroundPaint;
    private Paint mAxisPaint;
    private Paint mPointerPaint;
    private int mBackgroundColor = android.R.color.holo_blue_dark;
    private int mAxisColor = android.R.color.white;
    private int mPointerColor = android.R.color.holo_red_light;
    private float mAxisWidth = 3;
    private float mPointerWidth = 5;

    /**
     * Current degrees, clockwise direction, drawing of view is based on this value.
     */
    private float degrees = 0;

    /**
     * When you set a rotation, specially when you call {@link #rotate(float)} which is based on current "real
     * degrees", there is a possible situation that animation has not finished, this variable is used as a buffer to
     * register final "real value/degrees".
     * <p>
     * When {@link #isAnimationEnabled} = false, {@link #finalDegrees} is equivalent to {@link #degrees}.
     */
    private float finalDegrees = 0;

    private boolean isVerticalAxisEnable;
    private boolean isHorizontalAxisEnable;
    private Style mStyle;
    private boolean isAnimationEnabled;
    private int mAnimDuration; // millisecond
    private ValueAnimator mRotateAnimator;

    /**
     * Default constructor.
     */
    public RotationIndicator(Context context) {
        super(context);
        init();
    }

    /**
     * Default constructor.
     */
    public RotationIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Default constructor.
     */
    public RotationIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mStyle = Style.RECT;
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(getResources().getColor(mBackgroundColor));

        mAxisPaint = new Paint();
        mAxisPaint.setStrokeWidth(mAxisWidth);
        mAxisPaint.setColor(getResources().getColor(mAxisColor));
        isHorizontalAxisEnable = true;
        isVerticalAxisEnable = true;

        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerPaint.setStrokeCap(Cap.ROUND);
        mPointerPaint.setStrokeWidth(mPointerWidth);
        mPointerPaint.setColor(getResources().getColor(mPointerColor));

        setBackgroundColor(Color.TRANSPARENT); // this code will ignore all background setups.

        mRotateAnimator = new ValueAnimator();
        mRotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mRotateAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // when animation value changes, update current degrees.
                degrees = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        setAnimationEnabled(true);
        setAnimationDuration(DEFAULT_DURATION);
    }

    public void setStyle(Style style) {
        mStyle = style;
        invalidate();
    }

    public Style getStyle() {
        return mStyle;
    }

    public void setAnimationDuration(int duration) {
        mAnimDuration = duration;
        mRotateAnimator.setDuration(mAnimDuration);
    }

    public int getAnimationDuration() {
        return mAnimDuration;
    }

    public void setAnimationEnabled(boolean enabled) {
        isAnimationEnabled = enabled;
        if (!isAnimationEnabled && mRotateAnimator.isRunning()) {
            mRotateAnimator.cancel();
        }
    }

    public boolean isAnimationEnabled() {
        return isAnimationEnabled;
    }

    private void startRotateAnimation(float startDegrees, float endDegrees) {
        if (mRotateAnimator.isRunning()) mRotateAnimator.cancel();
        mRotateAnimator.setFloatValues(startDegrees, endDegrees);
        mRotateAnimator.start();
    }

    /**
     * Set absolute degrees.
     *
     * @param degrees related to 0 degree.
     */
    public void setRotation(float degrees) {
        if (!isAnimationEnabled) {
            this.degrees = degrees;
            finalDegrees = degrees;
            invalidate();
        } else {
            finalDegrees = degrees;
            startRotateAnimation(degrees, finalDegrees);
        }
    }

    /**
     * Increase/decrease rotation degrees.
     *
     * @param delta based on current degrees.
     */
    public void rotate(float delta) {
        if (!isAnimationEnabled) {
            this.degrees += delta;
            finalDegrees = degrees;
            invalidate();
        } else {
            finalDegrees += delta;
            startRotateAnimation(degrees, finalDegrees);
        }
    }

    public float getRotation() {
        return degrees;
    }

    /**
     * This method is deprecated, you should use {@link #setBackgroundColor(int)}
     */
    @Deprecated
    public void setCircleColor(int color) {
        setBackgroundColor(color);
    }

    public void setBackGroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundPaint.setColor(getResources().getColor(mBackgroundColor));
        invalidate();
    }

    public void setAxisColor(int color) {
        mAxisColor = color;
        mAxisPaint.setColor(getResources().getColor(mAxisColor));
        invalidate();
    }

    public void setAxisWidth(float width) {
        mAxisWidth = width;
        mAxisPaint.setStrokeWidth(mAxisWidth);
        invalidate();
    }

    public void setPointerColor(int color) {
        mPointerColor = color;
        mPointerPaint.setColor(getResources().getColor(mPointerColor));
        invalidate();
    }

    public void setPointerWidth(float width) {
        mPointerWidth = width;
        mPointerPaint.setStrokeWidth(mPointerWidth);
        invalidate();
    }

    public void setVerticalAxisEnable(boolean enable) {
        isVerticalAxisEnable = enable;
        invalidate();
    }

    public boolean isVerticalAxisEnable() {
        return isVerticalAxisEnable;
    }

    public void setHorizontalAxisEnable(boolean enable) {
        isHorizontalAxisEnable = enable;
        invalidate();
    }

    public boolean isHorizontalAxisEnable() {
        return isHorizontalAxisEnable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mStyle == Style.RECT)
            drawRectIndicator(canvas);
        else if (mStyle == Style.CIRCLE)
            drawCircleIndicator(canvas);
    }

    private void drawRectIndicator(Canvas canvas) {
        float centX = getWidth() / 2;
        float centY = getHeight() / 2;
        // draw rect background
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBackgroundPaint);

        // draw axis
        if (isHorizontalAxisEnable)
            canvas.drawLine(0, centY, getWidth(), centY, mAxisPaint); // X-axis
        if (isVerticalAxisEnable)
            canvas.drawLine(centX, 0, centX, getHeight(), mAxisPaint); // Y-axis

        // draw pointer
        float pLen = (float) Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2)); // length of pointer
        canvas.save();
        canvas.translate(centX, centY);
        canvas.rotate(degrees);
        canvas.drawLine(-pLen / 2, 0, pLen / 2, 0, mPointerPaint);
        canvas.restore();

    }

    private void drawCircleIndicator(Canvas canvas) {
        float radius = Math.min(getWidth() / 2, getHeight() / 2);
        float centX = getWidth() / 2;
        float centY = getHeight() / 2;
        //draw circle background
        canvas.drawCircle(centX, centY, radius, mBackgroundPaint);

        // draw axis
        if (isHorizontalAxisEnable)
            canvas.drawLine(centX - radius, centY, centX + radius, centY, mAxisPaint); // X-axis
        if (isVerticalAxisEnable)
            canvas.drawLine(centX, centY - radius, centX, centY + radius, mAxisPaint); // Y-axis

        // draw pointer
        canvas.save();
        canvas.translate(centX, centY);
        canvas.rotate(degrees);
        canvas.drawLine(0, 0, 0, -(radius / 3), mPointerPaint); // little line pointing to UP.
        canvas.drawLine(-radius, 0, radius, 0, mPointerPaint);
        canvas.restore();
    }
}

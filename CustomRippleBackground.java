/**
 * @file CustomRippleBackground.java
 * @Synopsis  
 * @author Arrui.c@gmail.com
 * @version 
 * @date 2015-09-11
 */
package com.example.ripple;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

public class CustomRippleBackground extends RelativeLayout {
    private static final int DEFAULT_COLOR = Color.parseColor("#673ab7");
    private static final int DEFAULT_ALPHA = (int) (255 * 0.2f);
    private static final long DEFAULT_DURATION = 500;
    private static final float DEFAULT_RADIUS = 100f;
    private static final float DEFAULT_SCALE = 6f / DEFAULT_RADIUS;
    private static final float DEFAULT_PERCENT = 0.7f;

    private Paint paint;
    private AnimatorSet animatorSet;
    private ArrayList<RippleView> rippleViewList;
    private RippleAnimationListener listener = null;
    private boolean animationRunning = false;

    public void setRippleAnimationListener(RippleAnimationListener listener) {
        this.listener = listener;
    }

    public interface RippleAnimationListener {
        public void onRippleAnimationStart();

        public void onRippleAnimationEnd();
    }

    public CustomRippleBackground(Context context) {
        super(context);
        init(context);
    }

    public CustomRippleBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        float rippleRadius = DEFAULT_RADIUS;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(DEFAULT_COLOR);
        paint.setAlpha(DEFAULT_ALPHA);

        LayoutParams rippleParams = new LayoutParams((int) (2 * rippleRadius), (int) (2 * rippleRadius));
        rippleParams.addRule(CENTER_IN_PARENT, TRUE);

        rippleViewList = new ArrayList<CustomRippleBackground.RippleView>();
        animatorSet = new AnimatorSet();
        ArrayList<Animator> animatorList = new ArrayList<Animator>();

        animatorList.addAll(initRipple(rippleParams, true));
        animatorList.addAll(initRipple(rippleParams, false));

        animatorSet.playTogether(animatorList);
    }

    /**
     * first ripple, show full
     * second ripple , show 70%
     * 
     * @param rippleParams
     * @param isFirst
     * @return
     */
    private ArrayList<Animator> initRipple(LayoutParams rippleParams, final boolean isFirst) {
        ArrayList<Animator> animatorList = new ArrayList<Animator>();

        final RippleView rippleView = new RippleView(getContext());
        addView(rippleView, rippleParams);
        rippleViewList.add(rippleView);

        float scale = isFirst ? 1.0f : DEFAULT_PERCENT;
        long delay = (long) (isFirst ? 0 : DEFAULT_DURATION * (1 - DEFAULT_PERCENT));

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleX", DEFAULT_SCALE, scale);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleXAnimator.setDuration(DEFAULT_DURATION);
        scaleXAnimator.setStartDelay(delay);
        animatorList.add(scaleXAnimator);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(rippleView, "ScaleY", DEFAULT_SCALE, scale);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleYAnimator.setDuration(DEFAULT_DURATION);
        scaleYAnimator.setStartDelay(delay);
        animatorList.add(scaleYAnimator);

        scaleXAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                rippleView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isFirst) {
                    animationRunning = false;
                    if (listener != null) {
                        listener.onRippleAnimationEnd();
                    }
                }
            }

        });

        return animatorList;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private class RippleView extends View {

        public RippleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
            this.setScaleX(DEFAULT_SCALE);
            this.setScaleY(DEFAULT_SCALE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int radius = (Math.min(getWidth(), getHeight())) / 2;
            canvas.drawCircle(radius, radius, radius, paint);
        }
    }

    /**
     * start the ripple animation
     */
    public void startRippleAnimation() {
        animationRunning = true;
        resetRipple();
        animatorSet.start();
    }

    /**
     * stop the ripple animation
     */
    public void stopRippleAnimation() {
        animatorSet.cancel();
        animationRunning = false;
        if (listener != null) {
            listener.onRippleAnimationEnd();
        }
    }

    /**
     * check if the animation is running
     * 
     * @return
     */
    public boolean isAnimationRunning() {
        return this.animationRunning;
    }

    /**
     * hide the animation immediately
     */
    public void hideRipple() {
        animatorSet.cancel();
        resetRipple();
    }

    private void resetRipple() {
        for (RippleView rippleView : rippleViewList) {
            rippleView.setScaleX(DEFAULT_SCALE);
            rippleView.setScaleY(DEFAULT_SCALE);
            rippleView.setVisibility(View.INVISIBLE);
        }
    }

}

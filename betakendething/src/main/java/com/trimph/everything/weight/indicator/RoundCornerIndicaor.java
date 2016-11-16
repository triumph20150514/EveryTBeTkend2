package com.trimph.everything.weight.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

/**
 * @description: Project TrimphJuneFifty
 * Package_name com.trimh.nuannuan.view.indicator
 * Created by Trimph on 2016/7/16.
 */

public class RoundCornerIndicaor extends View implements IPageListener {

    public ViewPager viewPager;
    public int indicatorWidth;
    public int indicatorHight;
    //间隔
    public int indicatorGap;
    public Context context;

    public int cornerRadius;

    public int count;
    public List<GradientDrawable> unGradientDrawables = new ArrayList<>();
    public List<Rect> unRect = new ArrayList<>();

    private GradientDrawable selectDrawable = new GradientDrawable();
    private Rect selectRect = new Rect();

    private boolean isSnap = false;

    public RoundCornerIndicaor(Context context) {
        this(context, null);
    }

    public RoundCornerIndicaor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        indicatorWidth = dp2px(6);
        indicatorHight = dp2px(6);
        //间隔
        indicatorGap = dp2px(10);
        cornerRadius = dp2px(3);
    }

    public RoundCornerIndicaor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void setViewPager(ViewPager viewPager) {

        if (viewPager == null) {
            return;
        }
        indicatorWidth = dp2px(6);
        indicatorHight = dp2px(6);
        //间隔
        indicatorGap = dp2px(10);
        cornerRadius = dp2px(3);
        this.viewPager = viewPager;
        viewPager.removeOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);

        count = viewPager.getAdapter().getCount();

        createdIndicator();
    }

    private boolean isValid(ViewPager vp) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL!");
        }

        if (vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager adapter can not be NULL!");
        }

        return true;
    }

    public void setCurrentItem(int item) {
        if (isValid(viewPager)) {
            viewPager.setCurrentItem(item);
        }

    }

    private void createdIndicator() {
        unGradientDrawables.clear();
        unRect.clear();
        for (int i = 0; i < count; i++) {
            unGradientDrawables.add(new GradientDrawable());
            unRect.add(new Rect());
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         *水平 垂直位移   。。。。。。。
         */
        int verticalOffset = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2 - indicatorHight / 2;
        /**
         * 宽度
         */
        int indicatorLayoutWidth = indicatorWidth * count + indicatorGap * (count - 1);
        /**
         * 水平位移
         */
        int horizontalOffset = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 - indicatorLayoutWidth / 2;

        drawUnselect(canvas, count, horizontalOffset, verticalOffset);

        drawselect(canvas, count, horizontalOffset, verticalOffset);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHight(heightMeasureSpec));
    }


    private int measureHight(int heightMeasureSpec) {

        int result;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);

        if (mode == MeasureSpec.EXACTLY || count == 0) {
            result = size;
        } else {
            int padding = getPaddingTop() + getPaddingBottom();
            result = padding + indicatorHight;
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY || count == 0) {
            result = size;
        } else {

            int padding = getPaddingLeft() + getPaddingRight();
            result = padding + indicatorWidth * count + indicatorGap * (count - 1);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    /**
     * 选中的
     *
     * @param canvas
     * @param count
     * @param horizontalOffset
     * @param verticalOffset
     */
    private void drawselect(Canvas canvas, int count, int horizontalOffset, int verticalOffset) {
       /*先判断是否是激素跳转的*/

        int delta = (int) ((indicatorGap + indicatorWidth) * (isSnap ? 0 : positionOffset));

        selectRect.left = horizontalOffset + (indicatorWidth + indicatorGap) * currentItem + delta;
        selectRect.top = verticalOffset;
        selectRect.right = selectRect.left + indicatorWidth;
        selectRect.bottom = selectRect.top + indicatorHight;

        selectDrawable.setCornerRadius(10);
        selectDrawable.setColor(Color.RED);
        selectDrawable.setBounds(selectRect);
        selectDrawable.draw(canvas);


    }

    public int currentItem;
    public float positionOffset;

    /**
     * 未选中的
     *
     * @param canvas
     * @param count
     * @param horizontalOffset
     * @param verticalOffset
     */
    private void drawUnselect(Canvas canvas, int count, int horizontalOffset, int verticalOffset) {


        for (int i = 0; i < this.count; i++) {

            Rect rect = unRect.get(i);

            rect.left = horizontalOffset + (indicatorWidth + indicatorGap) * i;
            rect.right = rect.left + indicatorWidth;
            rect.top = verticalOffset;
            rect.bottom = rect.top + indicatorHight;

            GradientDrawable gradientDrawable = unGradientDrawables.get(i);
            gradientDrawable.setColor(Color.BLUE);
            gradientDrawable.setCornerRadius(10);
            gradientDrawable.setStroke(5, Color.WHITE);
            gradientDrawable.setBounds(rect);
            gradientDrawable.draw(canvas);

        }


    }

    @Override
    public void setViewPager(ViewPager viewPager, int realCount) {

        if (viewPager == null) {
            return;
        }
        this.viewPager = viewPager;

        indicatorWidth = dp2px(6);
        indicatorHight = dp2px(6);
        //间隔
        indicatorGap = dp2px(10);

        viewPager.removeOnPageChangeListener(this);
        viewPager.addOnPageChangeListener(this);

        count = viewPager.getAdapter().getCount();


        createdIndicator();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!isSnap) {
            /**
             * position:当前View的位置
             * positionOffset:当前View的偏移量比例.[0,1)
             */
            currentItem = position;
            this.positionOffset = positionOffset;
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (isSnap) {
            /**
             * position:当前View的位置
             */
            currentItem = position;
            invalidate();
        }
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

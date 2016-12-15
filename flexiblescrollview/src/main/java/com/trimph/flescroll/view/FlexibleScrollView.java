package com.trimph.flescroll.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * author: Trimph
 * data: 2016/12/9.
 * description : 弹性滚动
 */

public class FlexibleScrollView extends FrameLayout {

    public int distance = 20; //当移动大于20才开始滚动

    public int downY, downX;
    View content;
    Scroller scroller;
    public Rect origin = new Rect();

    public FlexibleScrollView(Context context) {
        super(context);
        Log.e("ScrollView-----", "一个参数的");
        init(context);
    }


    public FlexibleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e("ScrollView-----", "两个参数的");
        init(context);
    }

    public FlexibleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e("ScrollView-----", "三个参数的");
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("ScrollView-----", "onMeasure");

    }


    private void init(Context context) {
        scroller = new Scroller(context);
        overScroller = new OverScroller(context);
    }

    OverScroller overScroller;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("ScrollView-----", "onSizeChanged" + content.getTop());
//        origin.set(content.getLeft(), content.getTop(), content.getRight(), content.getBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e("ScrollView-----", "onLayout" + content.getTop());
        if (content == null) {
            return;
        }
        origin.set(content.getLeft(), content.getTop(), content.getRight(), content.getBottom());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("ScrollView-----", "dispatchTouchEvent" + super.dispatchTouchEvent(ev));

        if (content == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_UP:

                int offset1 = ((int) ev.getY() - downY);
                Log.e("Content", content.getTop() + "origint top:" + origin.top + " scrollY:" + getScrollY() + " oofset" + offset);
//                overScroller.startScroll(0, content.getTop(), 0, origin.top);

                scroller.startScroll(getScrollX(), getScrollY(), 0, -offset);
                invalidate();
//                content.scrollTo(0, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                //大于最小滑动距离
                if (((int) ev.getY() - downY) > distance) {
                    Log.e("X:", (int) ev.getX() + "　Y：" + ((int) ev.getY() - downY));

                    offset = ((int) ev.getY() - downY);
                    offsetX = ((int) ev.getX() - downX);
//                    content.scrollTo(0, -offset);

                    scroller.startScroll(getScrollX(), getScrollY(), offsetX, offset, 200);
                    invalidate();
//                    content.layout(origin.left, origin.top + offset, origin.right, origin.bottom + offset);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("ScrollView-----", "onInterceptTouchEvent" + super.onInterceptTouchEvent(ev));
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("ScrollView-----", "onTouchEvent" + super.onTouchEvent(ev));
        return super.onTouchEvent(ev);
    }

    int offset, offsetX;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     * 执行周期：
     * 在ScrollView加载完它的子布局后才会执行此方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("ScrollView", "FinishInflate" + getChildCount());
        if (getChildCount() != 0) {
            content = getChildAt(0);
        }
    }

}

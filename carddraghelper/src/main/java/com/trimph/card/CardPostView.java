package com.trimph.card;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Trimph
 * data: 2016/11/18.
 * description:卡片左右滑动
 */

public class CardPostView extends FrameLayout {
    public ViewDragHelper viewDragHelper;
    public Adapter adapter;
    public int itemNums;

    public CardPostView(Context context) {
        super(context);
    }

    public CardPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardPostView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public float startX, startY;
    public int distance = 400;
    public static int DIRECTION = -1;
    public static int DIRECTION_RIGHT = 1;
    public static int DIRECTION_LEFT = 2;
    public static int DIRECTION_TOP = 3;
    public static int DIRECTION_BOTTOM = 4;
    public int mMaxPostcardNum = 3;


    public int getmMaxPostcardNum() {
        return mMaxPostcardNum;
    }

    public void setmMaxPostcardNum(int mMaxPostcardNum) {
        if (mMaxPostcardNum < 3) {
            throw new IllegalThreadStateException("maxNum do not smartter more than 3");
        }
        this.mMaxPostcardNum = mMaxPostcardNum;
    }

    private void init(Context context) {

        //
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            /**
             * 触摸到屏幕边缘
             * @param edgeFlags
             * @param pointerId
             */
            @Override
            public void onEdgeTouched(int edgeFlags, int pointerId) {
                super.onEdgeTouched(edgeFlags, pointerId);

            }

            /**
             * 主要通过它判断
             * @param releasedChild
             * @param xvel
             * @param yvel
             */
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);

                int x = (int) releasedChild.getX();
                int y = (int) releasedChild.getY();

                //判断最小移动距离
                if ((x - startX) > distance) {
                    DIRECTION = DIRECTION_RIGHT;
                } else {
                    DIRECTION = DIRECTION_LEFT;
                }
                dropCard(DIRECTION);


            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                //通过X轴移动距离判断
                if (capturedChild != null) {
                    startX = capturedChild.getX();
                    startY = capturedChild.getY();
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return super.clampViewPositionHorizontal(child, left, dx);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return super.clampViewPositionVertical(child, top, dy);
            }
        });

    }

    /**
     * 删除掉
     *
     * @param direction
     */
    private void dropCard(int direction) {
        removeViewAt(getChildCount() - 1);

        View firstView = getChildAt(getChildCount() - 1);
        View twoView = getChildAt(getChildCount() - 2);

        if (firstView != null) {
            firstView.animate().setDuration(100).translationY(distanceY).start();
        }

        if (twoView != null) {
            firstView.animate().setDuration(100).translationY(distanceY * 2).start();
        }

        //


    }


    public int distanceY = 40;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.shouldInterceptTouchEvent(event);
        return true;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        itemNums = adapter.getItemNum();
        layoutPostCard();
    }

    /**
     * 完成
     */
    private void layoutPostCard() {

        int itemCount = itemNums < mMaxPostcardNum ? itemNums : mMaxPostcardNum;

        for (int i = 0; i < itemCount; i++) {
            addCardView(i);
        }


    }

    /**
     * 添加View
     *
     * @param
     */
    private void addCardView(int position) {

        View view = null;

        if (adapter != null) {
            view = adapter.createView(this);
            if (view!=null){
                adapter.viewList.add(view);
            }
        }
        this.addView(view);
    }


    /**
     * 定义一个抽象类来装载数据
     */
    public abstract class Adapter {

        List<View> viewList = new ArrayList<>();

        public abstract int getItemNum();

        public abstract View createView(ViewGroup viewGroup);

        public abstract void BindView(View view, int position);
    }


}

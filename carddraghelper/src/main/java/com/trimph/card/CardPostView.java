package com.trimph.card;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * author: Trimph
 * data: 2016/11/18.
 * description:卡片左右滑动
 */

public class CardPostView extends FrameLayout {
    public ViewDragHelper viewDragHelper;
    public ICardAdapter adapter;
    public int itemNums;

    public CardPostView(Context context) {
        super(context);
        init(context);
    }

    public CardPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
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
                Log.e("Callback onViewRelease ", "----------------xvel" + xvel);
                int x = (int) releasedChild.getX();
                int y = (int) releasedChild.getY();

                //判断最小移动距离
                if ((x - startX) > distance) {
                    DIRECTION = DIRECTION_RIGHT;
                    dropCard(DIRECTION);
                } else {
                    mIsBacked = false;
                    viewDragHelper.settleCapturedViewAt((int) startX, (int) startY);
                    invalidate();
                }

            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                super.onViewCaptured(capturedChild, activePointerId);
                //通过X轴移动距离判断
                Log.e("Callback onViewCapture ", "----------------Horizontal");
                if (mIsBacked) {
                    startX = capturedChild.getX();
                    startY = capturedChild.getY();
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.e("Callback   Clamp", "----------------Horizontal" + left);
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                Log.e("Callback   Clamp", "----------------Vertical" + top);
                return top;
            }
        });

    }

    private boolean mIsBacked = true;

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
            firstView.animate().scaleX(1f).scaleX(1f).setDuration(100).translationYBy(-distanceY).start();
        }

        if (twoView != null) {
            twoView.animate().scaleX(0.95f).scaleY(0.95f).translationYBy(-distanceY).setDuration(100).start();
        }

        //删除后  不足三个是添加数据
        if (currentPosition < itemNums) {  //当前位置小于总条数大小
            addCardView(currentPosition);
        }
    }

    public int distanceY = 40;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev) && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event); //
        return true;
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true))
            invalidate();

        if (getChildAt(0) != null && getChildAt(0).getLeft() == startX && getChildAt(0).getTop() == startY)
            mIsBacked = true;

    }

    public ICardAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ICardAdapter adapter) {
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
        View view = getView(position);
        Log.e("Position", position + "");
        if (position == 0) {
            view.setScaleX(1);
            view.setScaleY(1);
            view.setTranslationY(0);
        } else if (position == 1) {
            view.setScaleX(0.95f);
            view.setScaleY(0.95f);
            view.setTranslationY(distanceY);
        } else {
            view.setScaleX(0.5f);
            view.setScaleY(0.5f);
            view.setTranslationY(distanceY * 2);
            view.animate().scaleY(0.9f).scaleX(0.9f).setDuration(100).start();
        }
        addView(view, 0);
        currentPosition++;
    }

    public int currentPosition = 0;

    private View getView(int position) {
        int type = position % mMaxPostcardNum;
        View view = null;
        if (adapter != null) {
            Log.e("Callback", "size::" + adapter.viewList.size() + "");
            if (adapter.viewList.size() < mMaxPostcardNum) { //列表中条目小于3时
                view = adapter.createView(this);
                if (view != null) {
                    adapter.viewList.add(view);
                }
            } else { //当条目大于2
                view = adapter.viewList.get(type);  //当执行删除操作后会进入到这里
                if (view == null) {
                    view = adapter.createView(this);
                    adapter.viewList.add(view);
                }
            }
        }
        adapter.BindView(view, position);
        return view;
    }


//    /**
//     * 定义一个抽象类来装载数据
//     */
//    public abstract class Adapter {
//
//        List<View> viewList = new ArrayList<>();
//
//        public abstract int getItemNum();
//
//        public abstract View createView(ViewGroup viewGroup);
//
//        public abstract void BindView(View view, int position);
//    }


}

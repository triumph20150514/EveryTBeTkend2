package com.trimph.flescroll.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * author: Trimph
 * data: 2016/12/14.
 * description:
 */

public class FlexiBleLinearView extends FrameLayout {

    public static Scroller scroller;

    public FlexiBleLinearView(Context context) {
        super(context);
        init(context);
    }

    public FlexiBleLinearView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
    }

    public void smoothScroll() {
        Log.e("ScrollY", ":" + getScrollY() + " ScrollX:" + getScaleX());
        scroller.startScroll(getScrollX(), getScrollY(), -500, -200, 5000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
